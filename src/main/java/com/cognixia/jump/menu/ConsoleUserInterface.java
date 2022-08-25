package com.cognixia.jump.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cognixia.jump.DAO.BookDAO;
import com.cognixia.jump.DAO.RecommendationsDAO;
import com.cognixia.jump.DAO.TrackerDAO;
import com.cognixia.jump.DAO.UserDAO;
import com.cognixia.jump.Exceptions.LoginInputException;
import com.cognixia.jump.InputValidation.InputValidation;
import com.cognixia.jump.model.Book;
import com.cognixia.jump.model.Tracker;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.UserPreferences;

public class ConsoleUserInterface {

	private static int id = 0;

	public static void userPrompt(Scanner scan) throws SQLException {

		System.out.println("\t\t*** Spoiled Peaches ***");

		while (id == 0) {
			String username = null, password = null;
			UserDAO dao = new UserDAO();
			User user = new User();

			try {
				System.out.print("\t\nPlease enter username or enter EXIT to exit: ");
				username = scan.next();

				if ((username.equals("EXIT"))) {
					System.out.println("I'll miss you.... :(");
					return;
				}

				System.out.print("\t\nPlease enter password or enter EXIT to exit: ");
				password = scan.next();

				if ((password.equals("EXIT"))) {
					System.out.println("I'll miss you.... :(");
					return;
				}

				user.setUsername(username);
				user.setPassword(password);

				id = dao.verifyUser(user);
				user = new User();
				if (id == 0) {
					throw new LoginInputException(id);
				}
			} catch (LoginInputException e) {

			} catch (Exception e) {
				System.out.println("Please try again..");
			}
		}

		mainMenu(scan);

	}

	public static void mainMenu(Scanner scan) throws SQLException {
		int userInput = 0;
		boolean exitStatus = false;

		while (exitStatus == false) {
			System.out.println("\nPlease choose a menu option:");

			System.out.println("\n1. View book list\n" + "2. Update progress\n" + "3. Add a book\n"
					+ "4. Remove a book\n" + "5. View Recommendations\n" + "6. Add Rating\n" + "7. Logout");

			userInput = InputValidation.mainMenuValidation(scan, "^[1234567]{1}$");
			System.out.println();
			switch (userInput) {
			case 1:
				// List all view
				listView(scan);
				break;

			case 2:
				// Update process view
				updateProgressView(scan);
				break;

			case 3:
				// Add a book view
				addBookView(scan);
				break;

			case 4:
				removeBookView(scan);
				break;

			case 5:
				
				viewRecs(scan);
				break;
				
			case 6:
				
				addRating(scan);
				break;
				
			case 7:
				
				String response = null;

				System.out.println("\nDo you want to log out? [Y/N]");
				response = scan.next();

				if (response.equalsIgnoreCase("y")) {
					id = 0;
					exitStatus = true;
					userPrompt(scan);

				}

				break;
			}
		}
	}
	
	public static void viewRecs(Scanner scan) throws SQLException {
		
		RecommendationsDAO rDao = new RecommendationsDAO();

		
		List<Book> books = rDao.createRecList(id);
		
		for (Book book : books) {
			System.out.println(book);
		}
		
	}

	public static void listView(Scanner scan) {
		// created objects
		TrackerDAO tDao = new TrackerDAO();
		BookDAO bDao = new BookDAO();
		String title = "Title", author = "Author", rating = "Critic Rating", genre = "Genre", status = "Status";

		List<Tracker> trackers = new ArrayList<Tracker>();
		trackers = tDao.findByUserId(id);
		List<Book> books = new ArrayList<Book>();
		books = bDao.findByName(trackers);

		System.out.println("\nThese are the books currently being tracked: \n");

		String str = String.format("%10s | %60s | %10s | %30s | %25s", title, author, rating, genre, status);
		System.out.println(str);

		for (int i = 0; i < books.size(); i++) {
			System.out.println(books.get(i) + " | " + trackers.get(i).getProgressStatus());

		}

	}

