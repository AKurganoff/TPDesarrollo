package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.EstadiaDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Repository.DetalleEstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.DetalleReservaRepository;
import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.ReservaRepository;

@Service
public class EstadiaServiceImp implements EstadiaService{

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired 
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired 
    DetalleReservaRepository detalleReservaRepository;

    @Autowired
    DetalleEstadiaRepository detalleEstadiaRepository;


    @Override
    public void crearEstadia(EstadiaDTO eDTO){
        Estadia e = this.mapToDTO(new Estadia(), eDTO);
        estadiaRepository.save(e);
    }
    
    @Override
    public Estadia buscarEstadia(EstadiaDTO eDTO){
        return estadiaRepository.getById(eDTO.getIdEstadia());
    }

    @Override
    public Estadia mapToDTO(Estadia e, EstadiaDTO eDTO){
        Reserva res = reservaRepository.getById(eDTO.getIdEstadia());
        e.setIdEstadia(eDTO.getIdEstadia());
        e.setPrecio(eDTO.getPrecio());
        e.setHoraCheckIn(eDTO.getHoraCheckin());
        e.setHoraCheckOut(eDTO.getHoraCheckout());
        e.setReserva(res);
        return e;
    }
    
    @Override
    @Transactional
    public void ocuparHabitacion(List<HabitacionDTO> habitacionesDTO, List<HuespedDTO> huespedesDTO, LocalDate fechaInicio, LocalDate fechaFin, Integer idReservaPrevia) {

        Reserva reserva;
        double precioHab = 0.0;
        double precioTotal = 0.0;
        
        //Obtener o Crear Reserva 
        if (idReservaPrevia != null) {
            reserva = reservaRepository.findById(idReservaPrevia).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            if (huespedesDTO == null || huespedesDTO.isEmpty()) {
                throw new RuntimeException("Debe ingresar un huésped titular.");
            }

            HuespedDTO huespedEntrante = huespedesDTO.get(0);
            Huesped titularReal = reserva.getHuesped();

            if (!titularReal.getDni().equals(huespedEntrante.getDni())) {
                throw new RuntimeException("ERROR: El huésped ingresado (" + huespedEntrante.getApellido() + ") no es el titular de la reserva. El titular es: " + titularReal.getApellido() + ", " + titularReal.getNombre());
            }

            reserva.setEstado("Ocupada");
            if(!habitacionesDTO.isEmpty()){
                Habitacion habitacion = habitacionRepository.findById(habitacionesDTO.get(0).getIdHabitacion()).orElse(null);
                 if(habitacion != null) precioHab = habitacion.getIdTipo().getPrecioNoche();
            }

        } else {
            reserva = new Reserva();
            reserva.setFechaInicio(fechaInicio);
            long noches = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            reserva.setCantNoches((int) (noches > 0 ? noches : 1)); 
            reserva.setEstado("Ocupada"); 
            reserva.setCantHuesped(huespedesDTO.size());
            if(noches >= habitacionesDTO.get(0).getNochesDescuento()){
                reserva.setDescuento(true);
            }else{
                reserva.setDescuento(false);
            }

            Huesped titular = huespedRepository.findById(huespedesDTO.get(0).getDni()).orElseThrow(() -> new RuntimeException("Huesped titular no encontrado"));

            reserva.setHuesped(titular);
            reserva = reservaRepository.save(reserva);

            //Detalle Reserva 
            for (HabitacionDTO habDTO : habitacionesDTO) {
                DetalleReserva detalle = new DetalleReserva();
                detalle.setReserva(reserva);
                Habitacion habitacion = habitacionRepository.findById(habDTO.getIdHabitacion()).orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
                precioHab = habitacion.getIdTipo().getPrecioNoche();
                detalle.setHabitacion(habitacion);
                detalle.setCantidadNoches(reserva.getCantNoches());

                // Asumiendo que el DTO tiene el precio, o lo sacas del tipo de habitación

                detalle.setPrecio(habitacion.getIdTipo().getPrecioNoche() * reserva.getCantNoches()); 
                detalleReservaRepository.save(detalle);
            }
        }

        //Crear Estadía 
        Estadia nuevaEstadia = new Estadia();
        nuevaEstadia.setReserva(reserva);
        nuevaEstadia.setHoraCheckIn(LocalTime.now());
        nuevaEstadia.setHoraCheckOut(LocalTime.of(10, 0)); 
        precioTotal = precioHab * habitacionesDTO.size();
        nuevaEstadia.setPrecio(precioTotal);
        estadiaRepository.save(nuevaEstadia);

        for (HuespedDTO hDTO : huespedesDTO) {
            DetalleEstadia de = new DetalleEstadia();
            de.setEstadia(nuevaEstadia);
            de.setHuesped(huespedRepository.findById(hDTO.getDni()).get());
            detalleEstadiaRepository.save(de);
        }
    }
}
