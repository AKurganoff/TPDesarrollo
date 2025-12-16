package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ConsumibleDTO {
    @NotNull
    private Integer idConsumible;
    @NotNull
    private String nombre;
    @NotNull
    private Double precio;
}
