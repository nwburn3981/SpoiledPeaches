package com.cognixia.jump.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

				if (rs.getRow() == 0)
					throw new RecordNotFoundException("No books found");

				Book b = new Book();
				b.setBookID(rs.getInt(1));
				b.setName(rs.getString(2));
				b.setAuthor(rs.getString(3));
				b.setPages(rs.getInt(4));

				books.add(b);

			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
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
				if (rs.getRow() == 0)
					throw new RecordNotFoundException("No books found");

				book = new Book();
				book.setBookID(rs.getInt(1));
				book.setName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));

			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		}

		catch (Exception e) {
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
		int bookID;

		try {

			query = "SELECT * FROM book WHERE bookID = ?";

			pstmt = conn.prepareStatement(query);

			for (Tracker tracker : trackerList) {
				bookID = tracker.getBookID();
				pstmt.setInt(1, bookID);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					if (rs.getRow() == 0)
						throw new RecordNotFoundException("No books found");

					book = new Book();
					book.setBookID(rs.getInt(1));
					book.setName(rs.getString(2));
					book.setAuthor(rs.getString(3));
					book.setPages(rs.getInt(4));

					bookList.add(book);

				}
			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return bookList;
	}

	@Override
	public boolean create(Book entity) {
		// input a book into the database
		//

		try {
			String query = "Insert into book(bookID, bookName, author, pages)" + " values(?,?,?,?)";

			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setNull(1, Types.INTEGER);
			pstmt.setString(2, entity.getName());
			pstmt.setString(3, entity.getAuthor());
			pstmt.setInt(4, entity.getPages());

			int numInserts = pstmt.executeUpdate();

			if (numInserts > 0) {
				System.out.println("Entity " + entity + " added to db.");
				return true;
			}

		} catch (Exception e) {
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

				if (rs.getRow() == 0)
					throw new RecordNotFoundException("No books found");

				book = new Book();
				book.setBookID(rs.getInt(1));
				book.setName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));

			}

		} catch (RecordNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return book;
	}
}
