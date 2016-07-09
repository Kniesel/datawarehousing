package at.fhj.itm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Halmschlager Tabea, Kirchmaier Johanna
 * 
 */
public class Check {
	Connection conn = ConnectionFactory.getConnection();

	/**
	 * checks if a seat is in hall or not
	 * 
	 * @param seat 
	 * @param screening
	 * @return true if it is in hall, false if it is not in hall
	 */
	public boolean seatInHall(int seat, int screening) {
		
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM seatsPerScreening WHERE seat = ? AND screening = ?");
			stmt.setInt(1, seat);
			stmt.setInt(2, screening);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("Checking if your seat is in hall...");
			if (rs.first()){
				System.out.println("It is in hall");
				return true;
			}
			else {
				System.out.println("It is not in hall");
				return false;

			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
}
