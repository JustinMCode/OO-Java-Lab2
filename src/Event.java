import java.time.LocalDateTime;

public abstract class Event implements Comparable<Event> {

    // Initialize variables name and dateTime
    private String name;
    private LocalDateTime dateTime;

    // Constructor
    public Event(String name, LocalDateTime dateTime) {
        this.name = name;
        this.dateTime = dateTime;
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter method for dateTime
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Getter function for name
    public String getName() {
        return name;
    }

    // Getter function for dateTime
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Compares date of Event to incoming event, and returns.
    @Override
    public int compareTo(Event o) {
        return this.dateTime.compareTo(o.dateTime);
    }
}
