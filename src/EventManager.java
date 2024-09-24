import java.util.ArrayList;
import java.util.List;

/**
 * The EventManager class manages a list of events and notifies registered listeners
 * when changes occur to the event list. It provides methods to add and remove events,
 * retrieve the list of events, and manage event listeners.
 */
public class EventManager {
    private final List<Event> events;             // List to store events
    private final List<EventListener> listeners;  // List to store registered listeners

    // Constructs an EventManager with empty lists for events and listeners.
    public EventManager() {
        events = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    // Method that adds an event to the event list and notifies all registered listeners of the update
    public void addEvent(Event event) {
        events.add(event);
        notifyListeners();
    }

    // Method that removes an event from the event list and notifies all registered listeners of the update.
    public void removeEvent(Event event) {
        events.remove(event);
        notifyListeners();
    }

    // Method that retrieves the list of events managed by the EventManager
    public List<Event> getEvents() {
        return events;
    }

    // Method that registers an EventListener to receive notifications when the event list is updated
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    // Method that Notifies all registered listeners that the event list has been updated.
    public void notifyListeners() {
        for (EventListener listener : listeners) {
            listener.eventsUpdated();
        }
    }

    /**
     * The EventListener interface should be implemented by any class that wants to receive
     * notifications when the event list is updated. Implementing classes must define the
     * eventsUpdated() method.
     */
    public interface EventListener {
        // Called when the event list has been updated
        void eventsUpdated();
    }
}
