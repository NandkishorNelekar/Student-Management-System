package student_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class student_application {
	static Scanner sc = new Scanner(System.in);
	
	void newStudentRegistration() throws ClassNotFoundException, SQLException {
		Connection con = database.getConnection(); 

		PreparedStatement ps = con.prepareStatement("insert into student(name,mobile_number,address,course,course_fee,fee_paid)values(?,?,?,?,?,?)");
		
		System.out.println("---- New Student Registration ------");
		
		System.out.print("Enter student name : ");
		String name = sc.nextLine();
		
		System.out.print("Enter student mobile number (10 digit) : ");
		long mobile_number = sc.nextLong(); 
		sc.nextLine();
		
		System.out.print("Enter student address : ");
		String address = sc.nextLine();
		
		System.out.print("Enter course : ");
		String course = sc.nextLine();
		
		System.out.print("Enter course fee : ");
		long course_fee = sc.nextLong(); 
		sc.nextLine();
		
		ps.setString(1, name);
		ps.setLong(2, mobile_number);
		ps.setString(3, address);
		ps.setString(4, course);
		ps.setLong(5, course_fee);
		ps.setLong(6, 0);
		
		ps.executeUpdate();
		
		System.out.println("Student registered successfully !!!");

		database.closeConnection(con);
	}
	
	void fee_paid() throws ClassNotFoundException, SQLException {
	    Connection con = database.getConnection();
	    
	    System.out.println("---- Course Fee payment  ----");
	    
	    System.out.print("Enter Student Roll Number : ");
	    int rollno = sc.nextInt();
	    sc.nextLine(); 

	    System.out.print("Enter course : ");
	    String course = sc.nextLine();

	    System.out.print("Enter the amount to pay : ");
	    long amount = sc.nextLong();
	    sc.nextLine(); 

	    PreparedStatement ps = con.prepareStatement("update student set fee_paid = fee_paid + ? where roll_no = ? and course=?");
	    
	    ps.setLong(1, amount);
	    ps.setInt(2, rollno);
	    ps.setString(3, course);
	    
	    int rowsUpdate = ps.executeUpdate();
	    if(rowsUpdate > 0) {
		    String show = "select course_fee, fee_paid from student where roll_no=?";
		    PreparedStatement ps2 = con.prepareStatement(show);
		    ps2.setInt(1, rollno);
		    
		    ResultSet rs = ps2.executeQuery();
	
		    if(rs.next()) {
		        long coursefee = rs.getLong("course_fee");
		        long totalFeePaid = rs.getLong("fee_paid");
		        long remaning_fee = coursefee - totalFeePaid;
		        
		        String updateRemainingFee = "update student set remaning_fee = ? where roll_no = ?";
		        PreparedStatement ps3 = con.prepareStatement(updateRemainingFee);
		        ps3.setLong(1, remaning_fee);
		        ps3.setInt(2, rollno);
		        ps3.executeUpdate();
		        
		        if(remaning_fee == 0 ) {
			    	System.out.println("full fee is paid successfully by roll number "+rollno);
			    }
		    }
	        System.out.println("Payment of " + amount + " paid successfully for Roll No. " + rollno);
	    }
	    else {
	        System.out.println("No student found with Roll Number " + rollno + " and Course " + course);
	    }
	    
	    
	    database.closeConnection(con);
	}
	
	void checkStudentDetails() throws ClassNotFoundException, SQLException {
		Connection con = database.getConnection();
		
		System.out.println("---- Check Student Details/Fee Status ----");
		
		System.out.println("Enter Student Roll Number : ");
		int rollno = sc.nextInt();		
		
		String show = "select * from student where roll_no = ?;";
		PreparedStatement ps = con.prepareStatement(show);
		ps.setInt(1, rollno);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			System.out.println("\n--- STUDENT RECORD ---");
			System.out.println("Roll No      : " + rs.getInt("roll_no"));
			System.out.println("Name         : " + rs.getString("name"));
			System.out.println("Mobileno     : " + rs.getString("mobile_number"));
			System.out.println("Course       : " + rs.getString("course"));
			System.out.println("Course fee   : " + rs.getLong("course_fee"));
			System.out.println("Fee Paid     : " + rs.getLong("fee_paid"));
			System.out.println("Remaning fee : " + rs.getLong("remaning_fee"));
			System.out.println("----------------------\n");
			
		} else {
			System.out.println("No student record found with Roll Number " + rollno);
		}
		
		database.closeConnection(con);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		student_application s = new student_application();
		
		while(true) {
	        System.out.println("\n-- Welcome to the Student Management System --");
	        System.out.println("----- Choose an option:  ------------");
	        System.out.println("1. New Student Registration");
	        System.out.println("2. Course Fee Payment");
	        System.out.println("3. Check Student Details/Fee Status");
	        System.out.println("4. Exit");
	        
	        System.out.print("Choice is: ");
	        int ch = sc.nextInt();
	        sc.nextLine();
	        
	            switch(ch) {
	            case 1:
	                s.newStudentRegistration();
	                break; 
	            case 2:
	                s.fee_paid();
	                break; 
	            case 3:
	                s.checkStudentDetails();
	                break; 
	            case 4: 
	                System.out.println("Thank you for using the Student Management System!");
	                sc.close(); 
	                System.exit(0);
	                break;
	            default:
	                System.out.println("Please enter a correct choice (1-4)!");
	            }
		}
	}
}
