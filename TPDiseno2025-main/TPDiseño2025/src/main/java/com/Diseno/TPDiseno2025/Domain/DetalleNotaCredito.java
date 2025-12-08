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
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "detalle_notacredito", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_notacredito", "id_factura"})
})
public class DetalleNotaCredito {
    @Id
    @Column(name = "id_detallenota")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idDetalleNota;

    @OneToOne
    @JoinColumn(name = "id_notacredito", referencedColumnName = "id_notacredito", nullable = false)
    private NotaCredito notaCredito;

    @OneToOne
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura", nullable = false)
    private Factura factura;

    @Column(name = "monto_aplicado", nullable = false)
    private Double montoAplicado;
}