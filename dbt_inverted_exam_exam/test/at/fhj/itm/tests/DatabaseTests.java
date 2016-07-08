package at.fhj.itm.tests;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;

import at.fhj.itm.dao.*;
import at.fhj.itm.obj.*;
import at.fhj.itm.utils.ConnectionFactory;

public class DatabaseTests {

	@Test
	public void testConnectionFactory() throws SQLException {
		Connection c = ConnectionFactory.getConnection();
		Statement s = c.createStatement();
		s.execute("SELECT COUNT(*) FROM MOVIE");
		
		// Wenn keine Exception geworfen wird ist der Test erfolgreich
	}
	
	@Test 
	public void testCustomerDAO()	{
		CustomerDAO edao = new CustomerDAO();
		
		List<Customer> Customers_before = edao.readAllCustomers();

		// Eine neue person reinspeichern
		Customer p1 = new Customer();
		p1.firstname = "The";
		p1.surname = "Doctor";
		Integer newId = edao.create(p1);

		// Eigentlich muss jetzt in der DB ein Eintrag mehr sein
		assertTrue(Customers_before.size() < edao.readAllCustomers().size());
		
		// So, jetzt holen wir die Person wieder raus
		Customer p2 = edao.read(newId);
		assertTrue(p2.id == newId);
		assertTrue(p2.firstname.equals("The"));
		assertTrue(p2.surname.equals("Doctor"));

		// Und jetzt löschen wir die neue Person wieder raus
		edao.delete(p2);
		
		// Jetzt ist der alte Stand widerhergestellt
		assertTrue(Customers_before.size() == edao.readAllCustomers().size());
		
	}
	
	@Test 
	public void testCinemaDAO()
	{
		CinemaDAO cdao = new CinemaDAO();
		
		List<Cinema> original_Cinemas = cdao.readAllCinemas();

		Cinema g1 = new Cinema();
		g1.name = "Irgendwas neues";
		Integer newId = cdao.create(g1);
		
		// Eigentlich muss jetzt in der DB ein Eintrag mehr sein
		assertTrue(original_Cinemas.size() < cdao.readAllCinemas().size());

		// Cinema wieder aus der DB auslesen
		Cinema g2 = cdao.read(newId);
		assertTrue(g2.id == newId);
		assertTrue(g2.name.equals(g1.name));
		
		
		// Und jetzt löschen wir die neue Cinema wieder raus
		cdao.delete(g2);
		
		// Die Anzahl muss wieder gleich sein
		assertTrue(original_Cinemas.size() == cdao.readAllCinemas().size());
		
	}
	
	@Test
	public void testHallDAO()	{
		HallDAO hdao = new HallDAO();
		
		int numberOfHalls = hdao.readAllHalls().size();
		
		// Einen Hall hinzufügen
		Hall x = new Hall();
		x.name = "x";
		x.cinema = 1;
		hdao.create(x);
				
		// Jetzt muss eine Hall mehr drinnen sein als vorher
		assertTrue(numberOfHalls < hdao.readAllHalls().size());

		// Jetzt löschen wir den Hall wieder
		hdao.delete(x);
		
		// Jetzt muss Die Anzahl wieder so sein wie vorher
		assertTrue(numberOfHalls == hdao.readAllHalls().size());
		
	}
	
	@Test
	public void testSeatDAO()	{
		SeatDAO sdao = new SeatDAO();
		
		int numberOfSeats = sdao.readAllSeats().size();
		
		// Einen Seat hinzufügen
		Seat x = new Seat();
		x.hall = 1;
		sdao.create(x);
				
		// Jetzt muss eine Seat mehr drinnen sein als vorher
		assertTrue(numberOfSeats < sdao.readAllSeats().size());

		// Jetzt löschen wir den Seat wieder
		sdao.delete(x);
		
		// Jetzt muss Die Anzahl wieder so sein wie vorher
		assertTrue(numberOfSeats == sdao.readAllSeats().size());
		
	}
	
	@Test
	public void testMovieDAO()	{
		MovieDAO mdao = new MovieDAO();
		
		int numberOfMovies = mdao.readAllMovies().size();
		
		// Einen Movie hinzufügen
		Movie x = new Movie();
		x.name = "MysteryMovie";
		mdao.create(x);
				
		// Jetzt muss eine Movie mehr drinnen sein als vorher
		assertTrue(numberOfMovies < mdao.readAllMovies().size());

		// Jetzt löschen wir den Movie wieder
		mdao.delete(x);
		
		// Jetzt muss Die Anzahl wieder so sein wie vorher
		assertTrue(numberOfMovies == mdao.readAllMovies().size());
		
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testScreeningDAO()	{
		ScreeningDAO mdao = new ScreeningDAO();
		
		int numberOfScreenings = mdao.readAllScreenings().size();
		
		// Einen Screening hinzufügen
		Screening x = new Screening();
		x.movie = 1;
		x.starting_time = new Timestamp(20,7,15,0,0,0,0);
		x.hall = 1;
		x.id = 10001;
		mdao.create(x);
						
		// Jetzt muss eine Screening mehr drinnen sein als vorher
		assertTrue(numberOfScreenings < mdao.readAllScreenings().size());

		// Jetzt löschen wir den Screening wieder
		mdao.delete(x);
		
		// Jetzt muss Die Anzahl wieder so sein wie vorher
		assertTrue(numberOfScreenings == mdao.readAllScreenings().size());
		
	}
	
	@Test
	public void testBookingDAO()	{
		BookingDAO mdao = new BookingDAO();
		
		int numberOfBookings = mdao.readAllBookings().size();
		
		// Einen Booking hinzufügen
		Booking x = new Booking();
		x.screening = 1;
		x.seat = 1;
		x.id = 100;
		mdao.create(x);
		
						
		// Jetzt muss eine Booking mehr drinnen sein als vorher
		assertTrue(numberOfBookings < mdao.readAllBookings().size());

		// Jetzt löschen wir den Booking wieder
		mdao.delete(x);
		
		// Jetzt muss Die Anzahl wieder so sein wie vorher
		assertTrue(numberOfBookings == mdao.readAllBookings().size());
		
	}
	
	@Test
	public void testReserve(){
		long startTime = System.currentTimeMillis();
		
		CustomerDAO cdao = new CustomerDAO();
		cdao.reserve(1,5);
		
		assertTrue(cdao.reserve(1,5) == 0);
		
		long endTime = System.currentTimeMillis();
		System.out.println("time: " + (endTime - startTime));
	}
}