import javax.swing.*;
import java.time.LocalDateTime;

public class EventPlanner {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Event Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create EventManager
        EventManager eventManager = new EventManager();

        // Create panels
        EventListPanel eventListPanel = new EventListPanel(eventManager);
        CalendarDisplay calendarDisplay = new CalendarDisplay(eventManager);

        // Register panels as listeners
        eventManager.addListener(eventListPanel);
        eventManager.addListener(calendarDisplay);

        // Add default events
        addDefaultEvents(eventManager);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Event List", eventListPanel);
        tabbedPane.addTab("Calendar", calendarDisplay);

        // Add to frame
        frame.add(tabbedPane);

        // Set visible
        frame.setVisible(true);
    }

    static void addDefaultEvents(EventManager eventManager) {
        // Create some default events
        Event deadline = new Deadline("Project Deadline", LocalDateTime.now().plusDays(2));
        Event meeting = new Meeting("Team Meeting", LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), "Conference Room");

        // Add to the events list
        eventManager.addEvent(deadline);
        eventManager.addEvent(meeting);
    }
}
