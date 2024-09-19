import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The Meeting class represents a meeting event.
 * It extends the Event class and implements Completable.
 */
public class Meeting extends Event {

    private LocalDateTime endDateTime; // The time the meeting is over
    private String location;           // Represents the location of the event
    private boolean complete = false;  // Holds whether the meeting is complete

    // Constructor matching the one used in EventTester.
    public Meeting(String name, LocalDateTime start, LocalDateTime end, String location) {
        super(name, start);
        this.endDateTime = end;
        this.location = location;
    }

    // Getter for endDateTime
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    // Setter for endDateTime
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // Getter for endTime
    public LocalDateTime getEndTime() {
        return this.endDateTime;
    }

    // Setter method named getEndTime(LocalDateTime endDateTime)
    public void getEndTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // Getter for location
    public String getLocation() {
        return this.location;
    }

    // Setter for location
    public void setLocation(String location) {
        this.location = location;
    }

    // Override complete() method from Event
    @Override
    public void complete() {
        this.complete = true;
        super.complete();
    }

    /**
     * Override isComplete() method from Event.
     * Return true if the meeting is complete, false otherwise.
     */
    @Override
    public boolean isComplete() {
        return this.complete;
    }

    // getDuration() returns a Duration object representing the duration of the meeting.
    public Duration getDuration() {
        return Duration.between(this.getDateTime(), this.endDateTime);
    }
}
