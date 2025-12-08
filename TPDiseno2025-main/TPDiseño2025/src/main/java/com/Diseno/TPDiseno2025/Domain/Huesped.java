package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Huesped {
    
    @Id
    @Column(name = "dni_huesped", nullable = false, updatable = false)
    private Integer dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "tipodni", nullable = false)
    private String tipoDni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "ocupacion", nullable = false)
    private String ocupacion;

    @Column(name ="mail", nullable = true)
    private String email;

    @Column(name = "pos_iva", nullable = true)
    private String posIva;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "calle", referencedColumnName = "calle"),
        @JoinColumn(name = "numero", referencedColumnName = "numero"),
        @JoinColumn(name = "departamento", referencedColumnName = "departamento"),
        @JoinColumn(name = "piso", referencedColumnName = "piso"),
        @JoinColumn(name = "cod_postal", referencedColumnName = "cod_postal")
    })
    private Direccion direccion;

}
