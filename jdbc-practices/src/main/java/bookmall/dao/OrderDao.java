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
import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class OrderDao {
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
	
	public List<OrderVo> findAll() {
		List<OrderVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.no, a.number, a.status, a.payment, a.shipping, a.user_no, b.name"
															+ " from orders a, user b"
															+ " where a.user_no = b.no order by no desc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while(rs.next()) {
				Long no = rs.getLong(1);
				String number = rs.getString(2);
				String status = rs.getString(3);
				int payment = rs.getInt(4);
				String shipping = rs.getString(5);
				Long userNo = rs.getLong(6);
				String name = rs.getString(7);
				
				OrderVo vo = new OrderVo();
				vo.setNo(no);
				vo.setNumber(number);
				vo.setStatus(status);
				vo.setPayment(payment);
				vo.setShipping(shipping);
				vo.setUserNo(userNo);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}

	public OrderVo findByNoAndUserNo(Long no, Long userNo) {
		OrderVo resultVo = new OrderVo();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select no, number, status, payment, shipping, user_no from orders where no = ? and user_no = ?");
			
		) {
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String number = rs.getString(2);
				String status = rs.getString(3);
				int payment = rs.getInt(4);
				String shipping = rs.getString(5);
					
				resultVo.setNo(no);
				resultVo.setNumber(number);
				resultVo.setStatus(status);
				resultVo.setPayment(payment);
				resultVo.setShipping(shipping);
				resultVo.setUserNo(userNo);
				
			} else {
				resultVo = null;
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return resultVo;
	}

	public List<OrderBookVo> findBooksByNoAndUserNo(Long orderNo, Long userNo) {
		List<OrderBookVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.book_no, a.order_no, a.quantity, a.price, c.title"
															+ " from orders_book a, orders b, book c"
															+ " where a.order_no=b.no and a.book_no=c.no"
															+ "   and b.no = ? and b.user_no = ?");
		) {
			pstmt.setLong(1, orderNo);
			pstmt.setLong(2, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long bookNo = rs.getLong(1);
				//Long orderNo = rs.getLong(2);
				int quantity = rs.getInt(3);
				int price = rs.getInt(4);
				String bookTitle = rs.getString(5);
								
				OrderBookVo vo = new OrderBookVo();
				vo.setBookNo(bookNo);
				vo.setOrderNo(orderNo);
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
	
	public int insert(OrderVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into orders(number, status, payment, shipping, user_no) values(?, ?, ?, ?, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setString(1, vo.getNumber());
			pstmt1.setString(2, vo.getStatus());
			pstmt1.setInt(3, vo.getPayment());
			pstmt1.setString(4, vo.getShipping());
			pstmt1.setLong(5, vo.getUserNo());
			result = pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ?  rs.getLong(1) : null);
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;		
	}
	
	public int insertBook(OrderBookVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("insert into orders_book(book_no, order_no, quantity, price) values(?, ?, ?, ?)");
		) {
			pstmt.setLong(1, vo.getBookNo());
			pstmt.setLong(2, vo.getOrderNo());
			pstmt.setInt(3, vo.getQuantity());
			pstmt.setInt(4, vo.getPrice());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;		
	}
	
	public int deleteByNo(Long no) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from orders where no = ?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;	
	}

	public int deleteBooksByNo(Long orderNo) { // orders_book
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from orders_book where order_no = ?");
		) {
			pstmt.setLong(1, orderNo);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;	
	}
}
