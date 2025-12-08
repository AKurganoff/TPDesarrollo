package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public abstract class MedioPago {
    @Id
    @Column(name = "medio_pago", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedioPago;

    @Column(name = "nombre_mediopago", nullable=false)
    private String nombreMedioPago;
}
