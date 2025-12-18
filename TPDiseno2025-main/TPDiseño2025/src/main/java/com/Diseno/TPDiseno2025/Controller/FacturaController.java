package com.Diseno.TPDiseno2025.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Factura;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.ResponsablePago;
import com.Diseno.TPDiseno2025.Model.*;
import com.Diseno.TPDiseno2025.Service.FacturaService;

@RestController
@RequestMapping("/facturacion")
@CrossOrigin(origins = "http://localhost:3000")
public class FacturaController {
    @Autowired 
    private FacturaService facturaService;
    @GetMapping("/checkout")
    public ResponseEntity<List<HuespedDTO>> iniciarCheckout(
            @RequestParam Integer idHabitacion,
            @RequestParam LocalTime horaCheckout) {

        List<Huesped> ocupantes =
                facturaService.iniciarCheckout(idHabitacion, horaCheckout);

        List<HuespedDTO> response = ocupantes.stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/responsable")
    public ResponseEntity<ResponsablePagoResponseDTO> definirResponsable(
            @RequestParam Integer idEstadia,
            @RequestBody ResponsablePagoDTO request) {

        ResponsablePago responsable =
                facturaService.definirResponsablePago(idEstadia, request);

        ResponsablePagoResponseDTO response = new ResponsablePagoResponseDTO(
                responsable.getIdResponsablePago(),
                responsable.getRazonSocial()
        );

        return ResponseEntity.ok(response);
    }

    /* =========================================================
       PASO 6
       Preview de facturación
       ========================================================= */
    @GetMapping("/preview")
    public ResponseEntity<FacturaPreviewDTO> obtenerPreview(
            @RequestParam Integer idEstadia,
            @RequestParam Integer idResponsablePago) {

        return ResponseEntity.ok(
                facturaService.obtenerPreview(
                        idEstadia,
                        idResponsablePago));
    }
    @PostMapping("/generar")
    public ResponseEntity<Factura> generarFactura(
            @RequestBody FacturaRequestDTO request) {

        return ResponseEntity.ok(
                facturaService.generarFactura(request));
    }

    /* =========================================================
       MAPPER LOCAL
       ========================================================= */
    private HuespedDTO toDTO(Huesped h) {

        HuespedDTO dto = new HuespedDTO();
        dto.setNombre(h.getNombre());
        dto.setApellido(h.getApellido());
        dto.setTipoDni(h.getTipoDni());
        dto.setDni(h.getDni());
        dto.setEmail(h.getEmail());
        dto.setFechaNacimiento(h.getFechaNacimiento());
        dto.setEdad(
                Period.between(
                        h.getFechaNacimiento(),
                        LocalDate.now()).getYears());
        dto.setOcupacion(h.getOcupacion());
        dto.setPosIva(h.getPosIva());
        dto.setNacionalidad(h.getNacionalidad());
        dto.setTelefono(h.getTelefono().getTelefono());
        dto.setDireccion(toDireccionDTO(h.getDireccion()));

        return dto;
    }

    private DireccionDTO toDireccionDTO(Direccion d) {

        DireccionDTO dto = new DireccionDTO();
        dto.setCalle(d.getId().getCalle());
        dto.setNumero(d.getId().getNumero());
        dto.setDepartamento(d.getId().getDepartamento());
        dto.setPiso(d.getId().getPiso());
        dto.setCodPostal(d.getId().getCodPostal());
        dto.setLocalidad(d.getLocalidad());
        dto.setProvincia(d.getProvincia());
        dto.setPais(d.getPais());

        return dto;
    }
}
