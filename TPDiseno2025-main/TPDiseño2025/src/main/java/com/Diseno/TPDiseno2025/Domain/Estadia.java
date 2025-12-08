package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalTime;

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
public class Estadia {
    @Id
    @Column(name = "id_estadia", nullable=false, unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadia;

    @Column(name = "precio", nullable=false)
    private Double precio;

    @Column(name = "hora_checkin", nullable=false)
    private LocalTime horaCheckIn;

    @Column(name = "hora_checkout", nullable=false)
    private LocalTime horaCheckOut;

    @OneToOne
    @JoinColumn(name="id_reserva", referencedColumnName="id_reserva", nullable=false)
    private Reserva reserva;
}
