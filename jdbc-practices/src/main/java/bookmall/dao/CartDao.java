package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.BookVo;
import bookmall.vo.CartVo;

public class CartDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.196:3306/bookmall?charset=utf8";
			conn = DriverManager.getConnection(url, "bookmall", "bookmall");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		
		return conn;
	}
	
	public List<CartVo> findAll() {
		List<CartVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select user_no, book_no, quantity, price from cart");
			ResultSet rs = pstmt.executeQuery();
		) {
			while(rs.next()) {
				Long userNo = rs.getLong(1);
				Long bookNo = rs.getLong(2);
				int quantity = rs.getInt(3);
				int price = rs.getInt(4);
				
				CartVo vo = new CartVo();
				vo.setUserNo(userNo);
				vo.setBookNo(bookNo);
				vo.setQuantity(quantity);
				vo.setPrice(price);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}

	public List<CartVo> findByUserNo(Long userNo) {
		List<CartVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.user_no, a.book_no, a.quantity, a.price, b.title"
															+ " from cart a, book b"
															+ " where a.book_no=b.no and user_no = ?");
			
		) {
			pstmt.setLong(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long bookNo = rs.getLong(2);
				int quantity = rs.getInt(3);
				int price = rs.getInt(4);
				String bookTitle = rs.getString(5);
				
				CartVo vo = new CartVo();
				vo.setUserNo(userNo);
				vo.setBookNo(bookNo);
				vo.setQuantity(quantity);
				vo.setPrice(price);
				vo.setBookTitle(bookTitle);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public int insert(CartVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into cart(user_no, book_no, quantity, price) values(?, ?, ?, ?)");
		) {
			pstmt1.setLong(1, vo.getUserNo());
			pstmt1.setLong(2, vo.getBookNo());
			pstmt1.setInt(3, vo.getQuantity());
			pstmt1.setInt(4, vo.getPrice());
			result = pstmt1.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;		
	}

	public int deleteByUserNoAndBookNo(Long userNo, Long bookNo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from cart where user_no = ? and book_no = ?");
		) {
			pstmt.setLong(1, userNo);
			pstmt.setLong(2, bookNo);
			result = pstmt.executeUpdate();
					
		} catch (SQLException e) {
					System.out.println("error:" + e);
		}
				
		return result;
	}
	
}
