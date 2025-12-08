package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pago {
    @Id
    @Column(name = "id_pago", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name="id_mediopago", nullable=false)
    private MedioPago medioPago;

    @OneToOne
    @JoinColumn(name="id_factura", referencedColumnName="id_factura", nullable=false)
    private Factura factura;

    @OneToOne
    @JoinColumn(name="id_responsablepago", nullable=false)
    private ResponsablePago responsablePago;

    @Column(name = "monto",nullable=false)
    private Double monto;

    @Column(name = "fecha_pago", nullable=false)
    private LocalDate fechaPago;
}
