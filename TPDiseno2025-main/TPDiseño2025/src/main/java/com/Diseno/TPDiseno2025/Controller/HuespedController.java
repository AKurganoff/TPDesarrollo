package com.Diseno.TPDiseno2025.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Service.HuespedService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/huespedes") 
public class HuespedController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(HuespedController.class);

    private final HuespedService huespedservice;

    public HuespedController(final HuespedService huespedservice){
        this.huespedservice = huespedservice;
    }

    @GetMapping("/") 
    public ResponseEntity<List<HuespedDTO>> getAllHuespedes() {
        List<HuespedDTO> huespedes = huespedservice.obtenerTodosDTO();
        return ResponseEntity.ok(huespedes);
    }
    
    @GetMapping("/{tipodni}/{dni}")
    public ResponseEntity<HuespedDTO> getHuespedByTipoDniAndDni(
            @PathVariable @NotNull final String tipodni, 
            @PathVariable @NotNull final Integer dni) { // Cambiado a @PathVariable porque están en la URL
        return ResponseEntity.ok(huespedservice.buscarHuespedDTOByTipoDniAndDni(tipodni, dni));
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<HuespedDTO>> getHuespedesByNombre(@RequestParam @NotBlank @NotNull final String nombre) {
        List<HuespedDTO> huespedes = huespedservice.getByNombreDTO(nombre);
        return ResponseEntity.ok(huespedes);
    }

    @GetMapping("/apellido")
    public ResponseEntity<List<HuespedDTO>> getHuespedByApellido(@RequestParam @NotBlank final String apellido){
        return ResponseEntity.ok(huespedservice.getByApellidoDTO(apellido));
    }

    @GetMapping("/getByDni") // Ejemplo: GET /huesped/getByDni?dni=12345
    public ResponseEntity<?> getHuespedByDni(@RequestParam("dni") Integer dni) {
    try {
        // Asumiendo que tienes un método así en tu servicio. 
        // Si no, usa repository.findByDni(dni)
        HuespedDTO huesped = huespedservice.buscarHuespedDTOByTipoDniAndDni("DNI", dni); 
        return ResponseEntity.ok(huesped);
    } catch (Exception e) {
        // Si no lo encuentra, devolvemos 404
        return ResponseEntity.notFound().build();
    }
}
    
    @GetMapping("/buscar")
    public ResponseEntity<HuespedDTO> buscarCompleto(
        @RequestParam String nombre,
        @RequestParam String apellido,
        @RequestParam Integer dni,
        @RequestParam String tipodni
    ) {
        return ResponseEntity.ok(
            huespedservice.buscarHuespedByNombreAndapellidoAndTipoDniAndDni(nombre, apellido, tipodni, dni)
        );
    }

    @GetMapping("/buscarPorTipo/{tipodni}")
    public ResponseEntity<List<HuespedDTO>> getHuespedByTipoDni(@PathVariable @NotNull final String tipodni){
        return ResponseEntity.ok(huespedservice.buscarHuespedDTOPorTipoDni(tipodni));
    }

    @PostMapping("/crear")
    public ResponseEntity<Integer> createHuesped(@RequestBody @Valid HuespedDTO huespedDTO) {
        Integer createdId = huespedservice.crearHuespedDTO(huespedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    
    @PutMapping("/{tipodni}/{dni}")
    public ResponseEntity<Integer> updateHuesped(
            @PathVariable(name = "dni") final Integer dni, 
            @PathVariable(name = "tipodni") final String tipodni, 
            @RequestBody @Valid final HuespedDTO huespedDTO) { // @RequestBody para los datos nuevos
        
        huespedservice.modificarHuespedDTO(tipodni, dni, huespedDTO);
        return ResponseEntity.ok(dni);
    }

    @DeleteMapping("/{tipodni}/{dni}")
    public ResponseEntity<Void> deleteHuesped(
            @PathVariable(name = "dni") final Integer dni, 
            @PathVariable(name = "tipodni") final String tipodni){
        
        huespedservice.eliminarHuespedByTipoDniAndDni(tipodni, dni);
        return ResponseEntity.noContent().build();
    }
}