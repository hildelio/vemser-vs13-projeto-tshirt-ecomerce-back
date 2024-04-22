package br.com.dbc.vemser.iShirts.annotation.validator;

import br.com.dbc.vemser.iShirts.annotation.interfaces.FormatLocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
public class LocalDateTimeValidator implements ConstraintValidator<FormatLocalDateTime, LocalDateTime>{
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext){
        if(value == null){
            return false;
        }
        return !value.equals(LocalDateTime.MAX);
    }

}
