package com.andjela.airport.service;

import com.andjela.airport.entity.Flight;
import com.andjela.airport.entity.Gate;
import com.andjela.airport.repository.FlightRepository;
import com.andjela.airport.repository.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class DataLoader {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private FlightRepository flightRepository;

    @PostConstruct
    public void loadGates() {
        Gate gate1 = new Gate();
        gate1.setGateNumber("A1");

        Gate gate2 = new Gate();
        gate2.setGateNumber("A2");

        Flight flight1 = new Flight();
        flight1.setFlightNumber("AS301");

        Flight flight2 = new Flight();
        flight2.setFlightNumber("AS601");

        gateRepository.save(gate1);
        gateRepository.save(gate2);

        flightRepository.save(flight1);
        flightRepository.save(flight2);
    }

}
