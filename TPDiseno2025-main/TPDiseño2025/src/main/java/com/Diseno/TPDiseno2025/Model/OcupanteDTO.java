package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OcupanteDTO {
    @NotNull
    private Integer idHuesped;
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
    @NotNull
    @Size(max = 10)
    private String dni;
}
