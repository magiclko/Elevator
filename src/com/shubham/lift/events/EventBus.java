package com.shubham.lift.events;

/**
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.shubham.lift.CustomListener;

/**
 * @author magiclko
 *
 */
public class EventBus {

	private static List<CustomListener> listeners = new ArrayList<>();

	public static void addListener(CustomListener listener) {
		EventBus.listeners.add(listener);
	}

	public static void addEvent(final GenericEvent e) {

		new Thread() {
			@Override
			public void run() {
				//EventBus.inEvents.add(e);
				notifyListeners(e);
			}

		}.start();

	}

	private static void notifyListeners(GenericEvent e) {
		for(CustomListener l : EventBus.listeners) {
			l.newEvent(e);
		}
	}

}
