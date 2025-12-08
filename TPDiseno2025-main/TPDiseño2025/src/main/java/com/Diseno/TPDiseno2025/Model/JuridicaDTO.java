package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JuridicaDTO {

    @NotNull
    private Integer cuit;

    @NotBlank
    @Size(max = 100)
    private String razonSocial;

    @NotBlank
    private DireccionDTO direccion;

}
