package com.andjela.airport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AvailableGateNotFoundException extends ResponseStatusException {

    public AvailableGateNotFoundException() {
        super(HttpStatus.NOT_FOUND, "There are no available gates at the moment.");
    }
}
