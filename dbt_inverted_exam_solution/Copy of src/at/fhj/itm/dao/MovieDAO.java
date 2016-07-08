package at.fhj.itm.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.fhj.itm.obj.Movie;

/**
 * Data Access Object for the class Movie 
 * Connects to the database to create, read, update and delete Movies.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna
 *
 */
public class MovieDAO extends GenericSqlDAO<Movie, Integer>{

	/**
	 * Writes a new Movie to the database.
	 * @return returns the ID of the new Movie
	 */
	@Override
	public Integer create(Movie newInstance) {
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO Movie (ID, NAME, RUNNING_TIME) VALUES (?, ?, ?)");
			stmt.setInt(1, newInstance.id);
			stmt.setString(2,newInstance.name);
			stmt.setInt(3, newInstance.running_time);
			stmt.executeUpdate();   
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Movie could not be created.");
		}     

		return newInstance.id;
	}

	
	/**
	 * Reads the Movie with the specified ID from the database.
	 */
	@Override
	public Movie read(Integer id) {
		PreparedStatement stmt;
		Movie r = new Movie();

		r.id = -1;
		r.name = "";
		r.running_time = -1;


		try {
			stmt = conn.prepareStatement("SELECT * FROM MOVIE WHERE ID = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			r.id = rs.getInt("ID");
			r.name = rs.getString("NAME");
			r.running_time = rs.getInt("RUNNING_TIME");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Movie " + id + " could not be read.");
		}     
		return r;
	}

	
	/**
	 * Updates a Movie in the database.
	 */
	@Override
	public void update(Movie transientObject) {

		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE MOVIE SET NAME = ?, RUNNING_TIME = ? where ID = ?");
			stmt.setString(1, transientObject.name);
			stmt.setInt(2, transientObject.running_time);
			stmt.setInt(3, transientObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1){
				System.out.println("Something strange is going on: Movie not found or not unique: " + transientObject);
			}	 
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Movie could not be updated.");
		}     
	}
	
	
	/**
	 * Deletes a Movie from the database.
	 */
	@Override
	public void delete(Movie persistentObject) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("DELETE FROM Movie WHERE ID = ? ");
			stmt.setInt(1, persistentObject.id);
			int affectedRows = stmt.executeUpdate();

			if(affectedRows != 1)
				System.out.println("Something strange is going on: Movie not found or not unique: " + persistentObject);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Movie could not be deleted");
		}   
	}


	/**
	 * @return returns all available Movies
	 */
	public List<Movie> readAllMovies() {
		List<Movie> ret = new ArrayList<Movie>();
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement("SELECT * FROM MOVIE");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				Movie r = new Movie();
				r.name = rs.getString("NAME");
				r.running_time = rs.getInt("RUNNING_TIME");
				r.id = rs.getInt("ID");
				ret.add(r);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR while reading Movies.");
		}     
		return ret;
	}

	/**
	 * gets the room by combination of site and name
	 * @param name name of the Movie
	 * @param running_time running_time of the Movie
	 * @return returns a screening object from the database
	 */
	public Movie getMovie(int name, int running_time) {
		PreparedStatement stmt;
		Movie r = new Movie();

		r.id = -1;
		r.name = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM MOVIE WHERE FK_HALL = ? AND NAME = ? AND RUNNING_TIME = ?");
			stmt.setInt(1, name);
			stmt.setInt(2, running_time);
			ResultSet rs = stmt.executeQuery();
			rs.first();

			// Mapping
			r.id = rs.getInt("ID");
			r.name = rs.getString("NAME");
			r.running_time = rs.getInt("RUNNING_TIME");
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Movie could not be read.");
		}     
		return r;
	}
}
