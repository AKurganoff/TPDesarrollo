package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;

public interface DireccionService {
    
    void crearDireccion(DireccionId id, DireccionDTO direccionDTO);
    Direccion obtenerDireccionbyId(DireccionId id);
    List<Direccion> obtenerDireccionesPorLocalidad(String localidad);
    Boolean direccionExists(String calle, Integer numero, String departamento, Integer piso, Integer codPostal);
    Direccion mapToEntDireccion(DireccionDTO dDTO);
    DireccionDTO mapToDTODireccion(Direccion d, DireccionDTO dDTO);
    DireccionId crearDireccionId(String calle, Integer numero, String departamento, Integer piso, Integer codPostal);

}
