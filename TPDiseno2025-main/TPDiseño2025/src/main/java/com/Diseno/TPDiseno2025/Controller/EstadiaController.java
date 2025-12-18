package com.Diseno.TPDiseno2025.Controller;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Model.EstadiaDTO;
import com.Diseno.TPDiseno2025.Model.OcupacionRequest;
import com.Diseno.TPDiseno2025.Service.EstadiaService;

@RestController
@RequestMapping("/estadias")
@CrossOrigin(origins = "http://localhost:3000") // Permite conexión desde React
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;


    @GetMapping("/estadia")
    public ResponseEntity<EstadiaDTO> obtenerEstadiaFacturable(
            @RequestParam Integer numeroHabitacion,
            @RequestParam LocalTime horaCheckout
    ) {
        Estadia estadia = estadiaService.obtenerEstadiaFacturable(numeroHabitacion, horaCheckout);

        EstadiaDTO dto = new EstadiaDTO();
        dto.setIdEstadia(estadia.getIdEstadia());
        dto.setPrecio(estadia.getPrecio());
        dto.setHoraCheckin(estadia.getHoraCheckIn());
        dto.setHoraCheckout(estadia.getHoraCheckOut());
        dto.setIdReserva(estadia.getReserva().getIdReserva());

        return ResponseEntity.ok(dto);
    }
    // ENDPOINT: OCUPAR HABITACIÓN (CU15)
    // Recibe: Habitaciones, Huéspedes, Fechas y opcionalmente ID Reserva
    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@RequestBody OcupacionRequest request) {
        try {
            estadiaService.ocuparHabitacion(
                request.getHabitaciones(),
                request.getHuespedes(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getIdReservaPrevia()
            );
            return ResponseEntity.ok("La habitación ha sido ocupada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al ocupar habitación: " + e.getMessage());
        }
    }
}