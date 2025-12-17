package com.Diseno.TPDiseno2025.Model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ConfirmarFacturaRequestDTO {
    @NotNull
    private Integer idEstadia;

    @NotNull
    private Integer idResponsablePago;

    @NotEmpty
    private List<Integer> idsConsumibles;
}
