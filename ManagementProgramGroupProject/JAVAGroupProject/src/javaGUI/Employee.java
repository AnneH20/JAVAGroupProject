package javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.*;
import java.util.Arrays;


class Employee extends JFrame {
    JLabel l1, clockLabel;
    JButton b1, b2, b3, b4;
    long startTime, endTime;
    boolean clockedIn; //Flag to track if clocked in
    String username; // Add a field to store the username

    Employee(String username) {
    	this.username = username;
    	//this.payRate = payRate;
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
    
    
    private double readPayRateFromFile() {
        double payRate = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");

                // Check if the username matches and it is an employee or employer
                if (parts.length > 0 && parts[0].equals(username)) {
                    int payRateIndex = findPayRateIndex(parts);
                    if (payRateIndex != -1) {
                        payRate = Double.parseDouble(parts[payRateIndex]);
                        break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        System.out.println("readPayRateFromFile good");
        return payRate;
    }

    // Helper method to find the index where the pay rate is stored, considering possible variations
    private int findPayRateIndex(String[] parts) {
        for (int i = 3; i < parts.length; i++) {
            if (isNumeric(parts[i])) {
                return i;
            }
        }
        return -1;
    }
    
    private void writeElapsedTimeToFile() {
        double hourlyRate = readPayRateFromFile();
        String tempFileName = "login_temp.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                System.out.println("Processing line: " + Arrays.toString(parts));
                System.out.println("Line: " + line);
                System.out.println("Parts length: " + parts.length);
                
                if (parts.length < 7) {
                    // Add empty elements to meet the condition
                    parts = Arrays.copyOf(parts, 7);
                    Arrays.fill(parts, 4, parts.length, "");  // Fill the added elements with empty strings
                }

                if (parts.length >= 6 && "Employee".equals(parts[2]) && username.equals(parts[0])) {
	            	System.out.println("Found matching Employee: " + parts[0]);
	                System.out.println("Existing UserType: " + parts[2]);
	                System.out.println("Expected UserType: Employee");
	                System.out.println("Existing Username: " + parts[0]);
	                System.out.println("Expected Username: " + username);
                	String existingElapsedTime = parts[4];

                    System.out.println("Existing Elapsed Time before update: " + existingElapsedTime);

                    long existingSeconds = getSecondsFromTime(existingElapsedTime);
                    long newSeconds = getSecondsFromTime(clockLabel.getText());
                    long totalSeconds = existingSeconds + newSeconds;

                    double pay = (double) totalSeconds / 3600 * hourlyRate * 3600;
                    String totalElapsedTime = formatTimeFromSeconds(totalSeconds);

                    parts[4] = totalElapsedTime;
                    parts[5] = String.format("%.2f", pay);

                    String modifiedLine = String.join("\t", parts);
                    System.out.println("Modified Line: " + modifiedLine);
                    writer.write(modifiedLine);
                }

                else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        File originalFile = new File("login.txt");
        File tempFile = new File(tempFileName);
        if (tempFile.renameTo(originalFile)) {
            System.out.println("Elapsed time and pay updated successfully.");
        } else {
            System.out.println("Failed to update elapsed time and pay.");
        }
    }


    // Helper method to check if a string represents a valid time in "HH:mm:ss" format
    private boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}:\\d{2}");
    }



    
 // Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




 // Helper method to convert time in "HH:mm:ss" format to seconds
    private long getSecondsFromTime(String time) {
        try {
            String[] parts = time.split(":");
            if (parts.length == 3) {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                
                return hours * 3600 + minutes * 60 + seconds;
            } 
            
            else {
                // Handle invalid time format
                //System.err.println("Invalid time format: " + time);
                return 0;
            }
            
        } catch (NumberFormatException e) {
            // Handle the exception gracefully, return 0 if parsing fails
            System.err.println("Error parsing time: " + time);
            return 0;
        }
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