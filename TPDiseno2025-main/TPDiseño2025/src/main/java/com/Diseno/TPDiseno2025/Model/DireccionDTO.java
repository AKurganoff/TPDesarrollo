package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DireccionDTO {
    
    @NotBlank
    @Size(max = 100)
    private String calle;

    @NotNull
    private Integer numero;

    @Size(max = 10)
    private String departamento;

    @Size(max = 10)
    private Integer piso;

    @NotNull
    private Integer codPostal;

    @NotBlank
    @Size(max = 50)
    private String localidad;

    @NotBlank
    @Size(max = 50)
    private String ciudad;

    @NotBlank
    @Size(max = 50)
    private String provincia;

    @NotBlank
    @Size(max = 50)
    private String pais;



}
