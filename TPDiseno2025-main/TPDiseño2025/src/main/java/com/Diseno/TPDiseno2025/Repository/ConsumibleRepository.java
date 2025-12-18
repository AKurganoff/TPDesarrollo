package com.Diseno.TPDiseno2025.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Consumible;

@Repository
public interface ConsumibleRepository extends JpaRepository<Consumible, Integer> {
    List<Consumible> findByIdEstadia_IdEstadia(Integer idEstadia);
}
