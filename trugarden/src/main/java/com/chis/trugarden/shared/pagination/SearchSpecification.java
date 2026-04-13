package com.chis.trugarden.shared.pagination;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class SearchSpecification<T> implements Specification<T> {

    private final transient List<SearchFilter> filters;
    private static final List<Class<?>> NUMBER_CLASS = List.of(BigDecimal.class, Integer.class, Long.class, Double.class, Float.class);

    public List<SearchFilter> getFilters() {
        return filters;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = filters
                .stream()
                .map(filter -> this.filterToPredicate(filter, root, criteriaBuilder))
                .toList();
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate filterToPredicate(SearchFilter filter, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Path<?> path = getPath(root, filter.key());
        String stringValue = filter.value().toString();

        return switch (filter.operation()) {
            case EQUAL -> criteriaBuilder.equal(path, filter.value());
            case LIKE -> criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), "%" + stringValue.toLowerCase() + "%");
            case STARTS_WITH -> criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), stringValue.toLowerCase() + "%");
            case ENDS_WITH -> criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), "%" + stringValue.toLowerCase());
            case GREATER_THAN, LESS_OR_EQUAL, LESS_THAN, GREATER_OR_EQUAL -> getPredicateElements(filter, path, criteriaBuilder);
            case IN -> handleInOperation(filter, path, criteriaBuilder);
            case BETWEEN -> handleBetween(filter, path, criteriaBuilder);
            case NOT_EQUAL -> criteriaBuilder.notEqual(path, filter.value());
            case NOT_IN -> criteriaBuilder.not(handleInOperation(filter, path, criteriaBuilder));
        };
    }

    private Path<?> getPath(Root<T> root, String key) {
        String[] parts = key.split("\\.");
        Path<?> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }

    private Predicate handleInOperation(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(path);
        ((Collection<?>) filter.value()).forEach(inClause::value);
        return inClause;
    }

    private Predicate handleBetween(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        if (Date.class.isAssignableFrom(path.getJavaType())) {
            return handleBetweenDates(filter, path, criteriaBuilder);
        } else if (BigDecimal.class.isAssignableFrom(path.getJavaType())) {
            return handleBetweenNumbers(filter, path, criteriaBuilder);
        } else {
            return handleBetweenStrings(filter, path, criteriaBuilder);
        }
    }

    private Predicate getPredicateElements(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        try {
            if (Date.class.isAssignableFrom(path.getJavaType())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(filter.value().toString());
                return switch (filter.operation()) {
                    case GREATER_THAN -> criteriaBuilder.greaterThan(path.as(Date.class), date);
                    case GREATER_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(path.as(Date.class), date);
                    case LESS_THAN -> criteriaBuilder.lessThan(path.as(Date.class), date);
                    case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(path.as(Date.class), date);
                    default -> null;
                };
            }

            if (NUMBER_CLASS.stream().anyMatch(clazz -> clazz.isAssignableFrom(path.getJavaType()))) {
                BigDecimal value = new BigDecimal(filter.value().toString());
                return switch (filter.operation()) {
                    case GREATER_THAN -> criteriaBuilder.greaterThan(path.as(BigDecimal.class), value);
                    case GREATER_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(path.as(BigDecimal.class), value);
                    case LESS_THAN -> criteriaBuilder.lessThan(path.as(BigDecimal.class), value);
                    case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(path.as(BigDecimal.class), value);
                    default -> null;
                };
            }
        } catch (Exception e) {
            log.error("Error parsing date", e);
        }

        String value = filter.value().toString();
        return switch (filter.operation()) {
            case GREATER_THAN -> criteriaBuilder.greaterThan(path.as(String.class), value);
            case GREATER_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(path.as(String.class), value);
            case LESS_THAN -> criteriaBuilder.lessThan(path.as(String.class), value);
            case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(path.as(String.class), value);
            default -> null;
        };
    }

    private Predicate handleBetweenDates(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lowerDate;
        Date upperDate;
        try {
            lowerDate = dateFormat.parse(filter.value().toString());
            upperDate = dateFormat.parse(filter.upperValue().toString());
        } catch (ParseException e) {
            return handleBetweenStrings(filter, path, criteriaBuilder);
        }
        return criteriaBuilder.between(path.as(Date.class), lowerDate, upperDate);
    }

    private Predicate handleBetweenNumbers(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        BigDecimal lowerNumber = new BigDecimal(filter.value().toString());
        BigDecimal upperNumber = new BigDecimal(filter.upperValue().toString());
        return criteriaBuilder.between(path.as(BigDecimal.class), lowerNumber, upperNumber);
    }

    private Predicate handleBetweenStrings(SearchFilter filter, Path<?> path, CriteriaBuilder criteriaBuilder) {
        String lowerString = filter.value().toString();
        String upperString = filter.upperValue().toString();
        return criteriaBuilder.between(path.as(String.class), lowerString, upperString);
    }
}
