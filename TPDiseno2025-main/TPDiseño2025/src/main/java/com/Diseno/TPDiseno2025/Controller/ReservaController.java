package com.Diseno.TPDiseno2025.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Model.CeldaCalendarioDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Service.DetalleReservaService;
import com.Diseno.TPDiseno2025.Service.HabitacionService;
import com.Diseno.TPDiseno2025.Service.HuespedService;
import com.Diseno.TPDiseno2025.Service.ReservaService;

import lombok.Data;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reservas") 
public class ReservaController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(HuespedController.class);

    private final ReservaService reservaService;
    private final HuespedService huespedService;
    private final HabitacionService habitacionService;
    private final DetalleReservaService detalleReservaService;

    public ReservaController(final ReservaService reservaService, final HuespedService huespedService, final HabitacionService habitacionService, final DetalleReservaService detalleReservaService){
        this.reservaService = reservaService;
        this.huespedService = huespedService;
        this.habitacionService = habitacionService;
        this.detalleReservaService = detalleReservaService;
    }

    // CREAR RESERVA 
    @PostMapping("/crear")
    public ResponseEntity<String> crearReserva(@RequestBody SolicitudReserva request) {
        try {
            reservaService.crearReserva(request.getReserva(), request.getHuesped(), request.getHabitacion());
            return ResponseEntity.ok("Reserva creada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear reserva: " + e.getMessage());
        }
    }

    // DISPONIBILIDAD 
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<CeldaCalendarioDTO>> obtenerDisponibilidad(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String inicioStr,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String finStr,
            @RequestParam(value = "idTipo", required = false) Integer idTipo) {

        return ResponseEntity.ok( reservaService.obtenerMatrizDisponibilidad(inicioStr, finStr, idTipo));
    }

}

// Clase auxiliar 
@Data
class SolicitudReserva {
    private ReservaDTO reserva;
    private HuespedDTO huesped;
    private HabitacionDTO habitacion;
}