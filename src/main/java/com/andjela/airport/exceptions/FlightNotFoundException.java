package com.andjela.airport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FlightNotFoundException extends ResponseStatusException {

    public FlightNotFoundException(String flightNumber) {
        super(HttpStatus.NOT_FOUND, "Flight with flight number " + flightNumber + " not found.");
    }
}
