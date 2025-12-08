package com.Diseno.TPDiseno2025.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;

@Repository
public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, Integer> {
    @Override
    Optional<DetalleReserva> findById(Integer idDetalle);

    Boolean existsByHabitacion_IdHabitacionAndReserva_IdReserva(Integer idHabitacion, Integer idReserva);

    List<DetalleReserva> findByReserva_IdReserva(Integer idReserva);

    @Override
    DetalleReserva getById(Integer idDetalle);

    @Override
    void deleteById(Integer idDetalle);

    DetalleReserva findByReserva_IdReservaAndHabitacion_IdHabitacion(Integer idReserva, Integer IdHabitacion);

    // Esta query busca todos los detalles de reservas activas que chocan con las fechas solicitadas
    @Query(value = "SELECT d.* FROM detalle_reserva d " +
                   "JOIN reserva r ON d.id_reserva = r.id_reserva " +
                   "WHERE r.estado != 'CANCELADA' " +
                   "AND (" +
                   "   r.fecha_inicio < :fechaFin " +
                   "   AND " +
                   // LÓGICA POSTGRESQL: fecha + (numero * intervalo de 1 dia)
                   "   (r.fecha_inicio + (r.cant_noches * INTERVAL '1 day')) > :fechaInicio " +
                   ")", nativeQuery = true)
    List<DetalleReserva> findReservasEnRango(
            @Param("fechaInicio") LocalDate fechaInicio, 
            @Param("fechaFin") LocalDate fechaFin);

}
