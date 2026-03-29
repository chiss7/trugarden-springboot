package com.chis.trugarden.application.order;

import com.chis.trugarden.application.cart.abstractions.CartRepository;
import com.chis.trugarden.application.order.abstractions.OrderRepository;
import com.chis.trugarden.application.order.create.CreateOrderResult;
import com.chis.trugarden.application.order.payment.dtos.PaymentRequest;
import com.chis.trugarden.application.order.payment.dtos.PaymentResult;
import com.chis.trugarden.application.order.payment.factory.PaymentStrategyFactory;
import com.chis.trugarden.application.order.payment.strategy.PaymentStrategy;
import com.chis.trugarden.application.product.StockService;
import com.chis.trugarden.application.user.abstractions.AddressRepository;
import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.domain.order.Order;
import com.chis.trugarden.domain.order.OrderItem;
import com.chis.trugarden.domain.order.Payment;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.domain.user.AddressErrors;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import com.chis.trugarden.shared.enums.Currency;
import com.chis.trugarden.shared.enums.PaymentProvider;
import com.chis.trugarden.shared.enums.PaymentStatus;
import com.chis.trugarden.shared.properties.PayphoneProperties;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PayphoneProperties payphoneProperties;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final StockService stockService;

    @Transactional
    public Result<CreateOrderResult> createOrder(Cart cart, Address shippingAddress) {
        Result<Address> addressResult = validateOrCreateAddress(shippingAddress, AuthenticationHelper.isAuthenticated(), cart.getUser());
        if (addressResult.isFailure()) {
            return Result.failure(addressResult.getError());
        }

        Result<Void> stockResult = stockService.deductStock(cart.getCartItems());
        if (stockResult.isFailure()) {
            return Result.failure(stockResult.getError());
        }

        Payment payment = Payment.ofNew(
                null,
                cart.getGrandTotal(),
                Currency.USD,
                PaymentStatus.PENDING
        );

        Order order = Order.newFromCart(cart, addressResult.getValue(), payment);
        Order savedOrder = orderRepository.save(order);

        Cart cartMarkedAsOrdered = cart.markAsPendingPayment();
        cartRepository.save(cartMarkedAsOrdered);

        // create payment link
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(PaymentProvider.PAYPHONE);
        PaymentRequest paymentRequest = buildPaymentRequest(savedOrder);

        PaymentResult result = paymentStrategy.createPaymentLink(paymentRequest);
        Payment paymentWithLink = savedOrder.getPayment().withPaymentLink(
                result.getPaymentUrl()
        );
        Order orderWithPayment = savedOrder.withUpdatedPayment(paymentWithLink);
        orderRepository.save(orderWithPayment);

        log.info("Order created successfully with id: {} for sessionId: {}", savedOrder.getId(), cart.getSessionId());
        return Result.success(new CreateOrderResult(orderWithPayment.getId(), result.getPaymentUrl()));
    }

    private Result<Address> validateOrCreateAddress(Address shippingAddress, boolean isAuthenticated, User user) {
        if (isAuthenticated) {
            if (shippingAddress.getId() == null) {
                log.info("User {} has not provide a shipping address. Creating new address.", user.getId());
                User withNewAddress = user.withNewAddress(shippingAddress);
                userRepository.save(withNewAddress);
                return Result.success(shippingAddress);
            }
            Address userAddress = user.getAddresses().stream()
                    .filter(addr -> addr.getId().equals(shippingAddress.getId()))
                    .findFirst()
                    .orElse(null);
            if (userAddress == null) {
                log.error("User {} does not have address with id: {}", user.getId(), shippingAddress.getId());
                return Result.failure(AddressErrors.notFound());
            }
            return Result.success(userAddress);
        }

        if (shippingAddress.getId() == null) {
            log.info("Creating address for guest user with sessionId: {}", shippingAddress.getSessionId());
            return Result.success(addressRepository.save(shippingAddress));
        }

        Optional<Address> guestAddress = addressRepository.findByIdAndSessionId(shippingAddress.getId(), shippingAddress.getSessionId());
        if (guestAddress.isEmpty()) {
            log.error("Guest {} does not have address with id: {}", shippingAddress.getSessionId(), shippingAddress.getId());
            return Result.failure(AddressErrors.notFound());
        }

        return Result.success(guestAddress.get());
    }

    private PaymentRequest buildPaymentRequest(Order order) {
        String additionalData = order.getUser() != null ? "userId:" + order.getUser().getId() : "guestSession:" + order.getSessionId();
        BigDecimal amountWithoutTax = BigDecimal.ZERO;
        BigDecimal amountWithTax = BigDecimal.ZERO;

        for (OrderItem item : order.getOrderItems()) {
            if (item.getTaxPercentage() > 0) {
                amountWithTax = amountWithTax.add(item.getSubtotal());
            } else {
                amountWithoutTax = amountWithoutTax.add(item.getSubtotal());
            }
        }

        return PaymentRequest.builder()
                .orderId(order.getOrderId())
                .amount(order.getGrandTotal())
                .amountWithoutTax(amountWithoutTax)
                .amountWithTax(amountWithTax)
                .tax(order.getTotalTax())
                .currency(Currency.USD.name())
                .description("Payment for order " + order.getId())
                .callbackUrl(payphoneProperties.getCallbackUrl())
                .cancellationUrl(payphoneProperties.getCancelUrl())
                .additionalData(additionalData)
                .build();
    }
}
