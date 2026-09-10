package com.kst.movie_ticket_reservation.util.annotations;

import com.kst.movie_ticket_reservation.util.validators.EnumConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface IsEnum
{
    Class<? extends Enum<?>> enumClass();

    String message() default "Value must be a valid constant of the specified enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
