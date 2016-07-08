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
			stmt = conn.prepareStatement("SELECT * FROM seat se inner join screening sc on se.fk_hall = sc.fk_hall WHERE se.id = ? AND sc.id = ?");
			stmt.setInt(1, seat);
			stmt.setInt(2, screening);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.first()){
				return true;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
}
