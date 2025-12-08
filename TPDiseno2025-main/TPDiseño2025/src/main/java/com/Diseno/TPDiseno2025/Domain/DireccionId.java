package com.Diseno.TPDiseno2025.Domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class DireccionId implements Serializable {

    public DireccionId(){}

    @Column(name = "calle")
    private String calle;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "departamento")
    private String departamento;
    @Column(name = "piso")
    private Integer piso;
    @Column(name = "cod_postal")
    private Integer codPostal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DireccionId that = (DireccionId) o;
        return Objects.equals(calle, that.calle) &&
               Objects.equals(numero, that.numero) &&
               Objects.equals(departamento, that.departamento) &&
               Objects.equals(piso, that.piso) &&
               Objects.equals(codPostal, that.codPostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero, departamento, piso, codPostal);
    }

    public DireccionId(String calle, Integer numero, String dept, Integer piso, Integer cod_p){
        this.calle=calle;
        this.numero = numero;
        this.departamento = dept;
        this.piso = piso;
        this.codPostal = cod_p;
    }
}
