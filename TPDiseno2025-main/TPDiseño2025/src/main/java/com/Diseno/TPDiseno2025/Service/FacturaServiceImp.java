package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Repository.DetalleEstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.FacturaRepository;
import com.Diseno.TPDiseno2025.Repository.ResponsablePagoRepository;
import com.Diseno.TPDiseno2025.Domain.Consumible;
import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Domain.ResponsablePago;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewDTO;
import com.Diseno.TPDiseno2025.Repository.ConsumibleRepository;

@Service
public class FacturaServiceImp implements FacturaService{
    
    @Autowired
    private EstadiaRepository estadiaRepository;
    
    @Autowired
    private DetalleEstadiaRepository detalleEstadiaRepository;
    
    @Autowired
    private ConsumibleRepository consumibleRepository;
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;
    
    public Estadia obtenerEstadiaFacturable(Integer numeroHabitacion, LocalTime horaCheckout) {
        validarDatosBusqueda(numeroHabitacion, horaCheckout);

        Estadia estadia = estadiaRepository
                .findByReserva_Habitacion_NumeroAndHoraCheckOut(numeroHabitacion, horaCheckout)
                .orElseThrow(() -> new RuntimeException("No existe una estadía activa para la habitación"));

        if (facturaRepository.existsByEstadia(estadia)) {
            throw new RuntimeException("La estadía ya fue facturada");
        }

        return estadia;
    }

    public List<DetalleEstadia> obtenerOcupantes(Integer idEstadia) {
        Estadia estadia = obtenerEstadiaPorId(idEstadia);
        return detalleEstadiaRepository.findByEstadia(estadia);
    }

    public FacturaPreviewDTO previsualizarFactura(Integer idEstadia, Integer idResponsablePago) {
        Estadia estadia = obtenerEstadiaPorId(idEstadia);
        ResponsablePago responsable = validarYObtenerResponsable(idResponsablePago);

        List<Consumible> consumos = obtenerConsumiblesNoFacturados(estadia);

        double subtotal = calcularSubtotal(estadia, consumos);
        double iva = calcularIVA(subtotal, responsable.getCondicionIVA());
        double total = subtotal + iva;

        String tipoFactura = determinarTipoFactura(responsable.getCondicionIVA());

        return new FacturaPreviewDTO(
                responsable,
                estadia,
                consumos,
                subtotal,
                iva,
                total,
                tipoFactura
        );
    }

    public Factura confirmarFactura(Integer idEstadia,
                                    Integer idResponsablePago,
                                    List<Integer> idsConsumibles) {

        Estadia estadia = obtenerEstadiaPorId(idEstadia);
        ResponsablePago responsable = validarYObtenerResponsable(idResponsablePago);

        if (idsConsumibles == null || idsConsumibles.isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos un consumo a facturar");
        }

        List<Consumible> consumos = consumibleRepository.findAllById(idsConsumibles);

        double subtotal = calcularSubtotal(estadia, consumos);
        double iva = calcularIVA(subtotal, responsable.getCondicionIVA());
        double total = subtotal + iva;

        Factura factura = new Factura();
        factura.setEstadia(estadia);
        factura.setResponsablePago(responsable);
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setTipoFactura(determinarTipoFactura(responsable.getCondicionIVA()));
        factura.setEstado("PENDIENTE_PAGO");
        factura.setFecha(LocalDate.now());

        Factura facturaGuardada = facturaRepository.save(factura);

        marcarConsumiblesComoFacturados(consumos, facturaGuardada);

        return facturaGuardada;
    }

    private void validarDatosBusqueda(Integer numeroHabitacion, LocalTime horaCheckout) {
        if (numeroHabitacion == null || horaCheckout == null) {
            throw new RuntimeException("Datos de búsqueda incompletos");
        }
    }

    private Estadia obtenerEstadiaPorId(Integer idEstadia) {
        return estadiaRepository.findById(idEstadia)
                .orElseThrow(() -> new RuntimeException("La estadía no existe"));
    }

    private ResponsablePago validarYObtenerResponsable(Integer idResponsablePago) {
        ResponsablePago responsable = responsablePagoRepository.findById(idResponsablePago)
                .orElseThrow(() -> new RuntimeException("Responsable de pago inexistente"));

        if (responsable.getFechaNacimiento() != null &&
                responsable.getFechaNacimiento().plusYears(18).isAfter(LocalDate.now())) {
            throw new RuntimeException("El responsable de pago es menor de edad");
        }

        return responsable;
    }

    private List<Consumible> obtenerConsumiblesNoFacturados(Estadia estadia) {
        return consumibleRepository.findByIdEstadiaAndFacturadoFalse(estadia);
    }

    private double calcularSubtotal(Estadia estadia, List<Consumible> consumos) {
        double subtotal = estadia.getPrecio();
        for (Consumible c : consumos) {
            subtotal += c.getPrecio();
        }
        return subtotal;
    }

    private double calcularIVA(double subtotal, String condicionIVA) {
        if ("RESPONSABLE_INSCRIPTO".equals(condicionIVA)) {
            return subtotal * 0.21;
        }
        return 0;
    }

    private String determinarTipoFactura(String condicionIVA) {
        if ("RESPONSABLE_INSCRIPTO".equals(condicionIVA)) {
            return "A";
        }
        return "B";
    }

    private void marcarConsumiblesComoFacturados(List<Consumible> consumos, Factura factura) {
        for (Consumible c : consumos) {
            c.setFacturado(true);
            c.setFactura(factura);
        }
        consumibleRepository.saveAll(consumos);
    }
}
