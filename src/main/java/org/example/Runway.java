package org.example;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Runway<T extends Airplane> {
	private String id;
	private String usage; // "landing" or "takeoff"
	private PriorityQueue<T> queue;
	private T currentManeuveringPlane;

	public T getCurrentManeuveringPlane() {
		return currentManeuveringPlane;
	}

	public void setCurrentManeuveringPlane(T plane) {
		this.currentManeuveringPlane = plane;
	}

	public Runway(String id, String usage, Comparator<T> comparator) {
		this.id = id;
		this.usage = usage;
		this.queue = new PriorityQueue<>(comparator);
	}

	// Adding a plane to the queue
	public void addPlane(T plane) throws IncorrectRunwayException {
		if ((usage.equals("landing") && plane.getStatus() != Status.WAITING_FOR_LANDING) ||
				(usage.equals("takeoff") && plane.getStatus() != Status.WAITING_FOR_TAKEOFF)) {
			throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect.");
		}
		queue.add(plane);
	}

	// Granting permission for maneuver
	public T grantPermissionForManeuver(LocalTime currentTime) throws UnavailableRunwayException {
		if (queue.isEmpty()) {
			throw new UnavailableRunwayException("No planes available for maneuver.");
		}
		return queue.poll();
	}

	public String getUsage() {
		return usage;
	}

	public PriorityQueue<T> getQueue() {
		return queue;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		PriorityQueue<T> tempQueue = new PriorityQueue<>(queue);

		while (!tempQueue.isEmpty()) {
			sb.append(tempQueue.poll()).append("\n");
		}

		return sb.toString();
	}
}

