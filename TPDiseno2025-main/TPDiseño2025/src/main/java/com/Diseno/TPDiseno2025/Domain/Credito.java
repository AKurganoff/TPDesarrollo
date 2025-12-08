package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Credito extends MedioPago {
    @Column(nullable=false, updatable=false)
    private Integer nroTarjeta;

    @Column(nullable=false)
    private Integer cuotas;

    @Column(nullable=false)
    private String nombreTitular;

    @Column(nullable=false)
    private String apellidoTitular;
}
