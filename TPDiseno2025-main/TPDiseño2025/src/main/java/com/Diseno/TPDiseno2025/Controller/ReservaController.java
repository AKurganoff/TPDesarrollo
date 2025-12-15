package com.Diseno.TPDiseno2025.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import com.Diseno.TPDiseno2025.Service.ReservaFacade;
import com.Diseno.TPDiseno2025.Service.ReservaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService reservaService;
    private final ReservaFacade reservaFacade;

    public ReservaController(final ReservaService reservaService, final ReservaFacade reservaFacade) {
        this.reservaService = reservaService;
        this.reservaFacade = reservaFacade;
    }

    // CREAR RESERVA (CU04)
 @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@Valid @RequestBody CrearReservaRequest request) {
        Integer idReserva = reservaFacade.procesarCrearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(idReserva);
    }

    // cu5
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<CeldaCalendarioDTO>> obtenerDisponibilidad(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String inicioStr,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String finStr,
            @RequestParam(value = "idTipo", required = false) Integer idTipo) {

        return ResponseEntity.ok(reservaService.obtenerMatrizDisponibilidad(inicioStr, finStr, idTipo));
    }
}



// Clase auxiliar
@Data
class SolicitudReserva {

    @NotNull
    @Valid
    private ReservaDTO reserva;

    @NotNull
    @Valid
    private HuespedDTO huesped;

    @NotNull
    @Valid
    private HabitacionDTO habitacion;
}
