package javaGUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;



class Login extends JFrame {
    JTextField t1, t2;
    JButton b1, b2;
    JLabel l1, l2, userLabel, passwordLabel;
    JRadioButton employeeRadioButton, employerRadioButton;
    ButtonGroup radioGroup;

    Login() {
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        l1 = new JLabel("Login");
        l1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        l1.setForeground(Color.BLUE);
        l1.setBounds(160, 10, 300, 40);
        add(l1);

        userLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        t1 = new JTextField(60);
        t2 = new JPasswordField(60);
        b1 = new JButton("Sign In");

        userLabel.setBounds(60, 60, 80, 30);
        passwordLabel.setBounds(60, 100, 80, 30);

        t1.setBounds(140, 60, 120, 30);
        t2.setBounds(140, 100, 120, 30);
        b1.setBounds(155, 140, 80, 30);

        l2 = new JLabel("");
        l2.setBounds(150, 230, 300, 30);
        add(l2);

        employeeRadioButton = new JRadioButton("Employee");
        employerRadioButton = new JRadioButton("Employer");
        radioGroup = new ButtonGroup();

        employeeRadioButton.setSelected(true);

        employeeRadioButton.setBounds(100, 210, 100, 30);
        employerRadioButton.setBounds(200, 210, 100, 30);

        radioGroup.add(employeeRadioButton);
        radioGroup.add(employerRadioButton);

        add(userLabel);
        add(passwordLabel);
        add(t1);
        add(t2);
        add(b1);
        add(employeeRadioButton);
        add(employerRadioButton);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                boolean match = false;

                String username = t1.getText().toString();
                String password = t2.getText().toString();
                String userType = "";

                if (employeeRadioButton.isSelected()) {
                    userType = "Employee";
                } else if (employerRadioButton.isSelected()) {
                    userType = "Employer";
                }

                try {
                    DBConnection con = new DBConnection();
                    if (con.search(username, password, userType))
                    	match = true;
                	
                	/*
                	FileReader fr = new FileReader("login.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        // Split the line into parts using tabs
                        String[] parts = line.split("\t");

                        // Check if the username and password match, and user type is correct
                        if (parts.length > 0 && parts[0].equals(username)
                                && parts.length > 1 && parts[1].equals(password)
                                && parts.length > 2 && parts[2].equals(userType)) {
                            match = true;
                            break;
                        } else if (line.startsWith(username + "\t") && line.endsWith("\t" + userType)) {
                            invalidUsername = false;
                            invalidPassword = false;
                        } else if (line.startsWith(username + "\t")) {
                            invalidUsername = false;
                        } else if (line.endsWith("\t" + userType)) {
                            invalidPassword = false;
                        }
                    }
                    fr.close();
                    */
                } 
                    catch (Exception e) {}
                
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                System.out.println("UserType: " + userType);
                System.out.println("Match: " + match);

                if (match) {
                    if ("Employee".equals(userType)) {
                        // Open Employee page
                        dispose();
                        Employee e = new Employee(username); // Pass the username
                        e.setBounds(400, 200, 400, 300);
                        e.setVisible(true);
                    } else if ("Employer".equals(userType)) {
                        // Open Employer page
                        dispose();
                        Employer e = new Employer();
                        e.setBounds(400, 200, 400, 300);
                        e.setVisible(true);
                    }
                } else {
                	l2.setText("Invalid credentials");
                }             

            }
            
        });
    }
}

class LoginScreen {
    public static void main(String[] args) {
        Login loginScreen = new Login();
        loginScreen.setBounds(400, 200, 400, 300);
        loginScreen.setVisible(true);
    }
}

