/**
 *
 */
package com.shubham.lift.entity;

import java.util.Random;

import com.shubham.lift.LiftConstants;
import com.shubham.lift.events.InEvent;
import com.shubham.lift.events.OutEvent;

/**
 * @author magiclko
 *
 */
public class Person {

	private int source; //to store at which floor he pressed the lift button
	private int destination; //to store where a particular person wants to go
	private String direction; //direction button which he pressed

	public Person() {

		//just for the sake of POC, fill the data in constructor!
		Random rdmFloor = new Random();
		Random rdmDir = new Random();

		int out = rdmFloor.nextInt(LiftConstants.MAX_FLOORS);
		String dir = (rdmDir.nextInt(2) == 1) ? LiftConstants.UP: LiftConstants.DOWN;
		if (out == (LiftConstants.MAX_FLOORS -1)) {
			dir = LiftConstants.DOWN;
		}

		if (out == 0) {
			dir = LiftConstants.UP;
		}

		int in = 0;

		if (dir.equals(LiftConstants.DOWN)) {
			in = rdmFloor.nextInt(out);
			if (in == out) {
				in = rdmFloor.nextInt(out);
			}
		} else {
			while(true) {
				in = rdmFloor.nextInt(LiftConstants.MAX_FLOORS);
				if (in > out) {
					break;
				}
			}
		}

		this.source = out;
		this.destination = in;
		this.direction = dir;

	}

	public InEvent generateInEvent() {
		System.out.println("A person pressed " + this.direction + " from floor no. " + this.source);
		return new InEvent(this.source, this.direction);
	}

	public OutEvent generateOutEvent() {
		System.out.println("A person pressed " + this.destination + " from inside lift.");
		return new OutEvent(this.destination);
	}

	//getters and setters

	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}

}
