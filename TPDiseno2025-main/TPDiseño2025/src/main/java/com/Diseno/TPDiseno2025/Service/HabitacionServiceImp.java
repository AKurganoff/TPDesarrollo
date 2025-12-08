package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;


@Service
public class HabitacionServiceImp implements HabitacionService{

    @Autowired
    private DetalleReservaService detalleReservaService;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @Override
    public void crearHabitacion(Integer idHabitacion, Integer idTipo, Integer nochesDesc, String estado){
        Habitacion nuevaHab = new Habitacion();
        nuevaHab.setIdHabitacion(idHabitacion);
        nuevaHab.setIdTipo(tipoHabitacionService.getTipoByIdTipo(idTipo));
        nuevaHab.setNochesDescuento(nochesDesc);
        nuevaHab.setEstado(estado);

        habitacionRepository.save(nuevaHab);
    }

    @Override
    public HabitacionDTO mapToDTOHabitacion(Habitacion h){
        HabitacionDTO hDTO = new HabitacionDTO();
        hDTO.setIdHabitacion(h.getIdHabitacion());
        hDTO.setIdTipo(h.getIdTipo().getIdTipo());
        hDTO.setNochesDescuento(h.getNochesDescuento());
        hDTO.setEstado(h.getEstado());
        return hDTO;
    }

    @Override
    public Habitacion mapToEntHabitacion(HabitacionDTO hDTO){
        Habitacion h = new Habitacion();
        h.setIdHabitacion(hDTO.getIdHabitacion());
        h.setIdTipo(tipoHabitacionService.getTipoByIdTipo(hDTO.getIdTipo()));
        h.setNochesDescuento(hDTO.getNochesDescuento());
        h.setEstado(hDTO.getEstado());

        return h;
    }

    @Override
    public List<Habitacion> obtenerTodas(){
        return habitacionRepository.findAll();
    }
    
    @Override
    public List<Habitacion> obtenerTodasPorTipo(Integer idTipo){
        if(idTipo == null){
            return this.obtenerTodas();
        }
        else return habitacionRepository.findByIdTipo(tipoHabitacionService.getTipoByIdTipo(idTipo));
    }

    @Override
    public List<String> mostrarestadoHabitacionesByFecha(String fechaDesde, String fechaHasta){
        List<String> estadosHabitaciones = new ArrayList<>();
        for (Habitacion h : this.obtenerTodas()) {
            estadosHabitaciones.add(h.getEstado());
        }
        return estadosHabitaciones;
    }

    @Override
    public Boolean verificarEstadoHabitacion(Integer idHabitacion){
        if(habitacionRepository.existsById(idHabitacion)){
            return (habitacionRepository.findById(idHabitacion).get().getEstado().equalsIgnoreCase("disponible"));
        }else{
            return false;
        }
    }

    @Override 
    public void reservarHabitacion(Integer idHabitacion){
        if(this.verificarEstadoHabitacion(idHabitacion)){
            habitacionRepository.findById(idHabitacion).get().setEstado("reservada");
        }else{
            throw(new RuntimeException("Habitacion no disponible"));
        }
    }

    @Override
    public Habitacion buscarHabitacionByIdHabitacion(Integer idHabitacion){
        return habitacionRepository.findById(idHabitacion).get();
    }

    @Override 
    public HabitacionDTO buscarHabitacionDTOByIdHabitacion(Integer idHabitacion){
        return this.mapToDTOHabitacion(habitacionRepository.findById(idHabitacion).get());
    }

    @Override
    public boolean habitacionDisponibleEnFechas(Integer idHabitacion, LocalDate fechaDesde, LocalDate fechaHasta){
        boolean disponible = true;
        List<DetalleReserva> reservasEnConflicto = detalleReservaService.buscarReservasEnConflicto(fechaDesde, fechaHasta);
        for(DetalleReserva dr : reservasEnConflicto){
            if(dr.getHabitacion().getIdHabitacion().equals(idHabitacion)){
                disponible = false;
            }
        }
        return disponible;
    }
}
