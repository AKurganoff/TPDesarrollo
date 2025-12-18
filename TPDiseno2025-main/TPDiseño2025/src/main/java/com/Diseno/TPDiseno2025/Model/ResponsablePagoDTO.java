package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsablePagoDTO {

    private Integer idResponsablePago; // si ya existe (paso siguiente del flujo)
    private Integer dniHuesped;        // si el responsable es un huésped

    @Valid
    private JuridicaDTO juridica;

    private String razonSocial;
}

