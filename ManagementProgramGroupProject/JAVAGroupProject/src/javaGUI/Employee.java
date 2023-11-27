package javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.*;


class Employee extends JFrame {
    JLabel l1, clockLabel;
    JButton b1, b2, b3, b4;
    long startTime, endTime;
    boolean clockedIn; //Flag to track if clocked in
    String username; // Add a field to store the username

    Employee(String username) {
    	this.username = username;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        l1 = new JLabel("Employee View");
        l1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        l1.setForeground(Color.BLUE);
        l1.setBounds(100, 10, 300, 30);
        add(l1);
        
        
        
        clockLabel = new JLabel("00:00:00");
        clockLabel.setBounds(150,60,100,30);
        clockLabel.setVisible(false);
        add(clockLabel);

        b1 = new JButton("Clock In");
        b2 = new JButton("Clock Out");
        b3 = new JButton("Log Out");

        b1.setBounds(100, 100, 100, 50);
        b2.setBounds(200, 100, 100, 50);
        b3.setBounds(150, 150, 100, 50);

        add(b1);
        add(b2);
        add(b3);
        
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                startTime = System.currentTimeMillis();
                clockedIn = true; //Set clockedIn flag to true
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (clockedIn) {
                    endTime = System.currentTimeMillis();
                    updateClock(); // Update clock label only if clocked in
                    clockedIn = false; // Reset clockedIn flag

                    // Store elapsed time in the login.txt file
                    writeElapsedTimeToFile();
                }
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Dispose of the current employee screen
                dispose();
                Login loginScreen = new Login(); // Create a new Login screen
                loginScreen.setBounds(400, 200, 400, 300);
                loginScreen.setVisible(true); // Show the login screen
            }
        });
        
        //Initially hide the clock label
        clockLabel.setVisible(false);
    }
    
 // Method to write elapsed time to the login.txt file
    private void writeElapsedTimeToFile() {
        // Set the hourly rate
        double hourlyRate = 7.25;

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("login_temp.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into parts using tabs
                String[] parts = line.split("\t");

                // Check if the username matches
                if (parts.length > 0 && parts[0].equals(username)) {
                    // Check if the user is an employee
                    if (parts.length > 2 && "Employee".equals(parts[2])) {
                        String existingElapsedTime = parts.length > 3 ? parts[3] : "00:00:00";

                        // Calculate the total elapsed time
                        long existingSeconds = getSecondsFromTime(existingElapsedTime);
                        long newSeconds = getSecondsFromTime(clockLabel.getText());
                        long totalSeconds = existingSeconds + newSeconds;

                        // Calculate pay based on total seconds and hourly rate
                        double pay = totalSeconds * hourlyRate;
                        String totalElapsedTime = formatTimeFromSeconds(totalSeconds);

                        // Replace the existing elapsed time and pay
                        line = parts[0] + "\t" + parts[1] + "\t" + parts[2] + "\t" +
                                totalElapsedTime + "\t" + String.format("%.2f", pay);
                    }
                }

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temporary file to overwrite the original file
        File originalFile = new File("login.txt");
        File tempFile = new File("login_temp.txt");
        if (tempFile.renameTo(originalFile)) {
            System.out.println("Elapsed time and pay updated successfully.");
        } else {
            System.out.println("Failed to update elapsed time and pay.");
        }
    }

    // Helper method to convert time in "HH:mm:ss" format to seconds
    private long getSecondsFromTime(String time) {
        String[] parts = time.split(":");
        return Long.parseLong(parts[0]) * 3600 + Long.parseLong(parts[1]) * 60 + Long.parseLong(parts[2]);
    }

    // Helper method to format seconds to "HH:mm:ss" format
    private String formatTimeFromSeconds(long seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int remainingSeconds = (int) (seconds % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }



    
    // Method to update the clock label with elapsed time
    private void updateClock() {
        long elapsed = endTime - startTime;
        int hours = (int) (elapsed / 3600000);
        int minutes = (int) ((elapsed % 3600000) / 60000);
        int seconds = (int) ((elapsed % 60000) / 1000);

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        clockLabel.setText(time);
        clockLabel.setVisible(true);
    }
    
    
    Login loginScreen;
    Employee(Login loginScreen){
    	this.loginScreen = loginScreen;
    }
}

class SimpleEmployee {
    public static void main(String[] args) {
        Login loginScreen = new Login();
        loginScreen.setBounds(400, 200, 400, 300);
        loginScreen.setVisible(true);

        SignUp signUpScreen = new SignUp();
        signUpScreen.setBounds(400, 200, 400, 300);
        signUpScreen.setVisible(false); // Hide the sign-up screen initially

        Employee e = new Employee(loginScreen); // Pass the reference to the login screen
        e.setBounds(400, 200, 400, 300);
        e.setVisible(false); // Hide the employee screen initially
    }
}