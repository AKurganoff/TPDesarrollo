package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Direccion {
    
    @EmbeddedId
    private DireccionId id;

    @Column(name = "localidad", nullable = false)
    private String localidad;

    @Column(name = "provincia", nullable = false)
    private String provincia;

    @Column(name = "pais", nullable = false)
    private String pais;

}