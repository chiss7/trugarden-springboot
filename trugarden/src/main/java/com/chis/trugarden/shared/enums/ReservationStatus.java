package com.chis.trugarden.shared.enums;

public enum ReservationStatus {
    ACTIVE,     // Reserva vigente, esperando pago
    CONFIRMED,  // Pago exitoso, stock decrementado
    EXPIRED,    // TTL expiró sin pago
    CANCELLED   // Pago cancelado por el usuario o sistema
}
