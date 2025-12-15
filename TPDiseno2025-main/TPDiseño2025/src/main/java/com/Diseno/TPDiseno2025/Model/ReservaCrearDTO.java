package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaCrearDTO {

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "fechaInicio debe tener formato yyyy-MM-dd")
    @Size(max = 10)
    private String fechaInicio;

    @NotNull
    @Positive
    private Integer cantNoches;

    @NotNull
    @Positive
    private Integer cantHuesped;

    @NotNull
    private Boolean descuento;

    // opcional: si no viene, backend pone "Confirmada"
    @Size(max = 10)
    private String estado;
}
