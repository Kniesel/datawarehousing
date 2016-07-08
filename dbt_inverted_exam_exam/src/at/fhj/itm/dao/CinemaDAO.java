package at.fhj.itm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Cinema;

/**
 * Data Access Object for the class Cinema 
 * Connects to the database to create, read, update and delete Cinemas.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class CinemaDAO extends GenericSqlDAO<Cinema, Integer> {
	
	/**
	 * Writes a new Cinema to the database.
	 * @return returns the ID of the new Cinema
	 */
	@Override
	public Integer create(Cinema newInstance) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO Cinema (ID, NAME) VALUES (?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setString(2, newInstance.name);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Cinema could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the Cinema with the specified ID from the database.
	 */
	@Override
	public Cinema read(Integer id) {
		PreparedStatement stmt;
		Cinema r = new Cinema();

		r.id = -1;
		r.name = "unknown";
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM CINEMA WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.name = rs.getString("NAME");

		} 
		
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Cinema " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates the name of a Cinema in the database.
	 */
	@Override
	public void update(Cinema transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE CINEMA SET NAME = ? where ID = ?");
			stmt.setString(1, transientObject.name);
			stmt.setInt(2, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Cinema not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Cinema could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a Cinema from the database.
	 */
	@Override
	public void delete(Cinema persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM Cinema WHERE ID = ?");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1)
				System.out.println("Something strange is going on: Cinema not found or not unique: " + persistentObject);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Cinema could not be deleted");
		}   
	}


	/**
	 * @return returns all Cinemas
	 */
	public List<Cinema> readAllCinemas() {
		List<Cinema> ret = new ArrayList<Cinema>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM CINEMA");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Cinema r = new Cinema();
				r.id = rs.getInt("ID");
				r.name = rs.getString("NAME");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Cinemas.");
		}     
		return ret;
	}

	/**
	 * gets the Cinema by name
	 * @param name name of the Cinema
	 * @return returns a Cinema object from the database
	 */
	public Cinema getCinema(String name) {
		PreparedStatement stmt;
		Cinema r = new Cinema();

		r.id = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM CINEMA WHERE NAME = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			// Mapping
			r.id = rs.getInt("ID");
			r.name = name;

			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Cinema could not be read.");
		}     
		return r;
	}
}

