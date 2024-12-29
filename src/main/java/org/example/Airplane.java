package org.example;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Airplane {
	private String model;
	private String flightID;
	private String departure;
	private String destination;
	private LocalTime desiredTime;
	private LocalTime actualTime;
	private Status status;
	private boolean isUrgent;

	public Airplane() {
	}

	public Airplane(String model, String flightID, String departure,
					String destination, LocalTime desiredTime,
					Status status, boolean isUrgent) {
		this.model = model;
		this.flightID = flightID;
		this.departure = departure;
		this.destination = destination;
		this.desiredTime = desiredTime;
		this.status = status;
		this.isUrgent = isUrgent;
	}

	public String getFlightID() {
		return flightID;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalTime getDesiredTime() {
		return desiredTime;
	}

	public void setActualTime(LocalTime actualTime) {
		this.actualTime = actualTime;
	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public LocalTime getActualTime() {
		return actualTime;
	}

	@Override
	public String toString() {
		return String.format("%s - %s - %s - %s - %s - %s%s",
				model,
				flightID,
				departure,
				destination,
				status,
				desiredTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
				actualTime != null ? " - " + actualTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "");
	}

}
