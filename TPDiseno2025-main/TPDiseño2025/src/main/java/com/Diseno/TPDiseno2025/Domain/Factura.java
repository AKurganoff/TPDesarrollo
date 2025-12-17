package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
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

    @ManyToOne
    @JoinColumn(name = "id_responsablepago", nullable = false)
    private ResponsablePago idResponsablePago;
    
    @Column(name = "precio_final", nullable = false)
    private Double precioFinal;

    @OneToOne
    @JoinColumn(name = "id_estadia", referencedColumnName = "id_estadia", nullable = false)
    private Estadia idEstadia;
    
    @OneToMany
    @JoinColumn(name = "id_factura")
    private List<Consumible> consumos;

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double iva;

    @Column(name = "tipo_factura", nullable = false)
    private String tipoFactura;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "nro_factura", nullable = false)
    private Integer nroFactura;

    @Column(name = "fecha", nullable= false)
    private LocalDate fecha;
}
