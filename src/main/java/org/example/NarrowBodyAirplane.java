package org.example;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class NarrowBodyAirplane extends Airplane{
	public NarrowBodyAirplane(String model, String flightID, String departure,
							  String destination, LocalTime desiredTime,
							  Status status, boolean isUrgent) {
		super(model, flightID, departure, destination, desiredTime, status, isUrgent);
	}

	@Override
	public String toString() {
		return "Narrow Body - " + super.toString();
	}
}
