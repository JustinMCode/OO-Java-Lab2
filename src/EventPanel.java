import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventPanel extends JPanel {
    private final Event event;
    private JButton completeButton;
    private final JLabel statusLabel;

    public EventPanel(Event event, EventManager eventManager) {
        this.event = event;

        // Use horizontal BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 5));

        // Define a smaller font
        Font smallFont = new Font("Arial", Font.PLAIN, 15);

        // Display event name
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(smallFont);

        // Display event time with 12-hour format
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        JLabel timeLabel = new JLabel(event.getDateTime().format(displayFormatter));
        timeLabel.setFont(smallFont);

        // Create labels panel
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setOpaque(false);

        labelsPanel.add(nameLabel);
        labelsPanel.add(timeLabel);

        // If event is a Meeting, display additional details
        if (event instanceof Meeting meeting) {
            JLabel durationLabel = new JLabel("Duration: " + meeting.getDuration().toMinutes() + " minutes");
            durationLabel.setFont(smallFont);
            labelsPanel.add(durationLabel);

            JLabel locationLabel = new JLabel("Location: " + meeting.getLocation());
            locationLabel.setFont(smallFont);
            labelsPanel.add(locationLabel);
        }

        add(labelsPanel);

        // Add horizontal glue to push status and button to the right
        add(Box.createHorizontalGlue());

        // Display status
        statusLabel = new JLabel(event.isComplete() ? "Completed" : null);
        statusLabel.setFont(smallFont);
        add(statusLabel);

        // Add 'Complete' button if the event is not yet complete
        if (!event.isComplete()) {
            completeButton = new JButton("Complete");
            completeButton.setFont(smallFont);
            completeButton.setMargin(new Insets(2, 4, 2, 4));
            completeButton.addActionListener(e -> {
                event.complete();
                statusLabel.setText("Complete");
                completeButton.setEnabled(false);
                eventManager.notifyListeners();
            });
            add(completeButton);
        }

        // Set maximum size
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Update panel color based on urgency
        updateUrgency();
    }

    // Function that sets the background of Event depending on urgency
    public void updateUrgency() {
        LocalDateTime now = LocalDateTime.now();
        if (event.getDateTime().isBefore(now)) {
            setBackground(Color.RED); // Overdue
        } else if (event.getDateTime().isBefore(now.plusHours(1))) {
            setBackground(Color.YELLOW); // Imminent
        } else {
            setBackground(Color.GREEN); // Distant
        }
    }
}
