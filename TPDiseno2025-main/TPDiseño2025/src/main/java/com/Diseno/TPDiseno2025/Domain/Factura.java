package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

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
public class Factura {
    @Id
    @Column(name = "id_factura", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;

    @OneToOne
    @JoinColumn(name = "id_responsablepago", referencedColumnName = "id_responsablepago", nullable = false)
    private ResponsablePago idResponsablePago;
    
    @Column(name = "precio_final", nullable = false)
    private Double precioFinal;

    @OneToOne
    @JoinColumn(name = "id_estadia", referencedColumnName = "id_estadia", nullable = false)
    private Estadia idEstadia;
    
    @Column(name = "nro_factura", nullable = false)
    private Integer nroFactura;

    @Column(name = "tipo_factura", nullable = false)
    private String tipoFactura;

    @Column(name = "fecha", nullable= false)
    private LocalDate fecha;
}
