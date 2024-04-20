package br.com.dbc.vemser.iShirts.annotation.interfaces;
import br.com.dbc.vemser.iShirts.annotation.validator.LocalDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
@Documented
public @interface FormatLocalDateTime {
    String message() default "Formato de data inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
