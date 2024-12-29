package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CommandProcessor {
	private final Map<String, Runway<Airplane>> runways;
	private final Map<String, Airplane> airplanes;
	private final String testFolder;
	private Map<String, LocalTime> runwayAvailability = new HashMap<>();

	public CommandProcessor(Map<String, Runway<Airplane>> runways, Map<String, Airplane> airplanes, String testFolder) {
		this.runways = runways;
		this.airplanes = airplanes;
		this.testFolder = testFolder;
	}

	public void processCommand(String line) {
		try {
			// The data is split by the regex " - "
			String[] parts = line.split(" - ");
			String timestamp = parts[0];
			String command = parts[1];
			// Processing the command
			switch (command) {
				case "add_runway_in_use":
					addRunway(parts);
					break;
				case "allocate_plane":
					allocatePlane(parts);
					break;
				case "permission_for_maneuver":
					permissionForManeuver(parts);
					break;
				case "runway_info":
					runwayInfo(parts);
					break;
				case "flight_info":
					flightInfo(parts);
					break;
				case "exit":
					// Program ends
					break;
				default:
					logException(String.format("Unknown command: %s", command));
					break;
			}
		} catch (Exception e) {
			logException(String.format("Error processing command: %s | %s", line, e.getMessage()));
		}
	}

	private void addRunway(String[] parts) {
		String id = parts[2];
		String usage = parts[3]; // "landing" or "takeoff"
		String type = parts[4]; // "wide body" or "narrow body"

		Comparator<Airplane> comparator;

		// Setting the comparator based on the runway usage
		if (usage.equals("landing")) {
			comparator = Comparator
					.comparing(Airplane::isUrgent, Comparator.reverseOrder())
					.thenComparing(Airplane::getDesiredTime);
		} else if (usage.equals("takeoff")) {
			comparator = Comparator.comparing(Airplane::getDesiredTime);
		} else {
			throw new IllegalArgumentException("Invalid runway usage type");
		}
		// Creating the runway based on the type
		Runway<Airplane> runway = type.equalsIgnoreCase("wide body")
				? new Runway<>(id, usage, comparator)
				: new Runway<>(id, usage, comparator);

		runways.put(id, runway);
	}

	private void allocatePlane(String[] parts) throws IOException {
		String timestamp = parts[0];
		String type = parts[2];
		String model = parts[3];
		String flightID = parts[4];
		String departure = parts[5];
		String destination = parts[6];
		LocalTime desiredTime = LocalTime.parse(parts[7], DateTimeFormatter.ofPattern("HH:mm:ss"));
		String runwayID = parts[8];
		boolean isUrgent = parts.length > 9 && parts[9].equalsIgnoreCase("urgent");
		// Casting the runway to the correct type and checking if it exists
		@SuppressWarnings("unchecked")
		Runway<Airplane> runway = (Runway<Airplane>) runways.get(runwayID);
		if (runway == null) {
			logException(String.format("%s | The chosen runway does not exist", timestamp));
			return;
		}
		// Creating the airplane based on the type
		boolean isLanding = destination.equalsIgnoreCase("Bucharest");
		Status initialStatus = isLanding ? Status.WAITING_FOR_LANDING : Status.WAITING_FOR_TAKEOFF;

		Airplane airplane;
		if (type.equalsIgnoreCase("wide body")) {
			airplane = new WideBodyAirplane(model, flightID, departure, destination, desiredTime, initialStatus, isUrgent);
		} else {
			airplane = new NarrowBodyAirplane(model, flightID, departure, destination, desiredTime, initialStatus, isUrgent);
		}
		// Adding the plane to the runway and the airplanes map
		try {
			runway.addPlane(airplane);
			airplanes.put(flightID, airplane);
		} catch (IncorrectRunwayException e) {
			logException(String.format("%s | The chosen runway for allocating the plane is incorrect", timestamp));
		}
	}

	private void permissionForManeuver(String[] parts) throws IOException {
		String timestamp = parts[0];
		LocalTime currentTime = LocalTime.parse(timestamp, DateTimeFormatter.ofPattern("HH:mm:ss"));
		String runwayID = parts[2];
		// Obtaining the runway and checking its existence
		Runway<Airplane> runway = runways.get(runwayID);
		if (runway == null) {
			logException(String.format("%s | The chosen runway does not exist", timestamp));
			return;
		}
		// Checking if the runway is available
		LocalTime availableTime = runwayAvailability.getOrDefault(runwayID, LocalTime.MIN);
		if (currentTime.isAfter(availableTime)) {
			runway.setCurrentManeuveringPlane(null);
		}
		// Checking if the runway is occupied by another plane
		Airplane currentManeuveringPlane = runway.getCurrentManeuveringPlane();
		if (currentManeuveringPlane != null) {
			logException(String.format("%s | The chosen runway for maneuver is currently occupied", timestamp));
			return;
		}
		// Checking the time for the runway availability
		if (currentTime.isBefore(availableTime)) {
			logException(String.format("%s | The chosen runway for maneuver is currently occupied", timestamp));
			return;
		}

		try {
			// Granting permission for maneuver
			Airplane plane = runway.grantPermissionForManeuver(currentTime);
			if (plane != null) {
				// Setting the status and the actual time
				plane.setStatus(runway.getUsage().equals("landing") ? Status.LANDED : Status.DEPARTED);
				plane.setActualTime(currentTime);
				// Setting the current maneuvering plane
				runway.setCurrentManeuveringPlane(plane);
				// Setting the runway availability based on the plane type
				runwayAvailability.put(runwayID, currentTime.plusMinutes(runway.getUsage().equals("landing") ? 10 : 5));
			}
		} catch (UnavailableRunwayException e) {
			logException(String.format("%s | %s", timestamp, e.getMessage()));
		}
	}

	private void runwayInfo(String[] parts) throws IOException {
		String timestamp = parts[0];
		String runwayID = parts[2];
		// Obtaining the runway and checking its existence
		Runway<Airplane> runway = runways.get(runwayID);
		if (runway == null) {
			logException(String.format("%s | The chosen runway does not exist", timestamp));
			return;
		}
		// Creating the file name and parsing the requested time
		String fileName = String.format("%s/runway_info_%s_%s.out", testFolder, runwayID, timestamp.replace(":", "-"));
		LocalTime requestedTime = LocalTime.parse(timestamp, DateTimeFormatter.ofPattern("HH:mm:ss"));

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			boolean isOccupied = false;
			Airplane matchedPlane = null;
			// Checking if the runway is occupied by another plane
			Airplane currentManeuveringPlane = runway.getCurrentManeuveringPlane();
			if (currentManeuveringPlane != null &&
					currentManeuveringPlane.getActualTime() != null &&
					!requestedTime.isAfter(runwayAvailability.getOrDefault(runwayID, LocalTime.MIN))) {
				isOccupied = true;
				matchedPlane = currentManeuveringPlane;
			}
			// Checking if the runway is available
			if (!isOccupied) {
				LocalTime unavailableUntil = runwayAvailability.getOrDefault(runwayID, LocalTime.MIN);
				if (requestedTime.isBefore(unavailableUntil)) {
					isOccupied = true;
				}
			}
			// Writing the runway information specifically it is free or occupied to the file
			writer.write(runway.getId() + " - " + (isOccupied ? "OCCUPIED" : "FREE") + "\n");

			PriorityQueue<?> tempQueue = new PriorityQueue<>(runway.getQueue());
			while (!tempQueue.isEmpty()) {
				writer.write(tempQueue.poll().toString() + "\n");
			}
		}
	}

	private void flightInfo(String[] parts) throws IOException {
		String timestamp = parts[0];
		String flightID = parts[2];
		// Obtaining the airplane and checking its existence
		Airplane airplane = airplanes.get(flightID);
		if (airplane == null) {
			logException(String.format("%s | Flight %s does not exist", timestamp, flightID));
			return;
		}
		// Creating the file name and writing the airplane information to the file
		String fileName = testFolder + "/flight_info.out";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			writer.write(String.format("%s | %s%n", timestamp, airplane.toString()));
		}
	}

	private void logException(String message) {
		String fileName = testFolder + "/board_exceptions.out";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			writer.write(message + "\n");
		} catch (IOException e) {
			System.err.println("Failed to log exception: " + e.getMessage());
		}
	}
}