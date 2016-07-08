package at.fhj.itm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Hall;

/**
 * Data Access Object for the class Hall 
 * Connects to the database to create, read, update and delete Halls.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class HallDAO extends GenericSqlDAO<Hall, Integer> {
	
	/**
	 * Writes a new Hall to the database.
	 * @return returns the ID of the new Hall
	 */
	@Override
	public Integer create(Hall newInstance) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO HALL (ID, NAME, NUM_OF_SEATS, FK_CINEMA) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setString(2,newInstance.name);
			stmt.setInt(3, newInstance.num_of_seats);
			stmt.setInt(4, newInstance.cinema);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hall could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the hall with the specified ID from the database.
	 */
	@Override
	public Hall read(Integer id) {
		PreparedStatement stmt;
		Hall r = new Hall();

		r.id = -1;
		r.name = "unknown hall";
		r.num_of_seats = -1;
		r.cinema = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM HALL WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.name = rs.getString("NAME");
			r.num_of_seats = rs.getInt("NUM_OF_SEATS");
			r.cinema = rs.getInt("FK_CINEMA");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hall " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates a hall in the database.
	 */
	@Override
	public void update(Hall transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE HALL SET NAME = ?, NUM_OF_SEATS = ?, FK_CINEMA = ? where ID = ?");
			stmt.setString(1, transientObject.name);
			stmt.setInt(2, transientObject.num_of_seats);
			stmt.setInt(3, transientObject.cinema);
			stmt.setInt(4, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Hall not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hall could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a hall from the database.
	 */
	@Override
	public void delete(Hall persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM HALL WHERE ID = ?");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1)
				System.out.println("Something strange is going on: Hall not found or not unique: " + persistentObject);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hall could not be deleted");
		}   
	}


	/**
	 * @return returns all available halls
	 */
	public List<Hall> readAllHalls() {
		List<Hall> ret = new ArrayList<Hall>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM HALL");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Hall r = new Hall();
				r.name = rs.getString("NAME");
				r.cinema = rs.getInt("FK_CINEMA");
				r.num_of_seats = rs.getInt("NUM_OF_SEATS");
				r.id = rs.getInt("ID");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Halls.");
		}     
		return ret;
	}

	/**
	 * gets the Hall by combination of cinema and name
	 * 
	 * @param cinema cinema that the hall is located at
	 * @param name name of the hall
	 * @return returns a hall object from the database
	 */
	public Hall getHall(int cinema, String name) {
		PreparedStatement stmt;
		Hall r = new Hall();

		r.id = -1;
		r.name = "unknown hall";
		r.cinema = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM HALL WHERE FK_CINEMA = ? AND NAME = ?");
			stmt.setInt(1, cinema);
			stmt.setString(2, name);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			// Mapping
			r.id = rs.getInt("ID");
			r.name = rs.getString("NAME");
			r.cinema = rs.getInt("FK_CINEMA");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hall could not be read.");
		}     
		return r;
	}
}
