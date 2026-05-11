package sistema_bancario.entities.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sistema_bancario.entities.validation.constraints.CEP;

public class CEPValidator implements ConstraintValidator<CEP, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String cep = value == null ? "" : value;

		return cep.matches("^\\d{2}\\d{3}[-]\\d{3}$");
	}

}
