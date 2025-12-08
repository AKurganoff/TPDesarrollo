package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cheque extends MedioPago{
    @Column(nullable=false, updatable=false)
    private Integer nroCheque;

    @Column(nullable=false)
    private String bancoEmisor;

    @Column(nullable=false)
    private String titularCheque;
}