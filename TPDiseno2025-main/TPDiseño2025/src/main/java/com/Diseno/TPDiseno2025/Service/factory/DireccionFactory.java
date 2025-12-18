package com.Diseno.TPDiseno2025.Service.factory;

import org.springframework.stereotype.Component;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;

@Component
public class DireccionFactory {

    public Direccion crearDesdeDTO(DireccionDTO dto) {

        DireccionId id = new DireccionId(
            dto.getCalle(),
            dto.getNumero(),
            dto.getDepartamento() != null ? dto.getDepartamento() : "-",
            dto.getPiso() != null ? dto.getPiso() : 0,
            dto.getCodPostal()
        );

        Direccion direccion = new Direccion();
        direccion.setId(id);
        return direccion;
    }
}
