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
public class Habitacion {
    @Id
    @Column(name = "id_habitacion", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHabitacion;

    @OneToOne
    @JoinColumn(name="idtipo", nullable=false)
    private TipoHabitacion idTipo;

    @Column (name = "noches_descuento", nullable=false)
    private Integer nochesDescuento;

    @Column (name = "estado", nullable=false)
    private String estado;
}
