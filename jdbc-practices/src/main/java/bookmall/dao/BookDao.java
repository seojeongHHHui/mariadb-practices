package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.BookVo;

public class BookDao {
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
	
	public List<BookVo> findAll() {
		List<BookVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.no, a.title, a.price, b.name from book a, category b where a.category_no = b.no order by no desc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				int price = rs.getInt(3);
				Long categoryNo = rs.getLong(4);
				
				BookVo vo = new BookVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setPrice(price);
				vo.setCategoryNo(categoryNo);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public int insert(BookVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into book(title, price, category_no) values(?, ?, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setInt(2, vo.getPrice());
			pstmt1.setLong(3, vo.getCategoryNo());
			result = pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ?  rs.getLong(1) : null);
			rs.close();			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;		
	}
	
	public int deleteByNo(Long no) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from book where no = ?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;	
	}
	
//	public Integer update(Long no, String status) {
//		int result = 0;
//
//		try (
//			Connection conn = getConnection();
//			PreparedStatement pstmt = conn.prepareStatement("update book set status = ? where no = ?");
//		) {
//			pstmt.setString(1, status);
//			pstmt.setLong(2, no);
//			result = pstmt.executeUpdate();
//			
//		} catch (SQLException e) {
//			System.out.println("error:" + e);
//		}
//		
//		return result;	
//	}
}
