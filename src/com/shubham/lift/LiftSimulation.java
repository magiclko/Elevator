/**
 *
 */
package com.shubham.lift;

import com.shubham.lift.events.EventBus;

/**
 * @author magiclko
 *
 */
public class LiftSimulation implements Runnable {

	@Override
	public void run() {
		Lift lift = Lift.getInstance();
		registerToEventBus(lift);

	}

	private static void registerToEventBus(Lift lift) {
		EventBus.addListener(lift);
	}

}
