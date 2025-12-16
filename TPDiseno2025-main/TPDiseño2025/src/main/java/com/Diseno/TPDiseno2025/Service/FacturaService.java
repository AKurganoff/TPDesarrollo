package com.Diseno.TPDiseno2025.Service;

import java.time.LocalTime;
import java.util.List;


import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewDTO;

public interface FacturaService {
    Estadia obtenerEstadiaFacturable(Integer numeroHabitacion, LocalTime horaCheckout);

    List<DetalleEstadia> obtenerOcupantes(Integer idEstadia);

    FacturaPreviewDTO previsualizarFactura(Integer idEstadia, Integer idResponsablePago);

    Factura confirmarFactura(Integer idEstadia, Integer idResponsablePago, List<Integer> idsConsumibles);
}
