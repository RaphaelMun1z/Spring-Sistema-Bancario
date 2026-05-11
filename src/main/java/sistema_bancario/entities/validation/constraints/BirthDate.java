package sistema_bancario.entities.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sistema_bancario.entities.validation.BirthDateValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
	String message() default "Invalid Date";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}