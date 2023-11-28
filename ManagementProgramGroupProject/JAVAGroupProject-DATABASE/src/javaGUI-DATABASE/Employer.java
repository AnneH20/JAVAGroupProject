package javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

class DBConnection{
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	DBConnection(){
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/JAVA", "root", ""); // may need to be changed depending on user
		System.out.println("Connection Established...!");
		}catch(Exception e) {
			System.out.println("JDBC not register");
			return;
		}
				
	}
	
	boolean search(String unm, String pwd, String usrtype) throws Exception {
	    try {
	        ps = con.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ? AND USERTYPE = ?");
	        ps.setString(1, unm);
	        ps.setString(2, pwd);
	        ps.setString(3, usrtype);

	        rs = ps.executeQuery();
	        return rs.next();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // rethrow the exception
	    }
	}
	
	void insert(String unm, String pwd, String usrtype, Double hrpay) throws Exception {
	    try {
	        // Round off hourly rate to 2 decimal places
	        double roundedHourlyRate = Math.round(hrpay * 100.0) / 100.0;

	        ps = con.prepareStatement("INSERT INTO USERS(USERNAME,PASSWORD,USERTYPE,HOURLYRATE) VALUES(?,?,?,?)");
	        ps.setString(1, unm);
	        ps.setString(2, pwd);
	        ps.setString(3, usrtype);
	        ps.setDouble(4, roundedHourlyRate);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // rethrow the exception
	    }
	}
	
	void deleteUser(String username) throws Exception {
        ps = con.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?");
        ps.setString(1, username);
        ps.executeUpdate();
    }
	
	ResultSet executeQuery(String query) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeQuery(query);
    }
	
	void updateDB(String username, long elapsedSeconds, double pay) throws SQLException {
	    try {
	        // Round off total pay to 2 decimal places
	        double roundedTotalPay = Math.round(pay * 100.0) / 100.0;

	        ps = con.prepareStatement("UPDATE USERS SET HOURSWORKED = ?, TOTALPAY = ? WHERE USERNAME = ?");
	        ps.setLong(1, elapsedSeconds);
	        ps.setDouble(2, roundedTotalPay);
	        ps.setString(3, username);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	}

	
	public double getHourlyRate(String username) throws SQLException {
	    double hourlyRate = 0.0;

	    try {
	        String query = "SELECT HOURLYRATE FROM USERS WHERE USERNAME=?";
	        ps = con.prepareStatement(query);
	        ps.setString(1, username);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            hourlyRate = rs.getDouble("HOURLYRATE");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        // Close resources, e.g., result set, statement, etc.
	        closeResources();
	    }

	    return hourlyRate;
	}

	// Modify your DBConnection class to include the closeResources method
	private void closeResources() {
	    try {
	        if (ps != null) {
	            ps.close();
	        }
	        if (rs != null) {
	            rs.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}

class SignUp extends JFrame {
    JTextField t1, t2, t3; // Add a text field for pay rate
    JButton b1;
    JLabel l1, userLabel, passwordLabel, hourlyRateLabel, noteLabel; // Add labels
    JRadioButton employeeRadioButton, employerRadioButton;
    ButtonGroup radioGroup;

    SignUp() {
        setLayout(new BorderLayout()); // Use BorderLayout

        JPanel formPanel = new JPanel();
        formPanel.setLayout(null); // Use null layout for the form panel
        formPanel.setPreferredSize(new Dimension(400, 200)); // Set preferred size

        l1 = new JLabel("Hire");
        l1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        l1.setForeground(Color.BLUE);
        l1.setBounds(225, 10, 300, 40);
        formPanel.add(l1);

        userLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        hourlyRateLabel = new JLabel("Hourly Rate:"); // Add label for hourly rate

        t1 = new JTextField(60);
        t2 = new JPasswordField(60);
        t3 = new JTextField(60); // Add a text field for pay rate
        b1 = new JButton("Submit");

        userLabel.setBounds(60, 60, 80, 30);
        passwordLabel.setBounds(60, 100, 80, 30);
        hourlyRateLabel.setBounds(60, 140, 80, 30); // Set the position for hourly rate label

        t1.setBounds(190, 60, 120, 30);
        t2.setBounds(190, 100, 120, 30);
        t3.setBounds(190, 140, 120, 30); // Set the position for pay rate field
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
                	DBConnection con = new DBConnection();
                	String userType = employeeRadioButton.isSelected() ? "Employee" : "Employer";
                	double hourlyRate = Double.parseDouble(t3.getText());

                    con.insert(t1.getText().toString(), t2.getText().toString(), userType, hourlyRate);
                    
                    /*
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
                    */
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Employee Hired");
                    dispose();
   
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        
        formPanel.add(userLabel);
        formPanel.add(passwordLabel);
        formPanel.add(hourlyRateLabel); // Add hourly rate label
        formPanel.add(t1);
        formPanel.add(t2);
        formPanel.add(t3);
        formPanel.add(b1);
        formPanel.add(employeeRadioButton);
        formPanel.add(employerRadioButton);

        // Add the form panel to the content pane
        add(formPanel, BorderLayout.CENTER);

        // Create and add the note label to the bottom
        noteLabel = new JLabel("Note: If hiring an Employer, put 0.00 for the hourly rate");
        noteLabel.setHorizontalAlignment(JLabel.CENTER);
        add(noteLabel, BorderLayout.SOUTH);
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
	        String employeeToRemove = JOptionPane.showInputDialog("Enter the employee username to be removed:");
	        try {
	        	DBConnection con = new DBConnection();
	            con.deleteUser(employeeToRemove);
	        	
	            /*
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
	            */

	            JFrame f = new JFrame();
	            JOptionPane.showMessageDialog(f, "Employee Fired");
	           
	        } 
	        
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	 
	 private void displayInfo() {
		    try {
		        DBConnection con = new DBConnection();
		        ResultSet rs = con.executeQuery("SELECT * FROM USERS WHERE USERTYPE = 'Employee'");

		        StringBuilder employeeInfo = new StringBuilder();

		        while (rs.next()) {
		            String username = rs.getString("USERNAME");
		            String role = rs.getString("USERTYPE");
		            Double hourlyRate = rs.getDouble("HOURLYRATE");
		            Double hours = rs.getDouble("HOURSWORKED");
		            Double totalPay = rs.getDouble("TOTALPAY");

		            employeeInfo.append("Username: ").append(username).append("\n");
		            //employeeInfo.append("User Type: ").append(role).append("\n");

		            if (hourlyRate != 0) {
		                employeeInfo.append("Hourly Rate: $").append(hourlyRate).append("\n");
		            }

		            if (hours != 0) {
		                employeeInfo.append("Hours Worked: ").append(hours).append("\n");
		            }

		            if (totalPay != 0) {
		                employeeInfo.append("Total Pay: $").append(totalPay).append("\n");
		            }

		            employeeInfo.append("\n"); // Add a separator between entries
		        }

		        // Display the information in a dialog
		        JFrame infoFrame = new JFrame("Employee Information");
		        JOptionPane.showMessageDialog(infoFrame, employeeInfo.toString(), "Employee Info", JOptionPane.INFORMATION_MESSAGE);

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	 
	 /*
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
*/
	
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