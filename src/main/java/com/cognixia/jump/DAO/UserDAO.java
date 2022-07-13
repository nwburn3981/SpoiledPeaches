package com.cognixia.jump.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.cognixia.jump.ConnectionManager.ConnectionManager;
import com.cognixia.jump.model.User;

public class UserDAO implements DAO<User>{

	Connection conn = ConnectionManager.getConnection();
	
	
	@Override
	public List<User> findAll() {
		
		return null;
	}

	@Override
	public boolean create(User entity) {
		
		return false;
	}

	@Override
	public boolean update(User entity) {
		
		return false;
	}

	@Override
	public boolean remove(User entity) {

		return false;
	}
	
	public int verifyUser(User entity) {
		ResultSet rs = null;
		User user = null;
		String SQL = "SELECT * from user where username=? and passwd=?";
		
		try {
			PreparedStatement prep = conn.prepareStatement(SQL);
			prep.setString(1, entity.getUsername());
			prep.setString(2, entity.getPassword());
			
			rs = prep.executeQuery();
			
			rs.next();
			
			if(rs != null) {
				return rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
