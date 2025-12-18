package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsablePagoDTO {

    private Integer idResponsablePago;

    @Valid
    private JuridicaDTO juridica;

    @NotBlank
    private String razonSocial;
}