	public static void updateProgressView(Scanner scan) throws SQLException {
		int userInput = 0;

		System.out.println("\nPlease choose your menu:");
		System.out.println("\n1. Enter Book Name " + "\n2. Go Back to previous menu\n");

		userInput = InputValidation.mainMenuValidation(scan, "^[12]{1}$");

		switch (userInput) {
		case 1:
			searchBookPrompt(scan);
			break;

		case 2:
			mainMenu(scan);
			break;
		}

	}
	
	public static void addRating(Scanner scan) throws SQLException {
		TrackerDAO tDao = new TrackerDAO();
		BookDAO bDao = new BookDAO();
		double userRating;
		String title = "Title", author = "Author", rating = "Critic Rating", genre = "Genre", status = "Status";

		List<Tracker> trackers = new ArrayList<Tracker>();
		trackers = tDao.findByUserId(id);
		List<Book> books = new ArrayList<Book>();
		books = bDao.findByName(trackers);

		System.out.println("\nThese are the books currently being tracked: \n");

		String str = String.format("%10s | %60s | %10s | %30s | %25s", title, author, rating, genre, status);
		System.out.println(str);

		for (int i = 0; i < books.size(); i++) {
			System.out.println(books.get(i) + " | " + trackers.get(i).getProgressStatus());

		}
		
		System.out.print("\nPlease enter a book name, if you'd like to: ");
		scan.nextLine();
		String bookName = scan.nextLine();
		Book book = new Book();
		
		for (int i = 0; i < books.size(); i++) {
			if ((books.get(i)).getName().equals(bookName)) {
				book = books.get(i);
			}
		}
		
		System.out.println("\nPlease enter a book rating, if you'd like");
		
		userRating = scan.nextDouble();
		System.out.println(userRating);
		// remove from progress tracker
		bDao.updateUserRating(book, userRating);

	}

	private static void searchBookPrompt(Scanner scan) throws SQLException {
		BookDAO bDao = new BookDAO();
		String bookName = null;
		Book book = null;

		/******************************/

		TrackerDAO tDao = new TrackerDAO();
		BookDAO bDAO = new BookDAO();
		Integer bookID = 0;
		List<Integer> bookIDList = new ArrayList<>();
		List<Book> bookList = new ArrayList<Book>();
		String bkID = "BookID", name = "Name", page = "Pages", author = "Author", status = "Status";

		List<Tracker> tList = tDao.findByUserId(id);

		System.out.println("You are currently tracking: \n");

		for (Tracker tracker : tList) {
			bookID = tracker.getBookID();
			bookIDList.add(bookID);
		}

		for (int i = 0; i < bookIDList.size(); i++) {
			bookList.add(bDAO.findById(bookIDList.get(i)));
		}

		// print out progress book list
		String str = String.format("%3s | %60s | %6s | %30s | %11s", bkID, name, page, author, status);
		System.out.println(str);

		for (int i = 0; i < bookList.size(); i++) {
			System.out.println(bookList.get(i) + tList.get(i).getProgressStatus());

		}

		/*******************************/

		System.out.print("\nPlease enter Book name to update: ");
		scan.nextLine();
		bookName = scan.nextLine();

		book = bDao.findByName(bookName);
		updateStatusView(book, scan);
	}

