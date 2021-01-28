package com.andjela.airport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FlightBusyException extends ResponseStatusException {

    public FlightBusyException(String gateNumber) {
        super(HttpStatus.NOT_FOUND, "Flight is already assigned to the gate " + gateNumber + ".");
    }
}
