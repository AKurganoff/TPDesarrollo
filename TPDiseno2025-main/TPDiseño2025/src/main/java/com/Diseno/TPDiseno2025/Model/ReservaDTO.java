package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {

   
    @Positive
    private Integer idReserva;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "fechaInicio debe tener formato yyyy-MM-dd")
    @Size(max = 10)
    private String fechaInicio;

    @NotNull
    @Positive(message = "cantNoches debe ser > 0")
    private Integer cantNoches;

   
    @Positive
    private Integer idHuesped;

    @NotNull
    @Positive(message = "cantHuesped debe ser > 0")
    private Integer cantHuesped;

    @NotNull
    private Boolean descuento;

   
    @Size(max = 10)
    private String estado;
}
