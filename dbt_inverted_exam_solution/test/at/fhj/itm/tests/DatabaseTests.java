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

/**
 * Tests the database connection, all DAOs and if the accessrights are working like they should.
 * 
 * @author Halmschlager Tabea, Kirchmaier Johanna (and based a bit on Gerhard Hutter's project jdbc_doit-h2)
 * 
 */
public class DatabaseTests {
	
	/*
	 * connection to the database
	 */
	@Test
	public void testConnectionFactory() throws SQLException {
		Connection c = ConnectionFactory.getConnection();
		Statement s = c.createStatement();
		s.execute("SELECT COUNT(*) FROM MOVIE");
		
		// If no exception is thrown --> test is successful

	}
	
	
//--------------------------------------------------------------------------------	

	/*
	 * test CustomerDAO
	 */
	@Test 
	public void testCustomerDAO()	{
		CustomerDAO edao = new CustomerDAO();
		
		List<Customer> Customers_before = edao.readAllCustomers();
		
		// create a new person
		Customer p1 = new Customer();
		p1.firstname = "The";
		p1.surname = "Doctor";
		Integer newId = edao.create(p1);

		// there should be a new entry in the database
		assertTrue(Customers_before.size() < edao.readAllCustomers().size());
		
		// read new entry
		Customer p2 = edao.read(newId);
		assertTrue(p2.id == newId);
		assertTrue(p2.firstname.equals("The"));
		assertTrue(p2.surname.equals("Doctor"));

		// delete new entry
		edao.delete(p2);
		
		// amount should be the same as before
		assertTrue(Customers_before.size() == edao.readAllCustomers().size());
		
	}


//--------------------------------------------------------------------------------	

	/*
	 * test CinemaDAO
	 */
	@Test 
	public void testCinemaDAO()
	{
		CinemaDAO cdao = new CinemaDAO();
		
		List<Cinema> original_Cinemas = cdao.readAllCinemas();
		
		// create a new cinema
		Cinema g1 = new Cinema();
		g1.name = "Irgendwas neues";
		Integer newId = cdao.create(g1);
		
		// there should be a new entry in the database
		assertTrue(original_Cinemas.size() < cdao.readAllCinemas().size());

		// read cinema from db
		Cinema g2 = cdao.read(newId);
		assertTrue(g2.id == newId);
		assertTrue(g2.name.equals(g1.name));
		
		
		// delete cinema
		cdao.delete(g2);
		
		// amount should be the same as before
		assertTrue(original_Cinemas.size() == cdao.readAllCinemas().size());
		
	}

	
//--------------------------------------------------------------------------------	

	/*
	 * test HallDAO
	 */
	@Test
	public void testHallDAO()	{
		HallDAO hdao = new HallDAO();
		
		int numberOfHalls = hdao.readAllHalls().size();
		
		
		// create a new hall
		Hall x = new Hall();
		x.name = "x";
		x.cinema = 1;
		hdao.create(x);
				
		// there should be a new entry in the database
		assertTrue(numberOfHalls < hdao.readAllHalls().size());

		// delete hall
		hdao.delete(x);
		
		// amount should be the same as before
		assertTrue(numberOfHalls == hdao.readAllHalls().size());
		
	}

	
//--------------------------------------------------------------------------------	

	/*
	 * test SeatDAO
	 */
	@Test
	public void testSeatDAO()	{
		SeatDAO sdao = new SeatDAO();
		
		int numberOfSeats = sdao.readAllSeats().size();
		
		// create a new seat
		Seat x = new Seat();
		x.hall = 1;
		sdao.create(x);
				
		// check if new entry in database exists
		assertTrue(numberOfSeats < sdao.readAllSeats().size());

		// delete seat
		sdao.delete(x);
		
		// amount should be the same as before
		assertTrue(numberOfSeats == sdao.readAllSeats().size());
		
	}

//--------------------------------------------------------------------------------	

	/*
	 * test MovieDAO
	 */
	@Test
	public void testMovieDAO()	{
		MovieDAO mdao = new MovieDAO();
		
		int numberOfMovies = mdao.readAllMovies().size();
		
		// add a new movie
		Movie x = new Movie();
		x.name = "MysteryMovie";
		mdao.create(x);
				
		// there should be a new entry in the database
		assertTrue(numberOfMovies < mdao.readAllMovies().size());

		// delete movie
		mdao.delete(x);
		
		// amount should be the same as before
		assertTrue(numberOfMovies == mdao.readAllMovies().size());
		
	}
	
	
//--------------------------------------------------------------------------------	

	/*
	 * test ScreeningDAO
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testScreeningDAO()	{
		ScreeningDAO mdao = new ScreeningDAO();
		
		int numberOfScreenings = mdao.readAllScreenings().size();
		
		// add a new screening
		Screening x = new Screening();
		x.movie = 1;
		x.starting_time = new Timestamp(20,7,15,0,0,0,0);
		x.hall = 1;
		x.id = 10001;
		mdao.create(x);
						
		// there should be a new entry in the database
		assertTrue(numberOfScreenings < mdao.readAllScreenings().size());

		// delete screening
		mdao.delete(x);
		
		// amount should be the same as before
		assertTrue(numberOfScreenings == mdao.readAllScreenings().size());
		
	}

	
//--------------------------------------------------------------------------------	

	/*
	 * test BookingDAO
	 */
	@Test
	public void testBookingDAO()	{
		
		BookingDAO mdao = new BookingDAO();
		
		int numberOfBookings = mdao.readAllBookings().size();
		
		// add a new booking at screening with id 1 and seat id 1
		Booking x = new Booking();
		x.screening = 1;
		x.seat = 1;
		x.id = 100;
		mdao.create(x);
		
						
		// there should be a new booking, test it
		assertTrue(numberOfBookings < mdao.readAllBookings().size());

		// delete new booking
		mdao.delete(x);
		
		// amount should be the same as before
		assertTrue(numberOfBookings == mdao.readAllBookings().size());
		
	}
	
	
	
//--------------------------------------------------------------------------------	

	/*
	 * test method reserve() in CustomerDAO
	 */
	@Test
	public void testReserve(){
		System.out.println("\n\n------------Start timing while testing reserve()------------");
		// add timing
		long startTime = System.currentTimeMillis();
		
		System.out.println("------------reserve a new seat------------");
		// reserve seat with id 1 at screening with id 5
		CustomerDAO cdao = new CustomerDAO();
		cdao.reserve(1,5);
		
		System.out.println("------------check if seat is taken or not------------");
		// check if taken (it should be taken)
		assertTrue(cdao.reserve(1,5) == 0);
		
		long endTime = System.currentTimeMillis();
		System.out.println("-------------Time: " + (endTime - startTime) + "ms -------------");
	}
}