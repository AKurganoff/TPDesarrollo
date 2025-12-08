package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Consumible {
    @ManyToOne
    @JoinColumn(name="id_estadia", referencedColumnName="id_estadia", nullable=false)
    private Estadia idEstadia;

    @Column(nullable=false)
    private Double precio;

    @Id
    @Column(name = "id_consumible", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsumible;

    @Column(nullable=false)
    private String nombre;
}
