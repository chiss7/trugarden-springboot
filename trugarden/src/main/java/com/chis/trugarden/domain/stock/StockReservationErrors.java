package com.chis.trugarden.domain.stock;

import com.chis.trugarden.shared.result.Error;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockReservationErrors {

    public static Error insufficientStock(String productName, int requested, int available) {
        return Error.conflict(
                "STOCK_RESERVATION_INSUFFICIENT_STOCK",
                String.format("Stock insuficiente para '%s'. Solicitado: %d, Disponible: %d", 
                        productName, requested, available)
        );
    }

    public static Error reservationNotFound(Long orderId) {
        return Error.notFound(
                "STOCK_RESERVATION_NOT_FOUND",
                String.format("No se encontró reserva para la orden %d", orderId)
        );
    }

    public static Error reservationExpired(Long orderId) {
        return Error.conflict(
                "STOCK_RESERVATION_EXPIRED",
                String.format("La reserva para la orden %d ha expirado", orderId)
        );
    }

    public static Error reservationAlreadyConfirmed(Long orderId) {
        return Error.conflict(
                "STOCK_RESERVATION_ALREADY_CONFIRMED",
                String.format("La reserva para la orden %d ya fue confirmada", orderId)
        );
    }

    public static Error reservationAlreadyCancelled(Long orderId) {
        return Error.conflict(
                "STOCK_RESERVATION_ALREADY_CANCELLED",
                String.format("La reserva para la orden %d ya fue cancelada", orderId)
        );
    }

    public static Error invalidReservationStatus(Long orderId, String currentStatus) {
        return Error.conflict(
                "STOCK_RESERVATION_INVALID_STATUS",
                String.format("Estado inválido para reserva de orden %d: %s", orderId, currentStatus)
        );
    }
}
