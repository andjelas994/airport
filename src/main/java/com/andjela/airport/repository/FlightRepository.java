package com.andjela.airport.repository;

import com.andjela.airport.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Flight findOneByFlightNumber(String flightNumber);

}
