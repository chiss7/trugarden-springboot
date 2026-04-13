package com.chis.trugarden.shared.pagination;

import com.chis.trugarden.shared.enums.SearchOperation;

public record SearchFilter (
        String key,
        Object value,
        SearchOperation operation,
        Object upperValue
){
}
