package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Juridica {
    @Id
    @Column(name = "juridica_cuit", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cuit;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "calle", referencedColumnName = "calle"),
        @JoinColumn(name = "numero", referencedColumnName = "numero"),
        @JoinColumn(name = "departamento", referencedColumnName = "departamento"),
        @JoinColumn(name = "piso", referencedColumnName = "piso"),
        @JoinColumn(name = "cod_postal", referencedColumnName = "cod_postal")
    })
    private Direccion direccion;
}
