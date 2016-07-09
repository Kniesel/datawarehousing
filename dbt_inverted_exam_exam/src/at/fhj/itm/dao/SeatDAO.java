package at.fhj.itm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Screening;
import at.fhj.itm.obj.Seat;
import at.fhj.itm.utils.CheckSeatInHall;

/**
 * Data Access Object for the class Seat 
 * Connects to the database to create, read, update and delete Seat.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class SeatDAO extends GenericSqlDAO<Seat, Integer> {
	
	/**
	 * Writes a new Seat to the database.
	 * @return returns the ID of the new Seat
	 */
	@Override
	public Integer create(Seat newInstance) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO Seat (ID, SEATNR, ROW, FK_HALL) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setInt(2,newInstance.seatnr);
			stmt.setInt(3, newInstance.row);
			stmt.setInt(4, newInstance.hall);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Seat could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the Seat with the specified ID from the database.
	 */
	@Override
	public Seat read(Integer id) {
		PreparedStatement stmt;
		Seat r = new Seat();

		r.id = -1;
		r.seatnr = -1;
		r.row = -1;
		r.hall = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM SEAT WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.seatnr = rs.getInt("SEATNR");
			r.row = rs.getInt("ROW");
			r.hall = rs.getInt("FK_HALL");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Seat " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates a Seat in the database.
	 */
	@Override
	public void update(Seat transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE SEAT SET SEATNR = ?, ROW = ?, FK_HALL = ? where ID = ?");
			stmt.setInt(1, transientObject.seatnr);
			stmt.setInt(2, transientObject.row);
			stmt.setInt(3, transientObject.hall);
			stmt.setInt(4, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Seat not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Seat could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a Seat from the database.
	 */
	@Override
	public void delete(Seat persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM Seat WHERE ID = ?");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1)
				System.out.println("Something strange is going on: Seat not found or not unique: " + persistentObject);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Seat could not be deleted");
		}   
	}


	/**
	 * @return returns all available Seats
	 */
	public List<Seat> readAllSeats() {
		List<Seat> ret = new ArrayList<Seat>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM SEAT");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Seat r = new Seat();
				r.seatnr = rs.getInt("SEATNR");
				r.hall = rs.getInt("FK_HALL");
				r.row = rs.getInt("ROW");
				r.id = rs.getInt("ID");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Seats.");
		}     
		return ret;
	}

	/**
	 * gets the seat by combination of hall, seatnr and row
	 * 
	 * @param hall that the Seat is located at
	 * @param seatnr seatnumber of the Seat
	 * @param row that the Seat is in
	 * @return returns a Seat object from the database
	 */
	public Integer getSeatID(int hall, int seatnr, int row) {
		PreparedStatement stmt;
		int id = -1;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM SEAT WHERE FK_HALL = ? AND SEATNR = ? AND ROW = ?");
			stmt.setInt(1, hall);
			stmt.setInt(2, seatnr);
			stmt.setInt(3, row);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			id = rs.getInt("ID");
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Seat could not be read.");
		}     
		return id;
	}


	/**
	 * checks if a seat at a certain screening is taken or not
	 * 
	 * @param seat id that is checked 
	 * @param screening id that is checked
	 * @return true if no booking exists, false if a booking already exists
	 */
	public boolean isFree(int seat, int screening) {
		PreparedStatement stmt;
		CheckSeatInHall c = new CheckSeatInHall();
		
		//check if seat is in hall beforehand
		if (c.seatInHall(seat, screening)) {
			
			// if seat is in hall
			System.out.println("Seat is in hall");
			
			// add new screening and seat
			ScreeningDAO scdao = new ScreeningDAO();
			Seat s = new Seat();
			
			// read from existing seats
			List<Seat> seats = readAllSeats();
			for (Seat se : seats){
				if(se.id == seat) {
					s = read(seat);
				}
			}
			
			Screening sc = scdao.read(screening);
			
			try {
				// check if a booking for a certain seat id with a certain creening id exists
				stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE FK_SEAT = ? AND FK_SCREENING = ?");
				stmt.setInt(1, s.id);
				stmt.setInt(2, sc.id);
				ResultSet rs = stmt.executeQuery();
				
				//if a booking exists return false
				if (rs.first()){
					System.out.println("Seat is not bookable");
					return false;
				}
				//if no booking for this seat at this screening exists, return true
				else {
					return true;
				}
			} 
			
			catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Booking failed.");
			}
			
		}
				
		return false;     
		
	}
}
