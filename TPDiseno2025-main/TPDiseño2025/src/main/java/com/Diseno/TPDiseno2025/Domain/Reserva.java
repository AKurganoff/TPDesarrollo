package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reserva")
@Getter
@Setter
public class Reserva {
    @Id
    @Column(name = "id_reserva", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @OneToOne
    @JoinColumn(name = "dni_huesped", referencedColumnName = "dni_huesped", nullable = false)
    private Huesped huesped;

    @Column(name = "cant_huesped", nullable = false)
    private Integer cantHuesped;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "cant_noches", nullable = false)
    private Integer cantNoches;

    @Column(name = "descuento", nullable = false)
    private Boolean descuento;

    @Column(name = "estado", nullable=false)
    private String estado; // Aceptado, Rechazado, En Proceso, Finalizado, Pagado, Cancelado
}
