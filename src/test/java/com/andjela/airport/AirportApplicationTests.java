package com.andjela.airport;

import com.andjela.airport.entity.Flight;
import com.andjela.airport.entity.Gate;
import com.andjela.airport.exceptions.AvailableGateNotFoundException;
import com.andjela.airport.exceptions.FlightBusyException;
import com.andjela.airport.exceptions.FlightNotFoundException;
import com.andjela.airport.exceptions.GateNotFoundException;
import com.andjela.airport.repository.FlightRepository;
import com.andjela.airport.repository.GateRepository;
import com.andjela.airport.service.GateService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AirportApplicationTests {

	@Mock
	private GateRepository gateRepository;

	@Mock
	private FlightRepository flightRepository;

	@InjectMocks
	private GateService gateService;

	@Captor
	ArgumentCaptor<Gate> gateArgumentCaptor;

	@Test
	void testAssignGateToAFlight() {
		String flightNumber = "TEST000";
		Flight flight = new Flight();
		flight.setId(1);
		flight.setFlightNumber(flightNumber);
		Mockito.when(flightRepository.findOneByFlightNumber(flightNumber)).thenReturn(flight);
		String gateNumber = "T1";
		Gate gate = new Gate();
		gate.setId(1);
		gate.setGateNumber(gateNumber);
		Mockito.when(gateRepository.findByFlight(flight)).thenReturn(new ArrayList<>());
		Mockito.when(gateRepository.findByFlight(null)).thenReturn(Arrays.asList(gate));
		Mockito.when(gateRepository.save(any(Gate.class))).thenReturn(gate);

		gateService.getAndAssignGateByFlightNumber(flightNumber);

		Mockito.verify(gateRepository).save(gateArgumentCaptor.capture());
		Gate savedGate = gateArgumentCaptor.getValue();

		assertEquals(gate, savedGate);
	}

	@Test
	void testAssignGateFlightNotFound() {
		String flightNumber = "anything";
		Mockito.when(flightRepository.findOneByFlightNumber(flightNumber)).thenReturn(null);

		FlightNotFoundException exception = assertThrows(FlightNotFoundException.class, () -> {
			gateService.getAndAssignGateByFlightNumber(flightNumber);
		});
		String expectedMessage = "Flight with flight number " + flightNumber + " not found.";
		String actualMessage = exception.getReason();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	void testAssignGateFlightAlreadyAssigned() {
		String flightNumber = "TEST000";
		Flight flight = new Flight();
		flight.setId(1);
		flight.setFlightNumber(flightNumber);
		Mockito.when(flightRepository.findOneByFlightNumber(flightNumber)).thenReturn(flight);
		String gateNumber = "T1";
		Gate gate = new Gate();
		gate.setId(1);
		gate.setGateNumber(gateNumber);
		Mockito.when(gateRepository.findByFlight(flight)).thenReturn(Arrays.asList(gate));

		FlightBusyException exception = assertThrows(FlightBusyException.class, () -> {
			gateService.getAndAssignGateByFlightNumber(flightNumber);
		});
		String expectedMessage = "Flight is already assigned to the gate " + gate.getGateNumber() + ".";
		String actualMessage = exception.getReason();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	void testAssignGateNoAvailableGates() {
		String flightNumber = "blabla";
		Mockito.when(gateRepository.findByFlight(null)).thenReturn(new ArrayList<>());
		Mockito.when(flightRepository.findOneByFlightNumber(flightNumber)).thenReturn(new Flight());

		AvailableGateNotFoundException exception = assertThrows(AvailableGateNotFoundException.class, () -> {
			gateService.getAndAssignGateByFlightNumber(flightNumber);
		});
		String expectedMessage = "There are no available gates at the moment.";
		String actualMessage = exception.getReason();

		assertEquals(expectedMessage, actualMessage);
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	void testUpdateGateAsAvailableGateNotFound() {
		Mockito.when(gateRepository.findOneByGateNumber(any(String.class))).thenReturn(null);

		assertThrows(GateNotFoundException.class, () -> {
			gateService.updateGateAsAvailable("T1");
		});
	}

	@Test
	void testUpdateGateAsAvailable() {
		String gateNumber = "T1";
		Gate gate = new Gate();
		gate.setId(1);
		gate.setGateNumber(gateNumber);
		gate.setFlight(new Flight());
		Mockito.when(gateRepository.findOneByGateNumber(gateNumber)).thenReturn(gate);
		Mockito.when(gateRepository.save(any(Gate.class))).thenReturn(gate);
		assertFalse(gate.isAvailable());
		gateService.updateGateAsAvailable(gateNumber);

		Mockito.verify(gateRepository).save(gateArgumentCaptor.capture());
		Gate savedGate = gateArgumentCaptor.getValue();
		assertTrue(savedGate.isAvailable());
	}

	@Disabled
	@Test
	void testAssignFlightToGate() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Callable<Boolean>> callableTasks = new ArrayList<>();

		for (int i=0; i<100; i++) {
			Callable<Boolean> callableTask = () -> {
				try {
					Gate gate = gateService.getAndAssignGateByFlightNumber("AS000");
					System.out.println(gate);
					return true;
				} catch (RuntimeException e) {
					System.out.println("NO AVAILABLE GATES");
					return false;
				}
			};
			callableTasks.add(callableTask);
		}

		List<Future<Boolean>> future =
				executor.invokeAll(callableTasks);
	}

	@Disabled
	@Test
	void testUpdateGateAsAvailable2() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Callable<Boolean>> callableTasks = new ArrayList<>();

		for (int i=0; i<100; i++) {
			Callable<Boolean> callableTask = () -> {
				try {
					Gate gate = gateService.updateGateAsAvailable("A1");
					System.out.println(gate);
					return true;
				} catch (RuntimeException e) {
					e.printStackTrace();
					System.out.println("GATE UPDATE FAILED");
					return false;
				}
			};
			callableTasks.add(callableTask);
		}

		List<Future<Boolean>> future =
				executor.invokeAll(callableTasks);
	}

}
