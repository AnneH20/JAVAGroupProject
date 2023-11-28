package javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class SignUp extends JFrame {
    JTextField t1, t2; // Add a text field for pay rate
    JButton b1;
    JLabel l1;
    JRadioButton employeeRadioButton, employerRadioButton;
    ButtonGroup radioGroup;

    SignUp() {
        setLayout(null);
        setSize(400, 300);

        l1 = new JLabel("Hire");
        l1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        l1.setForeground(Color.BLUE);
        l1.setBounds(225, 10, 300, 40);
        add(l1);

        t1 = new JTextField(60);
        t2 = new JPasswordField(60);
        //t3 = new JTextField(60); // Add a text field for pay rate
        b1 = new JButton("Submit");

        t1.setBounds(190, 60, 120, 30);
        t2.setBounds(190, 100, 120, 30);
        //t3.setBounds(190, 140, 120, 30); // Set the position for pay rate field
        b1.setBounds(210, 180, 80, 30);

        // Create radio buttons and a button group
        employeeRadioButton = new JRadioButton("Employee");
        employerRadioButton = new JRadioButton("Employer");
        radioGroup = new ButtonGroup();

        // Set the default selection to "Employee"
        employeeRadioButton.setSelected(true);

        // Set the positions of radio buttons
        employeeRadioButton.setBounds(150, 210, 100, 30);
        employerRadioButton.setBounds(250, 210, 100, 30);

        // Add radio buttons to the button group
        radioGroup.add(employeeRadioButton);
        radioGroup.add(employerRadioButton);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    // Database?? DBConnection con = new DBConnection();

                    FileWriter fw = new FileWriter("login.txt", true);
                    fw.write(t1.getText() + "\t" + t2.getText());

                    // Check which radio button is selected and write to the same line
                    if (employeeRadioButton.isSelected()) {
                        double hourlyRate = Double.parseDouble(JOptionPane.showInputDialog("Enter hourly rate:"));
                        fw.write("\tEmployee");
                        fw.write("\t" + hourlyRate + "\n");
                    } else if (employerRadioButton.isSelected()) {
                        fw.write("\tEmployer\n");
                    }

                    fw.close();
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Employee Hired");
                    dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        add(t1);
        add(t2);
        add(b1);
        add(employeeRadioButton);
        add(employerRadioButton);
    }
}


class Employer extends JFrame{
	JLabel l1;

	JButton b1, b2, b3, b4;
	Employer(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		l1 = new JLabel("Employer View");
		l1.setFont(new Font("Times New Roman",Font.BOLD,30));
		l1.setForeground(Color.BLUE);
		l1.setBounds(100,10,300,30);
		add(l1);
		
		
		b1 = new JButton("Hire");
		b2 = new JButton("Fire");
		b3 = new JButton("Payroll");
		b4 = new JButton("Log Out");

		
		b1.setBounds(100,100,100,50);
		b2.setBounds(200,100,100,50);
		b3.setBounds(100,150,100,50);
		b4.setBounds(200,150,100,50);
		
		
		add(b1);
		add(b2);
		add(b3);
		add(b4);
		
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SignUp s = new SignUp();
                s.setVisible(true);
                s.setBounds(200, 200, 500, 300);
            }
        });
        
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Code to remove employee from login.txt
                removeEmployee();
            }
        });
        
        b3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		displayInfo();
        	}
        });
		
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Dispose of the current employee screen
                dispose();
                Login loginScreen = new Login(); // Create a new Login screen
                loginScreen.setBounds(400, 200, 400, 300);
                loginScreen.setVisible(true); // Show the login screen
            }
        });
	}
	
	 private void removeEmployee() {
	        String employeeToRemove = JOptionPane.showInputDialog("Enter the employee to be removed:");
	        try {
	            File inputFile = new File("login.txt");
	            File tempFile = new File("temp.txt");

	            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

	            String lineToRemove = employeeToRemove.trim(); // Adjust as per your file format/employee info

	            String currentLine;
	            while ((currentLine = reader.readLine()) != null) {
	                // If the line does not contain the employee to be removed, write it to temp file
	                if (!currentLine.trim().startsWith(lineToRemove)) {
	                    writer.write(currentLine + System.getProperty("line.separator"));
	                }
	            }

	            writer.close();
	            reader.close();

	            // Delete the original file
	            if (inputFile.delete()) {
	                // Rename the temp file to the original file name
	                if (!tempFile.renameTo(inputFile)) {
	                    System.out.println("Could not rename the file");
	                }
	            } else {
	                System.out.println("Could not delete the file");
	            }

	            JFrame f = new JFrame();
	            JOptionPane.showMessageDialog(f, "Employee Fired");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	 
	 private void displayInfo() {
		    try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
		        StringBuilder employeeInfo = new StringBuilder();

		        String line;
		        while ((line = reader.readLine()) != null) {
		            String[] parts = line.split("\t");

		            // Check if the array has at least 4 elements before accessing indices
		            if (parts.length >= 4 && "Employee".equals(parts[2])) {
		                employeeInfo.append("Username: ").append(parts[0]).append("\n");
		                employeeInfo.append("Role: ").append(parts[2]).append("\n");
		                employeeInfo.append("Hourly Rate: ").append(parts[3]).append("\n");
		            }

		            // Check if parts[4] and parts[5] exist before appending
		            if (parts.length >= 6) {
		                employeeInfo.append("Hours: ").append(parts[4]).append("\n");
		                employeeInfo.append("Total Pay: ").append(parts[5]).append("\n");
		            }

		            employeeInfo.append("\n"); // Add a separator between entries
		        }

		        // Display the information in a dialog
		        JFrame infoFrame = new JFrame("Employee Information");
		        JOptionPane.showMessageDialog(infoFrame, employeeInfo.toString(), "Employee Info", JOptionPane.INFORMATION_MESSAGE);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


	
    Login loginScreen;
    Employer(Login loginScreen){
    	this.loginScreen = loginScreen;
    }
}

class SimpleEmployer {
    public static void main(String[] args) {
        Login loginScreen = new Login();
        loginScreen.setBounds(400, 200, 400, 300);
        loginScreen.setVisible(true);

        SignUp signUpScreen = new SignUp();
        signUpScreen.setBounds(400, 200, 400, 300);
        signUpScreen.setVisible(false); // Hide the sign-up screen initially

        Employer e = new Employer(loginScreen); // Pass the reference to the login screen
        e.setBounds(400, 200, 400, 300);
        e.setVisible(false); // Hide the employee screen initially
    }
}
