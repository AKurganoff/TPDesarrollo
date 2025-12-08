package com.Diseno.TPDiseno2025.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;
import com.Diseno.TPDiseno2025.Repository.TipoHabitacionRepository;

@RestController
@RequestMapping("/tiposhabitacion")
@CrossOrigin(origins = "http://localhost:3000") 
public class TipoHabitacionController {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @GetMapping
    public List<TipoHabitacion> obtenerTodos() {
        return tipoHabitacionRepository.findAll();
    }
}