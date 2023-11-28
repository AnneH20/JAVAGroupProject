# Group 4's Group Project for Advanced Languages (BIS 3523) #

## To Use: ##
You must have all the requirements installed (especially if you are using the DATABASE version)

Open up XAMPP and make sure that mySQL **AND** Apache Web Server are **BOTH** running

Open up workspace in Eclipse and import project (either JAVAGroupProject or JAVAGroupProject-DATABASE)

If the mysql.jar file is not in the Referenced Libraries, add that by going to File -> Build Path -> (maybe Configure Build Path) --> Add External JARs

On a web browser: type in http://localhost/phpmyadmin/ to get to the web server where you can view your database

There should be a JAVA.sql file where you can import that directly into the web server

Run the program as normal

**NOTE: There may be unforeseen issues with this code**


## The Program ##
### Employee View: ###
* Clock In - starts a real-time timer (1 second = 1 hour)
* Clock Out - stops real-time timer (stores in login.txt file OR JAVA DB table USERS)
* Log Out - logs out of account


### Employer View: ###
* Hire - adds new Employee/Employer to login.txt/JAVA DB table USERS (username, password, user type, hourly rate)
* Fire - deletes Employee/Employer from login.txt
* Payroll - displays all payroll information (user, user type, hourly rate, hours worked, total pay)
* Log Out - logs out of account


## Requirements: ##
1. XAMPP
2. mysql-connector (mysql-connector-j-8.1.0.jar included)
3. JAVA.sql (database included??)
4. Eclipse
