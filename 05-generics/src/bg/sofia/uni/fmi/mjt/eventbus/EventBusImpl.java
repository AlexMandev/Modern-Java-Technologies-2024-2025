package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {
    private final Map<Class<? extends Event<?>>, List<Subscriber<?>>> eventSubscribers;
    private final Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;

    public EventBusImpl() {
        this.eventSubscribers = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null.");
        }

        eventSubscribers.putIfAbsent(eventType, new ArrayList<>());
        if (!this.eventSubscribers.get(eventType).contains(subscriber)) {
            this.eventSubscribers.get(eventType).add(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null.");
        }

        List<Subscriber<?>> subscribers = eventSubscribers.get(eventType);
        if (subscribers == null || !subscribers.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber not found for the event type.");
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        List<Subscriber<?>> subscribers = eventSubscribers.get(event.getClass());
        if (subscribers != null) {
            for (Subscriber<?> subscriber : subscribers) {
                ((Subscriber<T>) subscriber).onEvent(event);
            }
        }
        var events = this.eventLogs.getOrDefault(event.getClass(), null);
        if (events == null) {
            this.eventLogs.put((Class<? extends Event<?>>) event.getClass(), new ArrayList<>());
        }
        this.eventLogs.get(event.getClass()).add(event);
    }

    @Override
    public void clear() {
        eventSubscribers.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Event type, from timestamp, and to timestamp cannot be null.");
        }

        List<Event<?>> events = eventLogs.get(eventType);
        if (events == null) {
            return Collections.emptyList();
        }

        List<Event<?>> result = new ArrayList<>();
        for (Event<?> event : events) {
            if (!event.getTimestamp().isBefore(from) && event.getTimestamp().isBefore(to)) {
                result.add(event);
            }
        }

        result.sort(new EventComparatorByTimeStampAscending());

        return Collections.unmodifiableList(result);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null.");
        }

        List<Subscriber<?>> subscribers = eventSubscribers.get(eventType);
        return subscribers == null ? Collections.emptyList() : Collections.unmodifiableList(subscribers);
    }
}

class EventComparatorByTimeStampAscending implements Comparator<Event<?>> {
    @Override
    public int compare(Event<?> e1, Event<?> e2) {
        return e1.getTimestamp().compareTo(e2.getTimestamp());
    }
}