package sistema_bancario.entities.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sistema_bancario.entities.validation.CEPValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CEPValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CEP {
	String message() default "Invalid CEP";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}