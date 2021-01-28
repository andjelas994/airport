package com.andjela.airport.repository;

import com.andjela.airport.entity.Flight;
import com.andjela.airport.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {

    List<Gate> findByFlight(Flight flight);

    Gate findOneByGateNumber(String gateNumber);

}
