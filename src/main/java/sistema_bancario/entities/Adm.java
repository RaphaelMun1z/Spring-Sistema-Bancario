package sistema_bancario.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import sistema_bancario.entities.enums.UserRole;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "tb_adms")
public class Adm extends User implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	public Adm() {
		super();
	}

	public Adm(String id, String name, String phone, String email, String password) {
		super(id, name, phone, email, password, UserRole.ADM);
	}
}
