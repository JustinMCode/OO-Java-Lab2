import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

// AddEventModal is a dialog window that allows users to add new events
public class AddEventModel extends JDialog {
    private final EventManager eventManager;      // Reference to the EventManager
    private final JTextField nameField;           // TextField for event name
    private final JTextField startDateTimeField;  // TextField for start date and time
    private final JTextField endDateTimeField;    // TextField for end date and time (Meetings only)
    private final JTextField locationField;       // TextField for location (Meetings only)
    private final JComboBox<String> eventTypeComboBox; // ComboBox to select event type

    // DateTimeFormatter for parsing date and time input
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    // Constructor: Constructs the AddEventModal dialog.
    public AddEventModel(EventManager eventManager) {
        this.eventManager = eventManager;
        setTitle("Add Event");
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Set layout with a GridBagLayout for better control over component placement
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize components
        eventTypeComboBox = createEventTypeComboBox();
        nameField = new JTextField();
        startDateTimeField = new JTextField();
        endDateTimeField = new JTextField();
        locationField = new JTextField();
        JButton addButton = createAddButton();

        // Add components to the dialog
        int row = 0;
        addLabelAndComponent("Event Type:", eventTypeComboBox, gbc, row++);
        addLabelAndComponent("Name:", nameField, gbc, row++);
        addLabelAndComponent("Start Date (yyyy-MM-dd hh:mm AM/PM):", startDateTimeField, gbc, row++);
        addLabelAndComponent("End Date (yyyy-MM-dd hh:mm AM/PM):", endDateTimeField, gbc, row++);
        addLabelAndComponent("Location:", locationField, gbc, row++);

        // Add an empty label for alignment and the Add button
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(new JLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(addButton, gbc);

        // Initialize the state of fields based on the selected event type
        toggleFields();
    }

    // Function that creates and returns the event type combo box
    private JComboBox<String> createEventTypeComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Deadline", "Meeting"});
        comboBox.addActionListener(e -> toggleFields());
        return comboBox;
    }

    // Function that creates and returns the Add button
    private JButton createAddButton() {
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addEvent());
        return addButton;
    }

    // Function that adds a label and a component to the dialog using GridBagLayout
    private void addLabelAndComponent(String labelText, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        add(component, gbc);
    }

    // Function that toggles the visibility and enabled state of fields based on the selected event type
    private void toggleFields() {
        boolean isMeeting = Objects.equals(eventTypeComboBox.getSelectedItem(), "Meeting");
        endDateTimeField.setEnabled(isMeeting);
        locationField.setEnabled(isMeeting);
    }

    // Function that adds the event based on the input fields when the Add button is clicked
    private void addEvent() {
        String name = nameField.getText().trim();
        String startDateTimeStr = startDateTimeField.getText().trim();

        // Validate event name
        if (name.isEmpty()) {
            showErrorDialog("Please enter an event name.");
            return;
        }

        // Parse start date and time
        LocalDateTime startDateTime = parseDateTime(startDateTimeStr, "start");
        if (startDateTime == null) {
            return; // Error message already shown in parseDateTime()
        }

        String selectedEventType = (String) eventTypeComboBox.getSelectedItem();
        Objects.requireNonNull(selectedEventType, "Event type cannot be null");

        if (selectedEventType.equals("Deadline")) {
            // Create and add a Deadline event
            Deadline deadline = new Deadline(name, startDateTime);
            eventManager.addEvent(deadline);
        } else {
            // Create and add a Meeting event
            if (!validateMeetingFields()) {
                return; // Error message already shown in validateMeetingFields()
            }

            String endDateTimeStr = endDateTimeField.getText().trim();
            LocalDateTime endDateTime = parseDateTime(endDateTimeStr, "end");
            if (endDateTime == null) {
                return; // Error message already shown in parseDateTime()
            }

            String location = locationField.getText().trim();
            Meeting meeting = new Meeting(name, startDateTime, endDateTime, location);
            eventManager.addEvent(meeting);
        }

        // Close the dialog after adding the event
        dispose();
    }

    // Function that validates that the necessary fields for a Meeting are filled.
    private boolean validateMeetingFields() {
        if (endDateTimeField.getText().trim().isEmpty()) {
            showErrorDialog("Please enter an end date and time for the meeting.");
            return false;
        }
        if (locationField.getText().trim().isEmpty()) {
            showErrorDialog("Please enter a location for the meeting.");
            return false;
        }
        return true;
    }

    // Function that parses a date and time string into a LocalDateTime object.
    private LocalDateTime parseDateTime(String dateTimeStr, String fieldLabel) {
        try {
            return LocalDateTime.parse(dateTimeStr, FORMATTER);
        } catch (DateTimeParseException ex) {
            showErrorDialog("Invalid " + fieldLabel + " date and time format. Use yyyy-MM-dd hh:mm AM/PM.");
            return null;
        }
    }

    // Function that shows an error dialog with the given message.
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
