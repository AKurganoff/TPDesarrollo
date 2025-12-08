package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ResponsablePago {
    @Id
    @Column(name = "id_responsablepago", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResponsablePago;

    @OneToOne
    @JoinColumn(name = "juridica_cuit", referencedColumnName = "juridica_cuit", nullable = true)
    private Juridica juridica;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;
}