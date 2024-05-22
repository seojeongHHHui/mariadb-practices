package bookshop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookshop.vo.AuthorVo;

public class AuthorDao {
	
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.0.196:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		
		return conn;
	}
	
	public List<AuthorVo> findAll() {
		List<AuthorVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select no, name from author");
			ResultSet rs = pstmt.executeQuery(); // 바인딩 필요없으면 여기에 가능
		){
			// 바인딩 (필요하면) 여기에서
			
			//ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				
				AuthorVo vo = new AuthorVo();
				vo.setNo(no);
				vo.setName(name);
				
				result.add(vo);
			}
			
			//rs.close(); // try에 넣으면 자동 클로즈
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		return result;
	}

	public int insert(AuthorVo vo) {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into author(name) values(?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			){
				pstmt1.setString(1, vo.getName());
				//result = 1 == pstmt.executeUpdate();
				result = pstmt1.executeUpdate();
				
				ResultSet rs = pstmt2.executeQuery();
//				if(rs.next()) {
//					vo.setNo(rs.getLong(1));
//				}
				vo.setNo(rs.next() ? rs.getLong(1) : null);
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
				PreparedStatement pstmt = conn.prepareStatement("delete from author where no = ?");
			){
				pstmt.setLong(1,no);
				result = pstmt.executeUpdate();
			
			} catch (SQLException e) {
				System.out.println("error:" + e);
			} 
		
		return result;
	}
	
	public int deleteAll() {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from author");
		) {
			result = pstmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("error:" + ex);
		}
		
		return result;
	}	
}
