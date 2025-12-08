package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "detalle_reserva", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_habitacion", "id_reserva"})
})
public class DetalleReserva {
    
    @Id
    @Column(name = "id_detalle", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @OneToOne
    @JoinColumn(name = "id_habitacion", referencedColumnName="id_habitacion", nullable = false)
    @NotNull
    private Habitacion habitacion;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "cantidad_noches", nullable = false)
    private Integer cantidadNoches;

    @OneToOne
    @JoinColumn(name = "id_reserva", referencedColumnName="id_reserva", nullable = false)
    @NotNull
    private Reserva reserva;
    
}
