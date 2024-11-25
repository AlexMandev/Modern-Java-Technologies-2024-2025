package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

class EventComparatorByPriorityThenTimeStamp<T extends Event<?>> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        int priority = Integer.compare(o1.getPriority(), o2.getPriority());
        if (priority != 0) {
            return priority;
        }
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {
    private final Queue<T> unprocessedEvents;

    public DeferredEventSubscriber() {
        this.unprocessedEvents = new PriorityQueue<>(new EventComparatorByPriorityThenTimeStamp<>());
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        unprocessedEvents.add(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted by
     * their priority in descending order. Events with equal priority are ordered in ascending order
     * of their timestamps.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        return this.unprocessedEvents.iterator();
    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return this.unprocessedEvents.isEmpty();
    }

}