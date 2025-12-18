package com.Diseno.TPDiseno2025.Model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaPreviewDTO {

    private Double precioEstadia;
    private List<ConsumibleDTO> consumibles;
    private Double total;
    private String tipoFactura;
}
