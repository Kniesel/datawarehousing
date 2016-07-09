package at.fhj.itm.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Screening;

/**
 * Data Access Object for the class Screening 
 * Connects to the database to create, read, update and delete Screenings.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class ScreeningDAO extends GenericSqlDAO<Screening, Integer>{

	/**
	 * Writes a new Screening to the database.
	 * @return returns the ID of the new Screening
	 */
	@Override
	public Integer create(Screening newInstance) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO Screening (ID, STARTING_TIME, FK_MOVIE, FK_HALL) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setTimestamp(2,newInstance.starting_time);
			stmt.setInt(3, newInstance.movie);
			stmt.setInt(4, newInstance.hall);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Screening could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the Screening with the specified ID from the database.
	 */
	@Override
	public Screening read(Integer id) {
		PreparedStatement stmt;
		Screening r = new Screening();

		r.id = -1;
		r.starting_time = null;
		r.movie = -1;
		r.hall = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM SCREENING WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.starting_time = rs.getTimestamp("STARTING_TIME");
			r.movie = rs.getInt("FK_MOVIE");
			r.hall = rs.getInt("FK_HALL");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Screening " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates a Screening in the database.
	 */
	@Override
	public void update(Screening transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE SCREENING SET STARTING_TIME = ?, FK_MOVIE = ?, FK_HALL = ? where ID = ?");
			stmt.setTimestamp(1, transientObject.starting_time);
			stmt.setInt(2, transientObject.movie);
			stmt.setInt(3, transientObject.hall);
			stmt.setInt(4, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("updating");
				System.out.println("Something strange is going on: Screening not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Screening could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a Screening from the database.
	 */
	@Override
	public void delete(Screening persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM Screening WHERE ID = ?");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Screening not found or not unique: " + persistentObject);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Screening could not be deleted");
		}   
	}


	/**
	 * @return returns all available Screenings
	 */
	public List<Screening> readAllScreenings() {
		List<Screening> ret = new ArrayList<Screening>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM SCREENING");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Screening r = new Screening();
				r.starting_time = rs.getTimestamp("STARTING_TIME");
				r.hall = rs.getInt("FK_HALL");
				r.movie = rs.getInt("FK_MOVIE");
				r.id = rs.getInt("ID");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Screenings.");
		}     
		return ret;
	}

	/**
	 * gets the Screening by combination of hall, movie and starting_time
	 * @param hall that the Screening is located at
	 * @param starting_time of the Screening
	 * @return returns a screening object from the database
	 */
	public Screening getScreening(int hall, int starting_time, int movie) {
		PreparedStatement stmt;
		Screening r = new Screening();

		r.id = -1;
		r.starting_time = null;
		r.hall = -1;

		try {
			stmt = conn.prepareStatement("SELECT * FROM SCREENING WHERE FK_HALL = ? AND STARTING_TIME = ? AND FK_MOVIE = ?");
			stmt.setInt(1, hall);
			stmt.setInt(2, starting_time);
			stmt.setInt(3, movie);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			// Mapping
			r.id = rs.getInt("ID");
			r.hall = rs.getInt("FK_HALL");
			r.starting_time = rs.getTimestamp("STARTING_TIME");
			r.movie = rs.getInt("FK_MOVIE");
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Screening could not be read.");
		}     
		return r;
	}
}
