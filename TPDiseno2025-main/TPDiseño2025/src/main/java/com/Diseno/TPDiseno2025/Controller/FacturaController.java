package com.Diseno.TPDiseno2025.Controller;

import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Model.DetalleEstadiaDTO;
import com.Diseno.TPDiseno2025.Model.EstadiaDTO;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewDTO;
import com.Diseno.TPDiseno2025.Model.FacturaConfirmacionDTO;
import com.Diseno.TPDiseno2025.Model.FacturaPreviewRequestDTO;
import com.Diseno.TPDiseno2025.Model.ConfirmarFacturaRequestDTO;
import com.Diseno.TPDiseno2025.Service.FacturaService;

import jakarta.validation.Valid;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/facturacion")
public class FacturaController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(FacturaController.class);
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping("/estadia")
    public ResponseEntity<EstadiaDTO> obtenerEstadiaFacturable(
            @RequestParam Integer numeroHabitacion,
            @RequestParam LocalTime horaCheckout
    ) {
        Estadia estadia = facturaService.obtenerEstadiaFacturable(numeroHabitacion, horaCheckout);

        EstadiaDTO dto = new EstadiaDTO();
        dto.setIdEstadia(estadia.getIdEstadia());
        dto.setPrecio(estadia.getPrecio());
        dto.setHoraCheckin(estadia.getHoraCheckIn());
        dto.setHoraCheckout(estadia.getHoraCheckOut());
        dto.setIdReserva(estadia.getReserva().getIdReserva());
        dto.setIdHabitacion(numeroHabitacion);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/estadia/{idEstadia}/ocupantes")
    public ResponseEntity<List<DetalleEstadiaDTO>> obtenerOcupantes(
            @PathVariable Integer idEstadia
    ) {
        List<DetalleEstadia> detalles = facturaService.obtenerOcupantes(idEstadia);

        List<DetalleEstadiaDTO> dtos = detalles.stream().map(detalle -> {
            DetalleEstadiaDTO dto = new DetalleEstadiaDTO();
            dto.setIdDetalleEstadia(detalle.getIdDetalleEstadia());
            dto.setIdEstadia(detalle.getEstadia().getIdEstadia());
            dto.setDniHuesped(detalle.getHuesped().getDni());
            dto.setNombre(detalle.getHuesped().getNombre());
            dto.setApellido(detalle.getHuesped().getApellido());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/preview")
    public ResponseEntity<FacturaPreviewDTO> previsualizarFactura(
            @RequestBody @Valid FacturaPreviewRequestDTO request
    ) {
        FacturaPreviewDTO preview = facturaService.previsualizarFactura(
                request.getIdEstadia(),
                request.getIdResponsablePago()
        );

        return ResponseEntity.ok(preview);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<FacturaConfirmacionDTO> confirmarFactura(
            @RequestBody @Valid ConfirmarFacturaRequestDTO request
    ) {
        Factura factura = facturaService.confirmarFactura(
                request.getIdEstadia(),
                request.getIdResponsablePago(),
                request.getIdsConsumibles()
        );

        FacturaConfirmacionDTO dto = new FacturaConfirmacionDTO();
        dto.setIdFactura(factura.getIdFactura());
        dto.setNroFactura(factura.getNroFactura());
        dto.setFecha(factura.getFecha());
        dto.setPrecioFinal(factura.getPrecioFinal());
        dto.setTipoFactura(factura.getTipoFactura());

        return ResponseEntity.ok(dto);
    }
}
