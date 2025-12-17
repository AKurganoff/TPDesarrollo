package com.Diseno.TPDiseno2025.Model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FacturaConfirmacionDTO {
    private Integer idFactura;
    private LocalDate fecha;
    private Double total;
    private String estado;
    private Integer nroFactura;
    private Double precioFinal;
    private String tipoFactura;
}
