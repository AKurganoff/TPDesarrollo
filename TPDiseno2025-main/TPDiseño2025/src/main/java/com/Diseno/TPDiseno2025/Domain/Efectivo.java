package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Efectivo extends MedioPago{
    @JoinColumn(name = "id_efectivo", nullable = false)
    private Integer idEfectivo;
}
