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
@Table(name = "detalle_estadia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_estadia", "dni_huesped"})
})
public class DetalleEstadia {
    @Id
    @Column(name = "id_detalleestadia")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idDetalleEstadia;

    @OneToOne
    @JoinColumn(name = "id_estadia", referencedColumnName = "id_estadia", nullable = false)
    private Estadia estadia;
    
    @OneToOne
    @JoinColumn(name = "dni_huesped", referencedColumnName = "dni_huesped", nullable = false)
    private Huesped huesped;

}
