import java.time.LocalDateTime;

/**
 * The Deadline class represents a deadline event.
 * It extends the Event class and implements Completable.
 */
public class Deadline extends Event {

    // Constructor matching the one used in EventTester
    public Deadline(String name, LocalDateTime deadline) {
        super(name, deadline);
    }

}
