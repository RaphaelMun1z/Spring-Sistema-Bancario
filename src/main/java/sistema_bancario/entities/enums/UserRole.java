package sistema_bancario.entities.enums;

public enum UserRole {
	SELLER("seller"), ADM("adm");

	private String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
