package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.EnumType;
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

    private String tipo; // FISICA / JURIDICA

    @Column(unique = true)
    private String cuit;   // para jurídica

    @Column(unique = true)
    private String dni;    // para física

    private LocalDate fechaNacimiento; // validar mayoría de edad

    @Column(name = "condicionIVA", nullable = false)
    private String condicionIVA;
}