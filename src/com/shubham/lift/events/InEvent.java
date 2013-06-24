/**
 *
 */
package com.shubham.lift.events;


/**
 * InEvent signifies a person pressing button from outside the lift to get inside the elevator
 * @author magiclko
 *
 */
public class InEvent extends GenericEvent {

	private int source;
	private String direction;

	public InEvent(int source, String direction) {
		this.source = source;
		this.direction = direction;
	}

	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}

}
