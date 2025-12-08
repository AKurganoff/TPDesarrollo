package com.Diseno.TPDiseno2025.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Telefono;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Repository.DireccionRepository;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.TelefonoRepository;
import com.Diseno.TPDiseno2025.Util.NotFoundException;

@Service
public class HuespedServiceImp implements HuespedService {

    @Autowired
    private DireccionService direccionService;
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired 
    private TelefonoRepository telefonoRepository;

    @Override
    public void crearHuesped(Huesped h) {
        huespedRepository.save(h);
    }

    @Override
    public Integer crearHuespedDTO(HuespedDTO hDTO){
        DireccionDTO dirDTO = hDTO.getDireccion();
        Direccion dir = new Direccion();
        if(hDTO.getDireccion().getPiso() == null){
            hDTO.getDireccion().setPiso(0);
        }
        if(hDTO.getDireccion().getDepartamento() == null){
            hDTO.getDireccion().setDepartamento("-");
        }
        if(direccionService.direccionExists(dirDTO.getCalle(), dirDTO.getNumero(), dirDTO.getDepartamento(), dirDTO.getPiso(),dirDTO.getCodPostal())){
            dir = direccionRepository.findById(new DireccionId(dirDTO.getCalle(), dirDTO.getNumero(), dirDTO.getDepartamento(), dirDTO.getPiso(), dirDTO.getCodPostal())).get();
        }else{
            DireccionId dirId = direccionService.crearDireccionId(dirDTO.getCalle(), dirDTO.getNumero(), dirDTO.getDepartamento(), dirDTO.getPiso(), dirDTO.getCodPostal());
            direccionService.crearDireccion(dirId, dirDTO);
            dir = direccionRepository.findById(dirId).get();
        }

        //Guardar Huésped 
        Huesped huesped = this.mapToEntity(new Huesped(), hDTO);
        huesped.setDireccion(dir); 
        Huesped huesped1 = huespedRepository.save(huesped);

        String telefonoVieneDelFront = hDTO.getTelefono(); 

        if (telefonoVieneDelFront != null && !telefonoVieneDelFront.trim().isEmpty()) {
            Telefono tel = new Telefono();
            tel.setHuesped(huesped1); 
            tel.setTelefono(telefonoVieneDelFront);
            telefonoRepository.save(tel);
        }

        return huesped1.getDni();
    }

    @Override
    public Huesped buscarHuespedByTipoDniAndDni(String tipodni, Integer dni){
        return huespedRepository.findByTipoDniAndDni(tipodni, dni)
                .orElseThrow(() -> 
                new NotFoundException("Huesped no enconrtrado"));
    }

    @Override
    public HuespedDTO buscarHuespedDTOByTipoDniAndDni(String tipodni, Integer dni){
        Huesped h = huespedRepository.findByTipoDniAndDni(tipodni, dni)
            .orElseThrow(() -> 
                new NotFoundException("Huésped no encontrado")
            );

        return mapToDTO(h, new HuespedDTO());
    }

    @Override
    public void modificarHuesped(String tipoDni, Integer numOriginal, Huesped hActualizado) {
        Huesped existente = buscarHuespedByTipoDniAndDni(tipoDni, numOriginal);

        
        existente.setNombre(hActualizado.getNombre());
        existente.setApellido(hActualizado.getApellido());
        existente.setDireccion(hActualizado.getDireccion());
        

        huespedRepository.save(existente);
    }

    @Override
    public void eliminarHuespedByTipoDniAndDni(String tipoDni, Integer dni) {
        if(huespedRepository.findByTipoDniAndDni(tipoDni, dni) != null){
            HuespedDTO hDTO = new HuespedDTO();
            hDTO = this.mapToDTO(huespedRepository.findByTipoDniAndDni(tipoDni, dni).get(), hDTO);
            huespedRepository.delete(huespedRepository.findByTipoDniAndDni(tipoDni, dni).get());
            telefonoRepository.delete(telefonoRepository.findByTelefono(hDTO.getTelefono()).get());
        } else{
            throw(new NotFoundException("Huesped no encontrado"));
        }
    }

    @Override
    public List<Huesped> obtenerTodos() {
        return huespedRepository.findAll();
    }

    @Override 
    public List<HuespedDTO> obtenerTodosDTO(){
        List<HuespedDTO> huespedes = new ArrayList<>();
        for(Huesped h : this.obtenerTodos()){
            HuespedDTO hDTO = new HuespedDTO();
            hDTO = this.mapToDTO(h, hDTO);
            huespedes.add(hDTO);
        }
        return huespedes;
    }

