package com.chis.trugarden.shared.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trugarden.stock")
@Getter
@Setter
public class StockProperties {
    
    /**
     * Tiempo de vida (TTL) de una reserva de stock en minutos.
     * Por defecto: 15 minutos.
     */
    private int reservationTtlMinutes = 15;
    
    /**
     * Intervalo de ejecución del job de expiración en milisegundos.
     * Por defecto: 60000ms (1 minuto).
     */
    private long expirationCheckIntervalMs = 60000;

    /**
     * Máximo permitido por pedido para productos hechos bajo pedido.
     * Por defecto: 10.
     */
    private int maxMadeToOrderQuantityPerOrder = 10;
}
