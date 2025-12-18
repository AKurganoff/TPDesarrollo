package com.Diseno.TPDiseno2025.Service;

import java.time.LocalTime;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.ResponsablePago;
import com.Diseno.TPDiseno2025.Model.ResponsablePagoDTO;
import com.Diseno.TPDiseno2025.Model.FacturaRequestDTO;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewDTO;

public interface FacturaService {
    public List<Huesped> iniciarCheckout(Integer idHabitacion,LocalTime horaCheckout);
    public ResponsablePago definirResponsablePago(Integer idEstadia,ResponsablePagoDTO responsableDTO);
    public FacturaPreviewDTO obtenerPreview(Integer idEstadia, Integer idResponsablePago);
    public Factura generarFactura(FacturaRequestDTO request);
}
