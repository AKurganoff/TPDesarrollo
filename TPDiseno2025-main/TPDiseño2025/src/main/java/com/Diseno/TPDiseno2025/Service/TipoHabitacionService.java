package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;
import com.Diseno.TPDiseno2025.Model.TipoHabitacionDTO;

public interface TipoHabitacionService {
    
    Double getPrecioByTipo(Integer idTipo);
    TipoHabitacion getTipoByIdTipo(Integer idTipo);
    TipoHabitacionDTO mapToDTOTipoHabitacion(TipoHabitacion tipoH);
    TipoHabitacion mapToEntTipoHabitacion(TipoHabitacionDTO tipoDTO);


}
