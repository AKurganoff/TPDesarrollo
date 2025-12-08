package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class NotaCredito {
    @Id
    @Column(name = "id_notacredito", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idNotaCredito;

    @Column(name = "fecha", nullable=false)
    private LocalDate fecha;

    @Column(name = "monto", nullable=false)
    private Double monto;

    @Column(name = "motivo", nullable=false)
    private String motivo;
}
