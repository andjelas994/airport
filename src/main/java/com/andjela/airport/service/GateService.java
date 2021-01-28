package com.andjela.airport.service;

import com.andjela.airport.entity.Flight;
import com.andjela.airport.entity.Gate;
import com.andjela.airport.exceptions.AvailableGateNotFoundException;
import com.andjela.airport.exceptions.FlightBusyException;
import com.andjela.airport.exceptions.FlightNotFoundException;
import com.andjela.airport.exceptions.GateNotFoundException;
import com.andjela.airport.repository.FlightRepository;
import com.andjela.airport.repository.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GateService {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Transactional
    public Gate getAndAssignGateByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.findOneByFlightNumber(flightNumber);
        if (flight == null) {
            throw new FlightNotFoundException(flightNumber);
        }
        Gate assignedGate = findGateByAssignedFlight(flight);
        if (assignedGate != null) {
            throw new FlightBusyException(assignedGate.getGateNumber());
        }
        while (true) {
            List<Gate> availableGates = getAvailableGates();
            if (availableGates.isEmpty()) {
                throw new AvailableGateNotFoundException();
            }
            Gate gate = availableGates.get(0);
            gate.setFlight(flight);
            try {
                gateRepository.save(gate);
                return gate;
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Available gate already taken, continuing the search");
            }
        }
    }

    private Gate findGateByAssignedFlight(Flight flight) {
        List<Gate> gates = gateRepository.findByFlight(flight);
        return gates.isEmpty() ? null : gates.get(0);
    }

    private List<Gate> getAvailableGates() {
        return gateRepository.findByFlight(null);
    }

    @Transactional
    public Gate updateGateAsAvailable(String gateNumber) {
        Gate gate = gateRepository.findOneByGateNumber(gateNumber);
        if (gate == null) {
            throw new GateNotFoundException(gateNumber);
        }
        gate.setFlight(null);
        gateRepository.save(gate);
        return gate;
    }

}
