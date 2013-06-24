/**
 *
 */
package com.shubham.lift;


/**
 * @author magiclko
 *
 */
public class StartLift {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		//start the lift
		Thread lift = new Thread(new LiftSimulation());
		lift.start();

		//start generating events
		Thread events = new Thread(new EventSimulation());
		events.start();

	}

}
