package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tipohabitacion")
@Getter
@Setter
public class TipoHabitacion {
    @Id
    @Column(name = "idtipo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipo;

    @Column(name = "nombretipo", nullable = false)
    private String nombreTipo;

    @Column(name = "precio_x_noche", nullable = false)
    private Double precioNoche;

    @Column(name = "cantidaddisponible", nullable = false)
    private Integer cantidadDisponible;

    public TipoHabitacion(){}

    public TipoHabitacion(Integer idTipo, String nombreTipo){
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
    }
}