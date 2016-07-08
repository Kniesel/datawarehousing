package at.fhj.itm.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Booking;

/**
 * Data Access Object for the class Booking 
 * Connects to the database to create, read, update and delete Bookings.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class BookingDAO extends GenericSqlDAO<Booking, Integer>{

	/**
	 * Writes a new Booking to the database.
	 * @return returns the ID of the new Booking
	 */
	@Override
	public Integer create(Booking newInstance) {
		PreparedStatement stmt;
		System.out.println(newInstance.id + "   " + newInstance.seat + "   " + newInstance.screening);
		try {
			stmt = conn.prepareStatement("INSERT INTO Booking (ID, FK_SCREENING, FK_SEAT) VALUES(?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setInt(2,newInstance.screening);
			stmt.setInt(3, newInstance.seat);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Booking could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the Booking with the specified ID from the database.
	 */
	@Override
	public Booking read(Integer id) {
		PreparedStatement stmt;
		Booking r = new Booking();

		r.id = -1;
		r.screening = -1;
		r.seat = -1;


		try {
			stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.screening = rs.getInt("FK_SCREENING");
			r.seat = rs.getInt("FK_SEAT");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Booking " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates a Booking in the database.
	 */
	@Override
	public void update(Booking transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE BOOKING SET FK_SCREENING = ?, FK_SEAT = ? where ID = ?");
			stmt.setInt(1, transientObject.screening);
			stmt.setInt(2, transientObject.seat);
			stmt.setInt(3, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Booking not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Booking could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a Booking from the database.
	 */
	@Override
	public void delete(Booking persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM Booking WHERE ID = ? ");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1)
				System.out.println("Something strange is going on: Booking not found or not unique: " + persistentObject);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Booking could not be deleted");
		}   
	}


	/**
	 * @return returns all available Bookings
	 */
	public List<Booking> readAllBookings() {
		List<Booking> ret = new ArrayList<Booking>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM BOOKING");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Booking r = new Booking();
				r.screening = rs.getInt("FK_SCREENING");
				r.seat = rs.getInt("FK_SEAT");
				r.id = rs.getInt("ID");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Bookings.");
		}     
		return ret;
	}

	/**
	 * gets the room by combination of site and screening
	 * @param screening screening of the Booking
	 * @param seat seat of the Booking
	 * @return returns a screening object from the database
	 */
	public Booking getBooking(int screening, int seat) {
		PreparedStatement stmt;
		Booking r = new Booking();

		r.id = -1;
		r.screening = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE FK_HALL = ? AND FK_SCREENING = ? AND FK_SEAT = ?");
			stmt.setInt(1, screening);
			stmt.setInt(2, seat);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			// Mapping
			r.id = rs.getInt("ID");
			r.screening = rs.getInt("FK_SCREENING");
			r.seat = rs.getInt("FK_SEAT");
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Booking could not be read.");
		}     
		return r;
	}
}
