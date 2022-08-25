package com.cognixia.jump.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.jump.ConnectionManager.ConnectionManager;
import com.cognixia.jump.Exceptions.RecordNotFoundException;
import com.cognixia.jump.model.Book;
import com.cognixia.jump.model.Tracker;

public class BookDAO implements DAO<Book> {

	private Connection conn = ConnectionManager.getConnection();

	// List all books
	@Override
	public List<Book> findAll() {

		List<Book> books = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";

		try {
			query = "SELECT * from book";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getRow() == 0) {
					throw new RecordNotFoundException("No books found");
				}

				Book b = new Book();
				b.setBookID(rs.getInt(1));
				b.setName(rs.getString(2));
				b.setAuthor(rs.getString(3));
				b.setPages(rs.getInt(4));
				b.setGenre(rs.getString(5));
				b.setCriticRating(rs.getDouble(6));
				b.setUserRating(rs.getDouble(7));
				b.setRatingCount(rs.getInt(8));

				books.add(b);
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return books;
	}

	// Find single book
	public Book findByName(String name) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		Book book = null;

		try {

			query = "SELECT * FROM book WHERE bookName = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getRow() == 0) {
					throw new RecordNotFoundException("No books found");
				}

				book = new Book();
				book.setBookID(rs.getInt(1));
				book.setName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));
				book.setGenre(rs.getString(5));
				book.setCriticRating(rs.getDouble(6));
				book.setUserRating(rs.getDouble(7));
				book.setRatingCount(rs.getInt(8));
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return book;
	}

	public List<Book> findByName(List<Tracker> trackerList) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		List<Book> bookList = new ArrayList<>();
		Book book = null;
		int bookID = 0;

		try {

			query = "SELECT * FROM book WHERE bookID = ?";
			pstmt = conn.prepareStatement(query);

			for (Tracker tracker : trackerList) {
				bookID = tracker.getBookID();
				pstmt.setInt(1, bookID);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					if (rs.getRow() == 0) {
						throw new RecordNotFoundException("No books found");
					}

					book = new Book();
					book.setBookID(rs.getInt(1));
					book.setName(rs.getString(2));
					book.setAuthor(rs.getString(3));
					book.setPages(rs.getInt(4));
					book.setGenre(rs.getString(5));
					book.setCriticRating(rs.getDouble(6));
					book.setUserRating(rs.getDouble(7));
					book.setRatingCount(rs.getInt(8));
					
					bookList.add(book);
				}
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookList;
	}
	
	

	// input a book into the database
	@Override
	public boolean create(Book entity) {

		PreparedStatement pstmt = null;
		int numInserts = 0;
		
		try {
			String query = "Insert into book(bookID, bookName, author, pages)" + " values(?,?,?,?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setNull(1, Types.INTEGER);
			pstmt.setString(2, entity.getName());
			pstmt.setString(3, entity.getAuthor());
			pstmt.setInt(4, entity.getPages());

			numInserts = pstmt.executeUpdate();

			if (numInserts > 0) {
				System.out.println("Entity " + entity + " added to db.");
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean remove(Book entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Book entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public Book findById(int id) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		Book book = null;

		try {

			query = "SELECT * FROM book WHERE bookID = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getRow() == 0) {
					throw new RecordNotFoundException("No books found");
				}
				
				book = new Book();
				book.setBookID(rs.getInt(1));
				book.setName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));
				book.setGenre(rs.getString(5));
				book.setCriticRating(rs.getDouble(6));
				book.setUserRating(rs.getDouble(7));
				book.setRatingCount(rs.getInt(8));
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	public List<Book> findByGenre(String genre) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		Book book = null;
		List<Book> books = new ArrayList<Book>();
		

		try {

			query = "SELECT * FROM book WHERE genre = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, genre);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getRow() == 0) {
					throw new RecordNotFoundException("No books found");
				}
				
				book = new Book();
				book.setBookID(rs.getInt(1));
				book.setName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));
				book.setGenre(rs.getString(5));
				book.setCriticRating(rs.getDouble(6));
				book.setUserRating(rs.getDouble(7));
				book.setRatingCount(rs.getInt(8));
				
				books.add(book);
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return books;
	}
	
	public Book calculateUserRating(Book book, double rating) {
		
		int ratingCount = book.getRatingCount();
		
		int newRatingCount = ratingCount + 1;
		
		if (ratingCount == 0) {
			book.setUserRating(rating/newRatingCount);
			
			book.setRatingCount(newRatingCount);
		} else {
		
			double currentUserRating = book.getUserRating() * ratingCount;
			book.setUserRating((currentUserRating + rating)/newRatingCount);
			
			book.setRatingCount(newRatingCount);
		}
		System.out.println(book);
		return book;
	}
	
	public boolean updateUserRating(Book book, double rating) {
		
		book = calculateUserRating(book, rating);
		
		PreparedStatement pstmt = null;
		String query = "";
		int numUpdates = 0;

		try {
			query = "UPDATE book SET userRating = ?, ratingCount = ? WHERE bookID = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setDouble(1, book.getUserRating());
			pstmt.setInt(2, book.getRatingCount());
			pstmt.setInt(3, book.getBookID());

			numUpdates = pstmt.executeUpdate();

			if (numUpdates > 0) {
				System.out.println("Book rating for " + book.getName() + " updated.");
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
