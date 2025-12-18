package com.Diseno.TPDiseno2025.Model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class FacturaResponseDTO {

    private Integer idFactura;
    private Integer nroFactura;
    private LocalDate fecha;
    private String tipoFactura;
    private Double precioFinal;
    private String razonSocialResponsable;
}
