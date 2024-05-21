package bookmall.vo;

public class UserVo {
	private Long no;
	private String name;
	private String email;
	private String password;
	private String tel;
	
	public UserVo() {}
	public UserVo(String name, String email, String password, String tel) {
		this.name=name;
		this.email=email;
		this.password=password;
		this.tel=tel;
	}
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "UserVo [no=" + no + ", name=" + name + ", tel=" + tel + ", email=" + email + ", password=" + password
				+ "]";
	}
	
}
