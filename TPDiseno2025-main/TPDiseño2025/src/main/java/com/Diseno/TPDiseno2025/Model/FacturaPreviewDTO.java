package com.Diseno.TPDiseno2025.Model;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaPreviewDTO {
    private ResponsablePagoDTO responsable;
    private Estadia estadia;
    private List<ConsumibleDTO> consumos;
    private Double subtotal;
    private Double iva;
    private Double total;
    private String tipoFactura;
    public FacturaPreviewDTO (ResponsablePagoDTO responsable, Estadia estadia, List<ConsumibleDTO> consumos, 
        Double subtotal, Double iva, Double total, String tipoFactura){
        this.responsable = responsable;
        this.estadia = estadia;
        this.consumos = consumos;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.tipoFactura = tipoFactura;
    }
}
