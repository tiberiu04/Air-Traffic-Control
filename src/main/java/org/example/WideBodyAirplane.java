package org.example;

import java.io.ObjectInputFilter;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WideBodyAirplane extends Airplane{
	public WideBodyAirplane(String model, String flightID, String departure,
							String destination, LocalTime desiredTime,
							Status status, boolean isUrgent) {
		super(model, flightID, departure, destination, desiredTime, status, isUrgent);
	}

	@Override
	public String toString() {
		return "Wide Body - " + super.toString();
	}
}
