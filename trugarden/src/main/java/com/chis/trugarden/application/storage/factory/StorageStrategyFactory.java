package com.chis.trugarden.application.storage.factory;

import com.chis.trugarden.application.storage.strategy.StorageStrategy;
import com.chis.trugarden.shared.enums.StorageProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StorageStrategyFactory {

    private final Map<StorageProvider, StorageStrategy> strategies;

    public StorageStrategyFactory(List<StorageStrategy> providers) {
        this.strategies = providers.stream()
                .collect(Collectors.toMap(StorageStrategy::provider, Function.identity()));
    }

    public StorageStrategy getStrategy(StorageProvider provider) {
        StorageStrategy strategy = strategies.get(provider);
        if (strategy == null) {
            throw new IllegalStateException("No storage provider found for: " + provider);
        }
        return strategy;
    }
}