    private boolean soloLetras(String texto){
            return texto.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    private boolean soloNumeros(String texto){
            return texto.matches("^[0-9]+$");
    }

    @Override
    public void validarDatos(HuespedDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Los campos no han sido completados");
        }
        if(dto.getNombre()==null || dto.getNombre().isBlank()){
            throw new IllegalArgumentException("El nombre no ha sido completado");
        }
        if(!soloLetras(dto.getNombre())){
            throw new IllegalArgumentException("El nombre solo puede contener letras");
        }
        if(dto.getApellido()==null || dto.getApellido().isBlank()){
            throw new IllegalArgumentException("El apellido no ha sido completado");
        }
        if(!soloLetras(dto.getNombre())){
            throw new IllegalArgumentException("El apellido solo puede contener letras");
        }
        if(dto.getDni()==null || dto.getDni()<=0){
            throw new IllegalArgumentException("El documento no ha sido completado o no es valido");
        }
        if(!soloLetras(dto.getPosIva()) && dto.getPosIva()!=null){
            throw new IllegalArgumentException("La posicion solo puede contener letras");
        }

        DireccionDTO direccionDTO = dto.getDireccion();
        
        if(direccionDTO.getCalle()==null || direccionDTO.getCalle().isBlank()){
            throw new IllegalArgumentException("La calle no ha sido completada");
        }
        if(!soloLetras(direccionDTO.getCalle())){
            throw new IllegalArgumentException("La calle solo puede contener letras");
        }
        if(direccionDTO.getNumero()==null){
            throw new IllegalArgumentException("El numero no ha sido completado");
        }
        if(direccionDTO.getNumero()<=0){
            throw new IllegalArgumentException("El numero debe ser un numero positivo");
        }
        if(direccionDTO.getDepartamento()!=null && (!soloLetras(direccionDTO.getDepartamento()) || direccionDTO.getDepartamento().isBlank())){
            throw new IllegalArgumentException("El departamento solo puede contener letras");
        }
        if(direccionDTO.getPiso()!=null && direccionDTO.getPiso()<0){
            throw new IllegalArgumentException("El piso no es valido");
        }
        if(direccionDTO.getCodPostal()!=null && direccionDTO.getCodPostal()<=0){
            throw new IllegalArgumentException("El codigo postal debe ser un numero positivo");
        }
        if(direccionDTO.getLocalidad()==null || direccionDTO.getLocalidad().isBlank()){
            throw new IllegalArgumentException("La localidad no ha sido completada");
        }
        if(direccionDTO.getProvincia()==null || direccionDTO.getProvincia().isBlank()){
            throw new IllegalArgumentException("La provincia no ha sido completada");
        }
        if(direccionDTO.getPais()==null || direccionDTO.getPais().isBlank()){
            throw new IllegalArgumentException("El pais no ha sido completada");
        }
        if(dto.getOcupacion()==null || dto.getOcupacion().isBlank()){
            throw new IllegalArgumentException("La ocupacion no ha sido completada");
        }
        if(!soloLetras(dto.getOcupacion())){
            throw new IllegalArgumentException("La ocupacion solo debe tener letras");
        }
    }

    @Override
    public void DarDeAltaHuesped(HuespedDTO dto){
        validarDatos(dto);
        if (huespedRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El huésped ya existe");
        }
        Telefono tel = new Telefono();
        DireccionDTO direccionDTO = dto.getDireccion();

        DireccionId id = new DireccionId();
        id.setCalle(direccionDTO.getCalle());
        id.setNumero(direccionDTO.getNumero());
        id.setDepartamento(direccionDTO.getDepartamento());
        id.setPiso(direccionDTO.getPiso());
        id.setCodPostal(direccionDTO.getCodPostal());

        Direccion direccion;

        if (direccionService.direccionExists(
                direccionDTO.getCalle(),
                direccionDTO.getNumero(),
                direccionDTO.getDepartamento(),
                direccionDTO.getPiso(),
                direccionDTO.getCodPostal()
        )) {
            direccion = direccionService.obtenerDireccionbyId(id);
        } 
        else {
            direccionService.crearDireccion(id, direccionDTO);
            direccion = direccionService.obtenerDireccionbyId(id);
        }

        


        Huesped huesped = new Huesped();
        huesped.setNombre(dto.getNombre());
        huesped.setApellido(dto.getApellido());
        huesped.setTipoDni(dto.getTipoDni());
        huesped.setDni(dto.getDni());
        huesped.setDireccion(direccion);
        huesped.setEmail(dto.getEmail());
        huesped.setFechaNacimiento(dto.getFechaNacimiento());
        huesped.setEdad(dto.getEdad());
        huesped.setOcupacion(dto.getOcupacion());
        huesped.setPosIva(dto.getPosIva());


        huespedRepository.save(huesped);
        tel.setHuesped(this.mapToEntity(new Huesped(), dto));
        if(dto.getTelefono() != null){
            tel.setTelefono("-");
        } else {
            tel.setTelefono(dto.getTelefono());
        }
        
        telefonoRepository.save(tel);
    }

