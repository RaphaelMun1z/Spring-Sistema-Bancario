package sistema_bancario.entities.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sistema_bancario.entities.validation.RGValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RGValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RG {
	String message() default "Invalid RG";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}