package javaGUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Employee extends JFrame {
    JLabel l1;
    JButton b1, b2, b3, b4;

    Employee() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        l1 = new JLabel("Employee View");
        l1.setFont(new Font("Times New Roman", Font.BOLD, 30));
        l1.setForeground(Color.BLUE);
        l1.setBounds(100, 10, 300, 30);
        add(l1);

        b1 = new JButton("Clock In");
        b2 = new JButton("Clock Out");
        b3 = new JButton("Log Out");

        b1.setBounds(100, 100, 100, 50);
        b2.setBounds(200, 100, 100, 50);
        b3.setBounds(150, 150, 100, 50);

        add(b1);
        add(b2);
        add(b3);

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Dispose of the current employee screen
                dispose();
                Login loginScreen = new Login(); // Create a new Login screen
                loginScreen.setBounds(400, 200, 400, 300);
                loginScreen.setVisible(true); // Show the login screen
            }
        });
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