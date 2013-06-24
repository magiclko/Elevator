
/**
 *
 */
package com.shubham.lift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.shubham.lift.entity.Person;
import com.shubham.lift.events.EventBus;
import com.shubham.lift.events.GenericEvent;
import com.shubham.lift.events.InEvent;
import com.shubham.lift.events.OutEvent;

/**
 * @author magiclko
 *
 */
public class Lift implements CustomListener {

	private Lift() {

	}

	private static class NewInstance {
		private static Lift lift = new Lift();
	}

	public static synchronized Lift getInstance() {
		return NewInstance.lift;
	}

	//fields
	private volatile List<GenericEvent> tempList = new ArrayList<>();
	private volatile NavigableSet<Integer> mainList = new TreeSet<>();
	private volatile Boolean tempListChanged = false;


	private volatile Integer current = 0;
	private volatile String direction = LiftConstants.UP;

	private void moveByOneStep(int from, int to, String direction) throws InterruptedException {
		System.out.println("Moving lift from " + from + " to " + to + " in " + direction + " direction.");
		Thread.sleep(100);
	}

	private synchronized void process() throws InterruptedException {

		//process it
		while(mainList.size() != 0 || tempListChanged) {
			if (tempListChanged || tempList.size() != 0) {
				synchronized(this.tempList) {
					Iterator<GenericEvent> it = tempList.iterator();
					while(it.hasNext()) {
						//put it in mainList
						GenericEvent e = it.next();
						if (e instanceof InEvent) {
							InEvent i = (InEvent) e;
							if (i.getDirection().equals(direction)) {
								mainList.add(i.getSource());
								it.remove();
							}

						}

						if (e instanceof OutEvent) {
							OutEvent i = (OutEvent) e;
							mainList.add(i.getDestination());
							it.remove();
						}
					}
				}

				tempListChanged = false;


			}

			//process it
			if (mainList.contains(current)) {

				if (current == mainList.last() && direction.equals(LiftConstants.UP)) {

					int higherFloor = getHighestFloor(current);
					if (higherFloor != -1) {
						mainList.add(higherFloor);
					} else {
						direction = LiftConstants.DOWN;
						System.out.println("Changing direction from up to down!");
					}

				} else if (current == mainList.first() && direction.equals(LiftConstants.DOWN)) {

					int lowerFloor = getLowestFloor(current);
					if (lowerFloor != (2*LiftConstants.MAX_FLOORS)) {
						mainList.add(lowerFloor);
					} else {
						direction = LiftConstants.UP;
						System.out.println("Changing direction from down to up!");
					}

				}

				//unload i.e. remove from tempList and mainList. Before removing check it was because of InEvent, if yes, create an OutEvent.
				List<InEvent>  list = findAtPosition(current);
				generateOutEvent(list.size());

				removeFromTempAtPosition(current);

				System.out.println("Stopped lift at floor no. " + current);

				mainList.remove(current);

			} else if (current < mainList.last() && direction.equals(LiftConstants.UP)) {
				moveByOneStep(current, current+1, LiftConstants.UP);
				current++;
			} else if (current > mainList.first() && direction.equals(LiftConstants.DOWN)) {
				moveByOneStep(current, current-1, LiftConstants.DOWN);
				current--;
			}
		}
	}

	/**
	 * Generate count number of OutEvents
	 * @param count
	 */
	private void generateOutEvent(int count) {
		for (int i = 0 ; i < count; i++) {
			new Thread() {
				@Override
				public void run() {
					Person p = new Person();

					GenericEvent e = p.generateOutEvent();
					EventBus.addEvent(e);
				}
			}.start();
		}

	}

	/**
	 * Return the highest floor greater than current
	 * @param current
	 * @return
	 */
	private int getHighestFloor(int current) {
		int maximum = -1;
		for (GenericEvent e : tempList) {
			if (e instanceof InEvent && (((InEvent) e).getSource() > current)) {
				if (((InEvent) e).getSource() > maximum) {
					maximum = ((InEvent) e).getSource();
				}
			}
		}
		return maximum;
	}

	/**
	 * Return the lowest floor less than current
	 * @param current
	 * @return
	 */
	private int getLowestFloor(int current) {
		int min = 2 * LiftConstants.MAX_FLOORS;
		for (GenericEvent e : tempList) {
			if (e instanceof InEvent && (((InEvent) e).getSource() > current)) {
				if (((InEvent) e).getSource() < min) {
					min = ((InEvent) e).getSource();
				}
			}
		}
		return min;
	}

	/**
	 * List containing InEvent requests by people at given floor
	 * @param current
	 * @return
	 */
	private List<InEvent> findAtPosition(int current) {
		List<InEvent> list = new ArrayList<>();
		for (GenericEvent e : tempList) {
			if (e instanceof InEvent && (((InEvent) e).getSource() == current)) {
				list.add((InEvent) e);
			}
		}
		return list;
	}

	/**
	 * Remove InEvent requests by people at given floor
	 * @param current
	 */
	private void removeFromTempAtPosition(int current) {
		Iterator<GenericEvent> it = tempList.iterator();

		while (it.hasNext()) {
			GenericEvent e = it.next();
			if (e instanceof InEvent && (((InEvent) e).getSource() == current)) {
				it.remove();
			}
		}
	}


	@Override
	public void newEvent(final GenericEvent e) {
		synchronized(this.tempList) {
			this.tempList.add(e);
			tempListChanged = true;
		}


		new Thread() {
			@Override
			public void run() {
				//process this list
				try {
					process();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
