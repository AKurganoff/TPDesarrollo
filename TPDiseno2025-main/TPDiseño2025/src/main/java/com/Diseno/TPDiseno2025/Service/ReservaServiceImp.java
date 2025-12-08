package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.CeldaCalendarioDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.ReservaRepository;



@Service
public class ReservaServiceImp implements ReservaService {
    
    @Autowired
    private HuespedService huespedService;
    
    @Autowired
    private HabitacionService habitacionService;
    
    
    @Autowired
    private DetalleReservaService detalleService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Override   
    public void crearReserva(ReservaDTO r, HuespedDTO h, HabitacionDTO habitacion) {
        LocalDate fechaDesde = LocalDate.parse(r.getFechaInicio());
        LocalDate fechaHasta = fechaDesde.plusDays(r.getCantNoches());
        //Verificar disponibilidad de la habitacion

        boolean disponible = habitacionService.habitacionDisponibleEnFechas(habitacion.getIdHabitacion(), fechaDesde, fechaHasta);
        
        if(!disponible){
            throw new IllegalArgumentException("La habitacion no esta disponible en las fechas seleccionadas.");
        }
        
        //Buscamos al huesped
        Huesped huespedExistente = huespedService.buscarHuespedByTipoDniAndDni(h.getTipoDni(), h.getDni());

        //Mapear la reserva
        Reserva nuevaReserva = this.mapToEntity(r);
        
        //Asigno le huesped
        nuevaReserva.setHuesped(huespedExistente);

        //Guarda la Reserva
        reservaRepository.save(nuevaReserva);

        //Guarda el DetalleReserva
        DetalleReserva detalle = new DetalleReserva();
        detalle.setReserva(nuevaReserva);
    
        // Buscamos la habitación real para asegurar consistencia
        Habitacion habReal = habitacionService.buscarHabitacionByIdHabitacion(habitacion.getIdHabitacion());
        detalle.setHabitacion(habReal);
    
        // Precio y Noches
        detalle.setPrecio(habReal.getIdTipo().getPrecioNoche() * r.getCantNoches()); 
        detalle.setCantidadNoches(r.getCantNoches());

        // Guardamos el detalle sin dependencias circulares
        detalleService.guardarDetalle(detalle);
    }

    @Override
    public void modificarReserva(Integer idReserva, Reserva rActualizada) {
        Reserva existente = obtenerReservaPorId(idReserva);
        
        existente.setCantHuesped(rActualizada.getCantHuesped());
        existente.setFechaInicio(rActualizada.getFechaInicio());
        existente.setCantNoches(rActualizada.getCantNoches());
        
        reservaRepository.save(existente);
    }

    @Override
    public void eliminarReserva(Reserva r) {    
        reservaRepository.delete(r);
    }

    @Override
    public Reserva obtenerReservaPorId(Integer id) {    
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reserva> obtenerTodas() {    
        return reservaRepository.findAll();
    }

    @Override
    public Reserva mapToEntity(ReservaDTO r){
        Reserva reserva = new Reserva();
        reserva.setIdReserva(r.getIdReserva());
        //reserva.setHuesped(huespedService.findById(r.getIdHuesped()));
        reserva.setCantHuesped(r.getCantHuesped());
        reserva.setFechaInicio(LocalDate.parse(r.getFechaInicio()));
        reserva.setCantNoches(r.getCantNoches());
        reserva.setDescuento(r.getDescuento());
        reserva.setEstado(r.getEstado());

        return reserva;
    }   
    
    @Override
    public ReservaDTO mapToDTO(Reserva r){
        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdReserva(r.getIdReserva());
        reserva.setIdHuesped(r.getHuesped().getDni());
        reserva.setCantHuesped(r.getCantHuesped());
        reserva.setFechaInicio(r.getFechaInicio().toString());
        reserva.setCantNoches(r.getCantNoches());
        reserva.setDescuento(r.getDescuento());
        reserva.setEstado(r.getEstado());

        return reserva;
    }   

    @Override
    public List<CeldaCalendarioDTO> obtenerMatrizDisponibilidad(String inicioStr, String finStr, Integer idTipo) {
        LocalDate fechaInicio = LocalDate.parse(inicioStr);
        LocalDate fechaFin = LocalDate.parse(finStr);

        //  Traer todo lo necesario
        List<Habitacion> todasLasHabitaciones = habitacionService.obtenerTodasPorTipo(idTipo);
        List<DetalleReserva> ocupaciones = detalleService.buscarReservasEnConflicto(fechaInicio, fechaFin);
        
        List<CeldaCalendarioDTO> grilla = new ArrayList<>();

        //  Recorrer día por día
        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            
            final LocalDate fechaActual = fecha; 

            for (Habitacion habitacion : todasLasHabitaciones) {
                
                String estadoCelda = "LIBRE"; // Verde por defecto
                Integer idReservaEncontrada = null;

                // Buscamos si hay alguna reserva que ocupe esta habitación en esta fecha
                DetalleReserva detalle = ocupaciones.stream()
                .filter(d -> 
                    d.getHabitacion().getIdHabitacion().equals(habitacion.getIdHabitacion()) &&
                    !fechaActual.isBefore(d.getReserva().getFechaInicio()) &&
                    fechaActual.isBefore(d.getReserva().getFechaInicio().plusDays(d.getCantidadNoches()))
                )
                .findFirst()
                .orElse(null);

                if (detalle != null) {
                    String estadoRealBD = detalle.getReserva().getEstado();
                    idReservaEncontrada = detalle.getReserva().getIdReserva();

                    if (estadoRealBD != null && estadoRealBD.trim().equalsIgnoreCase("Confirmada")) {
                        estadoCelda = "RESERVADA"; // Amarillo
                    } else {
                        estadoCelda = "OCUPADA"; // Rojo
                    }
                }
                grilla.add(new CeldaCalendarioDTO(fechaActual.toString(), habitacion.getIdHabitacion(), estadoCelda, idReservaEncontrada));
            }
        }
        return grilla;
    }
    

}
