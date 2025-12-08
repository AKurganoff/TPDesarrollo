package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Juridica;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.JuridicaDTO;
import com.Diseno.TPDiseno2025.Repository.JuridicaRepository;


@Service
public class JuridicaServiceImp implements JuridicaService{
    
    @Autowired
    private JuridicaRepository juridicaRepository;
    
    @Autowired
    private DireccionService direccionService;

    @Override
    public void crearJuridica(Integer cuit, String razonSocial, Direccion dir) {
        Juridica nuevaJuridica = new Juridica();
        nuevaJuridica.setCuit(cuit);
        nuevaJuridica.setRazonSocial(razonSocial);
        nuevaJuridica.setDireccion(dir);

        juridicaRepository.save(nuevaJuridica);
        
    }

    @Override
    public JuridicaDTO crearJuridicaDTO(Integer cuit, String razonSocial, DireccionDTO dir) {
        JuridicaDTO nuevaJuridicaDTO = new JuridicaDTO();
        nuevaJuridicaDTO.setCuit(cuit);
        nuevaJuridicaDTO.setRazonSocial(razonSocial);
        nuevaJuridicaDTO.setDireccion(dir);

        return nuevaJuridicaDTO;
    }

    @Override
    public Juridica findByCuit(Integer cuit) {
        return juridicaRepository.findById(cuit).orElse(null);
    }

    @Override
    public Integer getCuit(Juridica j) {
        return j.getCuit();
    }

    @Override
    public boolean existsByCuit(String cuit) {
        Integer cuitInt = Integer.parseInt(cuit);
        return juridicaRepository.existsById(cuitInt);
    }

    @Override
    public List<Juridica> obtenerTodas() {    
        return juridicaRepository.findAll();
    }

    @Override
    public Juridica mapToEntJuridica(JuridicaDTO jdTO, Juridica j) {

        j.setCuit(jdTO.getCuit());
        j.setRazonSocial(jdTO.getRazonSocial());
        j.setDireccion(direccionService.mapToEntDireccion(jdTO.getDireccion()));
        return j;
    }

    @Override
    public JuridicaDTO mapToDTOJuridica(Juridica j, JuridicaDTO jdTO) {
        jdTO.setCuit(j.getCuit());
        jdTO.setRazonSocial(j.getRazonSocial());
        jdTO.setDireccion(direccionService.mapToDTODireccion(j.getDireccion(), jdTO.getDireccion()));
        return jdTO;
    }


}
