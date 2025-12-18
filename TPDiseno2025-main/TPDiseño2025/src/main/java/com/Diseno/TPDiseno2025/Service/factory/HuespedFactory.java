package com.Diseno.TPDiseno2025.Service.factory;

import org.springframework.stereotype.Component;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;

@Component
public class HuespedFactory {
    public Huesped crearDesdeDTO(HuespedDTO dto, Direccion direccion) {
        Huesped h = new Huesped();
        h.setNombre(dto.getNombre());
        h.setApellido(dto.getApellido());
        h.setTipoDni(dto.getTipoDni());
        h.setDni(dto.getDni());
        h.setEmail(dto.getEmail());
        h.setFechaNacimiento(dto.getFechaNacimiento());
        h.setEdad(dto.getEdad());
        h.setOcupacion(dto.getOcupacion());
        h.setPosIva(dto.getPosIva());
        h.setDireccion(direccion);
        h.setActivo(true);
        return h;
    }
}