    @Override
    public HuespedDTO buscarHuespedByNombreAndapellidoAndTipoDniAndDni(String nombre, String apellido, String tipodni, Integer dni){
        Huesped h = huespedRepository.findByNombreAndApellidoAndTipoDniAndDni(nombre, apellido, tipodni, dni)
                    .orElseThrow(() -> new NotFoundException("Huesped no encontrado"));
        return this.mapToDTO(h, new HuespedDTO());
    }

    @Override
    public HuespedDTO mapToDTO(Huesped h, HuespedDTO hDTO){
        hDTO.setDni(h.getDni());
        hDTO.setNombre(h.getNombre());
        hDTO.setApellido(h.getApellido());
        hDTO.setTipoDni(h.getTipoDni());
        hDTO.setDireccion(direccionService.mapToDTODireccion(h.getDireccion(), new DireccionDTO()));
        hDTO.setEmail(h.getEmail());
        hDTO.setFechaNacimiento(h.getFechaNacimiento());
        hDTO.setEdad(h.getEdad());
        hDTO.setOcupacion(h.getOcupacion());
        hDTO.setPosIva(h.getPosIva());
        return hDTO;
    }

    @Override
    public Huesped mapToEntity(Huesped h, HuespedDTO hDTO){
        h.setDni(hDTO.getDni());
        h.setNombre(hDTO.getNombre());
        h.setApellido(hDTO.getApellido());
        h.setTipoDni(hDTO.getTipoDni());
        h.setDireccion(direccionService.mapToEntDireccion(hDTO.getDireccion()));
        h.setEmail(hDTO.getEmail());
        h.setFechaNacimiento(hDTO.getFechaNacimiento());
        h.setEdad(hDTO.getEdad());
        h.setOcupacion(hDTO.getOcupacion());
        h.setPosIva(hDTO.getPosIva());
        return h;
    }

    @Override
    public List<Huesped> getByNombre(String nombre){
        return this.huespedRepository.findByNombreStartingWithIgnoreCase(nombre);
    }
    
    @Override 
    public List<HuespedDTO> getByNombreDTO(String nombre){
        List<HuespedDTO> huespedes = new ArrayList<>();
        for(Huesped h : this.getByNombre(nombre)){
            huespedes.add(this.mapToDTO(h, new HuespedDTO()));
        }
        return huespedes;
    }

    @Override
    public void modificarHuespedDTO(String tipoDni, Integer dni, HuespedDTO hDTO) {
        
        Huesped huespedExistente = huespedRepository.findByTipoDniAndDni(tipoDni, dni).get();

        Direccion nuevaDireccion = direccionService.mapToEntDireccion(hDTO.getDireccion());
        if (!direccionService.direccionExists(
                nuevaDireccion.getId().getCalle(),
                nuevaDireccion.getId().getNumero(),
                nuevaDireccion.getId().getDepartamento(),
                nuevaDireccion.getId().getPiso(),
                nuevaDireccion.getId().getCodPostal()
            )) {
            direccionService.crearDireccion(nuevaDireccion.getId(), hDTO.getDireccion());
            nuevaDireccion = direccionRepository.findById(nuevaDireccion.getId()).get();
        }else{
            nuevaDireccion = direccionRepository.findById(nuevaDireccion.getId()).get();
        }
        
        huespedExistente.setDireccion(nuevaDireccion);
        huespedExistente.setEmail(hDTO.getEmail());
        huespedExistente.setPosIva(hDTO.getPosIva());
        huespedExistente.setNombre(hDTO.getNombre());
        huespedExistente.setApellido(hDTO.getApellido());
        huespedExistente.setOcupacion(hDTO.getOcupacion());
        huespedRepository.save(huespedExistente);

        Telefono tel = new Telefono();
        tel.setHuesped(this.mapToEntity(new Huesped(), hDTO));
        if(hDTO.getTelefono() != null){
            tel.setTelefono("-");
        } else {
            tel.setTelefono(hDTO.getTelefono());
        }
        
        telefonoRepository.save(tel);
    }

    @Override
    public Boolean existeHuesped(Huesped h){
        return huespedRepository.existsByDni(h.getDni());
    }

    @Override
    public Huesped findById(Integer idHuesped){
        return huespedRepository.findById(idHuesped).get();
    }

    @Override
    public List<HuespedDTO> getByApellidoDTO(String apellido){
        List<Huesped> huespedes = huespedRepository.findByApellidoStartingWithIgnoreCase(apellido);

        List<HuespedDTO> hDTOs= new ArrayList<>();

        for(Huesped h : huespedes){
            hDTOs.add(this.mapToDTO(h, new HuespedDTO()));
        }
        
        return hDTOs;
    }

    @Override
    public List<HuespedDTO> buscarHuespedDTOPorTipoDni(String tipoDoc){
        List<Huesped> huespedes = huespedRepository.findByTipoDni(tipoDoc);

        List<HuespedDTO> hDTOs = new ArrayList<>();

        for (Huesped h : huespedes){
            hDTOs.add(this.mapToDTO(h, new HuespedDTO()));
        }
        return hDTOs;
    }
}

