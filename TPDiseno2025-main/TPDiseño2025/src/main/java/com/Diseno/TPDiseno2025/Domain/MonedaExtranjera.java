package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MonedaExtranjera extends MedioPago{
    @Column(name = "tipo_moneda", nullable = false)
    private String tipoMoneda;

    @Column(name = "valor_cambio", nullable = false)
    private Double valorCambio;
}