	private static void updateStatusView(Book book, Scanner scan) throws SQLException {
		int userInput = 0;
		Tracker track = new Tracker();
		TrackerDAO tDao = new TrackerDAO();

		System.out.println("\nPlease choose your status:" + "\n1. Not started\n" + "2. Started\n" + "3. Complete\n"
				+ "4. Restart from main menu");

		userInput = InputValidation.mainMenuValidation(scan, "^[1234]$");

		switch (userInput) {
		case 1:
			// update status to not started
			track = tDao.findByCompositeId(id, book.getBookID());
			track.setProgressStatus("Not Started");

			if (tDao.update(track)) {
				System.out.println("\n\t *** Record updated ***");
			}

			System.out.println(track);
			break;

		case 2:
			// Update status to started
			track = tDao.findByCompositeId(id, book.getBookID());
			track.setProgressStatus("Started");

			if (tDao.update(track)) {
				System.out.println("\n\t *** Record updated ***");
			}

			System.out.println(track);
			break;

		case 3:
			// update status to completed
			track = tDao.findByCompositeId(id, book.getBookID());
			track.setProgressStatus("Completed");

			if (tDao.update(track)) {
				System.out.println("\n\t\t\t *** Record updated ***");
			}
			System.out.println(track);
			break;

		case 4:
			// Remove a book view
			mainMenu(scan);
			break;
		}
	}

	public static void addBookView(Scanner scan) {
		Integer bookID = 0;
		TrackerDAO tDao = new TrackerDAO();
		BookDAO bDao = new BookDAO();
		List<Integer> bookIDList = new ArrayList<>();
		String userAddedBook = null;
		int newbid = 0;
		String bkID = "BookID", name = "Name", page = "Pages", author = "Author";

		List<Tracker> tList = tDao.findByUserId(id);

		for (Tracker tracker : tList) {
			bookID = tracker.getBookID();
			bookIDList.add(bookID);
		}

		List<Book> blist = bDao.findAll();

		for (int i = 0; i < blist.size(); i++) {
			int bID = blist.get(i).getBookID();

			for (int j = 0; j < bookIDList.size(); j++) {
				if (bookIDList.get(j) == bID) {
					blist.remove(i);
					i--;
				}
			}
		}

		String str = String.format("%3s | %60s | %6s | %30s", bkID, name, page, author);
		System.out.println(str);
		blist.forEach(System.out::println);

		System.out.print("\nPlease enter a book name from the list above to add to your progress tracker: ");
		scan.nextLine();
		userAddedBook = scan.nextLine();

		for (int i = 0; i < blist.size(); i++) {
			if (userAddedBook.equals(blist.get(i).getName())) {
				newbid = blist.get(i).getBookID();
				Tracker newTracker = new Tracker(id, newbid, "Not Started");
				tDao.create(newTracker);
				System.out.println("Added to your list!");
			}
		}
	}

	public static void removeBookView(Scanner scan) {
		TrackerDAO tDao = new TrackerDAO();
		BookDAO bDAO = new BookDAO();
		Integer bookID = 0;
		List<Integer> bookIDList = new ArrayList<>();
		List<Book> bookList = new ArrayList<Book>();
		String userDelBook = null;
		int DelBookID = -1;
		String bkID = "BookID", name = "Name", page = "Pages", author = "Author";

		List<Tracker> tList = tDao.findByUserId(id);

		System.out.println("You are currently tracking: \n");

		for (Tracker tracker : tList) {
			bookID = tracker.getBookID();
			bookIDList.add(bookID);
		}

		for (int i = 0; i < bookIDList.size(); i++) {
			bookList.add(bDAO.findById(bookIDList.get(i)));
		}

		// print out progress book list
		String str = String.format("%3s | %60s | %6s | %30s", bkID, name, page, author);
		System.out.println(str);
		bookList.forEach(System.out::println);

		// get user input to remove from their progress tracker list
		System.out.print("\nPlease enter the book name you would like to remove from your progress list: ");
		scan.nextLine();
		userDelBook = scan.nextLine();

		for (int i = 0; i < bookList.size(); i++) {
			if ((bookList.get(i)).getName().equals(userDelBook)) {
				DelBookID = bookList.get(i).getBookID();
			}
		}

		// remove from progress tracker
		tDao.remove(id, DelBookID);

		// print progress tracker list
		tList = tDao.findByUserId(id);
		System.out.println();
	}
}
