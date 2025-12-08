package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;

public interface HuespedService {
 
    void crearHuesped(Huesped h);

    Integer crearHuespedDTO(HuespedDTO hDTO);

    Huesped buscarHuespedByTipoDniAndDni(String tipodni, Integer dni);

    HuespedDTO buscarHuespedDTOByTipoDniAndDni(String tipodni, Integer dni);

    void modificarHuesped(String tipoDni, Integer numOriginal, Huesped hActualizado);

    void modificarHuespedDTO(String tipoDni, Integer dni, HuespedDTO hDTO);

    void eliminarHuespedByTipoDniAndDni(String tipoDni, Integer dni);

    List<Huesped> obtenerTodos();

    List<HuespedDTO> obtenerTodosDTO();

    void DarDeAltaHuesped(HuespedDTO dto);

    HuespedDTO buscarHuespedByNombreAndapellidoAndTipoDniAndDni(
        String nombre,
        String apellido,
        String tipoDoc,
        Integer dni
    );

    HuespedDTO mapToDTO(Huesped h, HuespedDTO hDTO);

    Huesped mapToEntity(Huesped h, HuespedDTO hDTO);

    List<Huesped> getByNombre(String nombre);

    List<HuespedDTO> getByNombreDTO(String nombre);

    List<HuespedDTO> getByApellidoDTO(String apellido);

    Boolean existeHuesped(Huesped h);

    Huesped findById(Integer idHuesped);

    void validarDatos(HuespedDTO hdto);

    List<HuespedDTO> buscarHuespedDTOPorTipoDni(String tipoDoc);
}

