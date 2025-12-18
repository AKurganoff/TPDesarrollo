package com.Diseno.TPDiseno2025.Model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaRequestDTO {

    @NotNull
    private Integer idEstadia;

    @NotNull
    @Valid
    private ResponsablePagoDTO responsablePago;

    @NotNull
    private Boolean incluirEstadia;

    @NotNull
    private List<Integer> consumiblesSeleccionados;
}
