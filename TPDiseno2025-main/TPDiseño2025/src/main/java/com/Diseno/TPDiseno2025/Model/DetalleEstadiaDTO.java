package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleEstadiaDTO {
    
    @NotNull
    private Integer idDetalleEstadia;

    @NotNull
    private Integer idEstadia;

    @NotNull
    private Integer dniHuesped;

}
