/**
 *
 */
package com.shubham.lift.events;

/**
 * OutEvent signifies a person pressing button from inside the lift to get to his/her destination floor
 * @author magiclko
 *
 */
public class OutEvent extends GenericEvent {

	private int destination;

	public OutEvent(int destination) {
		this.destination = destination;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

}
