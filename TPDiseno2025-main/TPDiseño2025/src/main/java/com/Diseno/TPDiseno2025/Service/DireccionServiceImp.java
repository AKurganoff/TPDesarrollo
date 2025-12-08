package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Repository.DireccionRepository;

@Service
public class DireccionServiceImp implements DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public void crearDireccion(DireccionId id, DireccionDTO direccionDTO) {
        Direccion direccion = new Direccion();
        direccion.setId(id);
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setPais(direccionDTO.getPais());
        direccionRepository.save(direccion);
    }

    @Override
    public Direccion obtenerDireccionbyId(DireccionId id) {
        return direccionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Direccion> obtenerDireccionesPorLocalidad(String localidad) {
        return direccionRepository.findByLocalidadStartingWithIgnoreCase(localidad);
    }

    @Override
    public Boolean direccionExists(String calle, Integer numero, String departamento, Integer piso, Integer codPostal) {
        DireccionId id = new DireccionId();
        id.setCalle(calle);
        id.setNumero(numero);
        id.setDepartamento(departamento);
        id.setPiso(piso);
        id.setCodPostal(codPostal);
        return direccionRepository.existsById(id);
    }

    @Override
    public Direccion mapToEntDireccion(DireccionDTO dDTO) {
        Direccion direccion = new Direccion();
        DireccionId id = new DireccionId();
        id.setCalle(dDTO.getCalle());
        id.setNumero(dDTO.getNumero());
        id.setDepartamento(dDTO.getDepartamento());
        id.setPiso(dDTO.getPiso());
        id.setCodPostal(dDTO.getCodPostal());
        direccion.setId(id);
        direccion.setLocalidad(dDTO.getLocalidad());
        direccion.setProvincia(dDTO.getProvincia());
        direccion.setPais(dDTO.getPais());
        return direccion;
    }

    @Override
    public DireccionDTO mapToDTODireccion(Direccion d, DireccionDTO dDTO) {
        dDTO.setCalle(d.getId().getCalle());
        dDTO.setNumero(d.getId().getNumero());
        dDTO.setDepartamento(d.getId().getDepartamento());
        dDTO.setPiso(d.getId().getPiso());
        dDTO.setCodPostal(d.getId().getCodPostal());
        dDTO.setLocalidad(d.getLocalidad());
        dDTO.setProvincia(d.getProvincia());
        dDTO.setPais(d.getPais());
        return dDTO;
    }

    @Override
    public DireccionId crearDireccionId(String calle, Integer numero, String departamento, Integer piso, Integer codPostal){
        DireccionId dId = new DireccionId();
        dId.setCalle(calle);
        dId.setNumero(numero);
        dId.setDepartamento(departamento);
        dId.setPiso(piso);
        dId.setCodPostal(codPostal);
        return dId;
    }

}
