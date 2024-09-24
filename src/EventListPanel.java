import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * EventListPanel is a JPanel that displays a list of events.
 * It provides sorting and filtering options and allows adding new events.
 * The panel updates dynamically when events are added, removed, or updated.
 */
public class EventListPanel extends JPanel implements EventManager.EventListener {
    private final EventManager eventManager;     // Reference to the EventManager
    private final JPanel displayPanel;           // Panel to display the list of events
    private final JComboBox<String> sortDropDown;// ComboBox for sorting options
    private final JCheckBox filterCompleted;     // CheckBox to hide/show completed events
    private final JCheckBox filterDeadlines;     // CheckBox to hide/show deadlines
    private final JCheckBox filterMeetings;      // CheckBox to hide/show meetings

    // Constructs an EventListPanel with the given EventManager
    public EventListPanel(EventManager eventManager) {
        this.eventManager = eventManager;

        // Set the layout manager for this panel
        setLayout(new BorderLayout());

        // Initialize the control panel containing sort options and filters
        JPanel controlPanel = new JPanel();

        // Sort drop-down menu
        String[] sortOptions = {"Name Asc", "Name Desc", "Closest First", "Farthest First"};
        sortDropDown = new JComboBox<>(sortOptions);
        sortDropDown.addActionListener(e -> refreshDisplay()); // Refresh display on selection change
        controlPanel.add(sortDropDown);

        // Filter checkboxes

        // CheckBox to hide completed events
        filterCompleted = new JCheckBox("Hide Completed");
        filterCompleted.addItemListener(e -> refreshDisplay()); // Refresh display on state change
        controlPanel.add(filterCompleted);

        // CheckBox to show/hide deadlines
        filterDeadlines = new JCheckBox("Show Deadlines");
        filterDeadlines.setSelected(true); // Default to showing deadlines
        filterDeadlines.addItemListener(e -> refreshDisplay()); // Refresh display on state change
        controlPanel.add(filterDeadlines);

        // CheckBox to show/hide meetings
        filterMeetings = new JCheckBox("Show Meetings");
        filterMeetings.setSelected(true); // Default to showing meetings
        filterMeetings.addItemListener(e -> refreshDisplay()); // Refresh display on state change
        controlPanel.add(filterMeetings);

        // Add Event Button
        JButton addEventButton = new JButton("Add Event");
        addEventButton.addActionListener(e -> {
            // Open the AddEventModal dialog when clicked
            AddEventModel addEventModel = new AddEventModel(eventManager);
            addEventModel.setVisible(true);
        });
        controlPanel.add(addEventButton);

        // Add the control panel to the top of the main panel
        add(controlPanel, BorderLayout.NORTH);

        // Initialize the panel that displays the list of events
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        // Add a scroll pane to the display panel
        JScrollPane scrollPane = new JScrollPane(displayPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Initial population of the event list
        refreshDisplay();
    }

    /**
     * Method that refreshes the display panel by reloading the list of events.
     * Applies sorting and filtering based on user selections.
     */
    public void refreshDisplay() {
        // Remove all components from the display panel
        displayPanel.removeAll();

        // Get a copy of the current list of events
        List<Event> events = new ArrayList<>(eventManager.getEvents());

        // Apply sorting based on the selected option
        String selected = (String) sortDropDown.getSelectedItem();
        Comparator<Event> comparator = switch (Objects.requireNonNull(selected)) {
            case "Name Asc" -> Comparator.comparing(Event::getName);
            case "Name Desc" -> Comparator.comparing(Event::getName).reversed();
            case "Farthest First" -> Comparator.comparing(Event::getDateTime).reversed();
            default -> Comparator.comparing(Event::getDateTime); // "Closest First"
        };
        events.sort(comparator);

        // Iterate over the sorted list of events
        for (Event e : events) {
            // Apply filters to each event

            // Skip event if it's completed and the 'Hide Completed' filter is selected
            if (filterCompleted.isSelected() && e.isComplete()) {
                continue;
            }

            // Skip event if it's a Deadline and the 'Show Deadlines' filter is not selected
            if (!filterDeadlines.isSelected() && e instanceof Deadline) {
                continue;
            }

            // Skip event if it's a Meeting and the 'Show Meetings' filter is not selected
            if (!filterMeetings.isSelected() && e instanceof Meeting) {
                continue;
            }

            // Create an EventPanel for the event and add it to the display panel
            EventPanel eventPanel = new EventPanel(e, eventManager);
            displayPanel.add(eventPanel);
        }

        // Refresh the display panel to show the updated list
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    /**
     * Method that is called when the event list is updated.
     * Implements the EventManager.EventListener interface method.
     */
    @Override
    public void eventsUpdated() {
        // Refresh the display to reflect any changes in the event list
        refreshDisplay();
    }
}
