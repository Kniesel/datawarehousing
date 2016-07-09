package at.fhj.itm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Booking;
import at.fhj.itm.obj.Customer;




/**
 * Data Access Object for the class Customer 
 * Connects to the database to create, read, update and delete Customers.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class CustomerDAO extends GenericSqlDAO<Customer, Integer>{

	/**
	 * Writes a new Customer to the database.
	 * @return returns the ID of the new Customer
	 */
	@Override
	public Integer create(Customer newInstance) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("INSERT INTO CUSTOMER (ID, FIRSTNAME, SURNAME, EMAIL, DOB) VALUES (?, ?, ?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setString(2,newInstance.firstname);
			stmt.setString(3,newInstance.surname);
			stmt.setString(4, newInstance.email);
			stmt.setDate(5, newInstance.dob);
	        stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Customer could not be created");
		}     
		
		return newInstance.id;
	}

	
	/**
	 * Reads the Customer with the specified ID from the database.
	 */
	@Override
	public Customer read(Integer id) {
		PreparedStatement stmt;
		Customer e = new Customer();
		
		e.id = id;
		e.firstname = "unknown employee";
		e.surname = "unknown employee";
		e.email = "";
		e.dob = null;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE ID = ?");
			stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        rs.first();
	        
	        // Mapping
	        e.firstname = rs.getString("FIRSTNAME");
	        e.surname = rs.getString("SURNAME");
	        e.email = rs.getString("EMAIL");
	        e.dob = rs.getDate("DOB");
	 
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Customer " + id + " could not be read.");
		}     
		
		return e;
	}

	
	/**
	 * Updates a customer.
	 */
	@Override
	public void update(Customer transientObject) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("UPDATE CUSTOMER SET FIRSTNAME = ?, SURNAME = ?, EMAIL = ?, DOB = ? WHERE ID = ?");
			stmt.setString(1, transientObject.firstname);
			stmt.setString(2, transientObject.surname);
			stmt.setString(3, transientObject.email);
			stmt.setDate(4, transientObject.dob);
			stmt.setInt(5, transientObject.id);
	        int affectedRows = stmt.executeUpdate();
	        
	        if(affectedRows != 1)
	        	System.out.println("Something strange is going on: Customer not found or person not unique: " + transientObject);
	    } 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Customer could not be updated.");
		}     
	}

	/**
	 * Deletes an customer from the database.
	 */
	@Override
	public void delete(Customer persistentObject){
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("DELETE FROM CUSTOMER WHERE ID = ?");
			stmt.setInt(1, persistentObject.id);
	        int affectedRows = stmt.executeUpdate();
	        
	        if(affectedRows != 1)
	        	System.out.println("Something strange is going on: Customer not found or not unique: " + persistentObject);	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Customer could not be deleted.");
		}   
	}
	
	
	/**
	 * @return returns all available Customers
	 */
	public List<Customer> readAllCustomers(){
		List<Customer> ret = new ArrayList<Customer>();
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM CUSTOMER");
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next()){
	        	Customer e = new Customer();
	        	e.id = rs.getInt("ID");
	        	e.firstname = rs.getString("FIRSTNAME");
	        	e.surname = rs.getString("SURNAME");
	        	e.email = rs.getString("EMAIL");
	        	e.dob = rs.getDate("DOB");
	        	ret.add(e);
	        }
		} 
		catch (SQLException ex){
			ex.printStackTrace();
			System.err.println("Error while reading Customers.");
		}     
		return ret;
	}
	
	
	
	public int reserve(int seat, int screening){
		SeatDAO s = new SeatDAO();
		BookingDAO b = new BookingDAO();
		Booking booking = new Booking();
		
		int len = b.readAllBookings().size()+1;
			
		
	    if (s.isFree(seat, screening)) {
	     	//make booking
	      	booking.id = len;
	       	booking.seat = seat;
	       	booking.screening = screening;
	       	b.create(booking);
	       	System.out.println("Seat free");
		    return 1;
	    }
	   
	   System.out.println("Seat taken");     
	   return 0;
	}
}
