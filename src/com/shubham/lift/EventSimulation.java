/**
 *
 */
package com.shubham.lift;

import java.util.Random;

import com.shubham.lift.entity.Person;
import com.shubham.lift.events.EventBus;
import com.shubham.lift.events.GenericEvent;

/**
 * @author magiclko
 *
 */
public class EventSimulation implements Runnable {


	@Override
	public void run() {

		while(true) {

			Random rdm = new Random();
			int i = rdm.nextInt(10);

			while(i-- > 0) {
				Person p = new Person();

				GenericEvent e = p.generateInEvent();
				EventBus.addEvent(e);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
