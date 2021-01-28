package com.andjela.airport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GateNotFoundException extends ResponseStatusException {

    public GateNotFoundException(String gateNumber) {
        super(HttpStatus.NOT_FOUND, "Gate with gate number " + gateNumber + " not found.");
    }
}
