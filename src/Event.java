import java.time.LocalDateTime;

public abstract class Event implements Comparable<Event>, Completable {

    private String name;
    private LocalDateTime dateTime;
    private boolean complete = false;

    // Constructor
    public Event(String name, LocalDateTime dateTime) {
        this.name = name;
        this.dateTime = dateTime;
    }

    // Getter method for name
    public String getName() {
        return this.name;
    }

    // Getter method for dateTime
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter method for dateTime
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Implement compareTo method
    @Override
    public int compareTo(Event o) {
        return this.dateTime.compareTo(o.dateTime);
    }

    // Implement Completable methods
    @Override
    public void complete() {
        this.complete = true;
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }
}
