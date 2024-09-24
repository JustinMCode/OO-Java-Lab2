import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * CalendarDisplay is a JPanel that displays a calendar for a selected month.
 * It listens to event updates from the EventManager and refreshes the calendar accordingly.
 */
public class CalendarDisplay extends JPanel implements EventManager.EventListener {
    private final EventManager eventManager;            // Reference to the EventManager
    private final JComboBox<YearMonth> monthSelector;   // ComboBox to select the month
    private final JPanel calendarPanel;                 // Panel to display the calendar grid

     // Constructor
    public CalendarDisplay(EventManager eventManager) {
        this.eventManager = eventManager;

        // Register this CalendarDisplay as a listener to receive event updates
        this.eventManager.addListener(this);

        // Set the layout of this panel to BorderLayout
        setLayout(new BorderLayout());

        // Create the top panel containing the month selector
        JPanel topPanel = new JPanel();
        monthSelector = new JComboBox<>();

        // Get the current month
        YearMonth currentMonth = YearMonth.now();

        // Populate the month selector with months from -12 to +12 months relative to the current month
        for (int i = -12; i <= 12; i++) {
            YearMonth ym = currentMonth.plusMonths(i);
            monthSelector.addItem(ym);
        }

        // Set the selected month to the current month
        monthSelector.setSelectedItem(currentMonth);

        // Add an action listener to update the calendar when the selected month changes
        monthSelector.addActionListener(e -> updateCalendar());

        // Add the month selector to the top panel
        topPanel.add(monthSelector);

        // Add the top panel to the north region of the main panel
        add(topPanel, BorderLayout.NORTH);

        // Create the calendar panel to display the calendar grid
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7)); // 7 columns for the days of the week

        // Add the calendar panel to the center region of the main panel
        add(calendarPanel, BorderLayout.CENTER);

        // Initialize the calendar display
        updateCalendar();
    }

    // Function to update the calendar display based on the selected month and the events
    private void updateCalendar() {
        // Remove all components from the calendar panel
        calendarPanel.removeAll();

        // Add day-of-week headers to the calendar panel
        addDayOfWeekHeaders();

        // Get the selected month from the month selector
        YearMonth selectedMonth = (YearMonth) monthSelector.getSelectedItem();
        Objects.requireNonNull(selectedMonth, "Selected month cannot be null");

        // Get the first day of the selected month
        LocalDate firstOfMonth = selectedMonth.atDay(1);

        // Get the number of days in the selected month
        int daysInMonth = selectedMonth.lengthOfMonth();

        // Calculate the day of the week of the first day of the month (0=Sunday, 6=Saturday)
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Adjust to start from Sunday

        // Fill in blank days before the first of the month
        addEmptyLabels(firstDayOfWeek);

        // Get the list of events from the event manager
        List<Event> events = eventManager.getEvents();

        // Add day panels for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            // Create and add a day panel for this day
            LocalDate date = selectedMonth.atDay(day);
            JPanel dayPanel = createDayPanel(date, events);
            calendarPanel.add(dayPanel);
        }

        // Fill in the remaining slots to complete the grid (up to 6 weeks)
        fillRemainingSlots(firstDayOfWeek + daysInMonth);

        // Refresh the calendar panel
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // Function that adds the day-of-week headers to the calendar panel
    private void addDayOfWeekHeaders() {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : daysOfWeek) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            dayLabel.setBackground(new Color(173, 216, 230)); // Light blue background
            dayLabel.setOpaque(true);
            calendarPanel.add(dayLabel);
        }
    }

    // Function that adds empty labels to the calendar panel to represent blank days
    private void addEmptyLabels(int count) {
        for (int i = 0; i < count; i++) {
            calendarPanel.add(new JLabel("")); // Empty label for blank day
        }
    }

    // Function that creates a day panel for the given date, including any events on that day
    private JPanel createDayPanel(LocalDate date, List<Event> events) {
        // Create a panel for the day
        JPanel dayPanel = new JPanel();
        dayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        dayPanel.setLayout(new BorderLayout());

        // Set the background color
        setDayPanelBackground(dayPanel, date);

        // Add the day number label
        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Serif", Font.BOLD, 16));
        dayPanel.add(dayLabel, BorderLayout.NORTH);

        // Get the events on this day that are not completed
        List<Event> eventsOnThisDay = getEventsOnDate(date, events);

        // If there are events on this day, add them to the day panel
        if (!eventsOnThisDay.isEmpty()) {
            // Create a panel to list events
            JPanel eventsPanel = new JPanel();
            eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
            eventsPanel.setOpaque(false);

            for (Event event : eventsOnThisDay) {
                JLabel eventLabel = new JLabel("• " + event.getName());
                eventLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                eventsPanel.add(eventLabel);
            }

            dayPanel.add(eventsPanel, BorderLayout.CENTER);
        }

        return dayPanel;
    }

    // Function that sets the background color of the day panel based on the date
    private void setDayPanelBackground(JPanel dayPanel, LocalDate date) {
        if (date.equals(LocalDate.now())) {
            dayPanel.setBackground(Color.YELLOW);                        // Highlight today
        } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dayPanel.setBackground(new Color(255, 228, 196));   // Custom color for weekends
        } else {
            dayPanel.setBackground(new Color(240, 240, 240));   // Default color for weekdays
        }
    }

    // Function that retrieves the list of events occurring on the given date that are not completed.
    private List<Event> getEventsOnDate(LocalDate date, List<Event> events) {
        List<Event> eventsOnThisDay = new ArrayList<>();
        for (Event event : events) {
            if (!event.isComplete() && event.getDateTime().toLocalDate().equals(date)) {
                eventsOnThisDay.add(event);
            }
        }
        return eventsOnThisDay;
    }

    // Function to fill the remaining cells in the calendar grid with empty labels to complete the grid
    private void fillRemainingSlots(int cellsFilled) {
        int totalCells = 7 * 6; // 6 weeks
        for (int i = cellsFilled; i < totalCells; i++) {
            calendarPanel.add(new JLabel(""));
        }
    }

    // Function that is called when the events are updated in the EventManager.
    @Override
    public void eventsUpdated() {
        updateCalendar();
    }
}
