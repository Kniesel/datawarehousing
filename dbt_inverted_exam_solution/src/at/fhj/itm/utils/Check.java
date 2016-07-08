package at.fhj.itm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Check {
	Connection conn = ConnectionFactory.getConnection();


	public boolean seatInHall(int seat, int screening) {
		
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM seatsPerScreening WHERE seat = ? AND screening = ?");
			stmt.setInt(1, seat);
			stmt.setInt(2, screening);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("checking if seat is in hall...");
			if (rs.first()){
				System.out.println("in hall");
				return true;
			}
			else {
				System.out.println("not in hall");
				return false;

			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
}
