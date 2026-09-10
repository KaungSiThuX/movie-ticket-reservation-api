package com.kst.movie_ticket_reservation.util.validators;

import com.kst.movie_ticket_reservation.util.annotations.IsEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumConstraintValidator implements ConstraintValidator<IsEnum, CharSequence>
{
//
//    @Override
//    public void initialize(Annotation constraintAnnotation)
//    {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }
//
//    @Override
//    public boolean isValid(Object value, ConstraintValidatorContext context)
//    {
//        return false;
//    }

    private List<String> acceptedValues;

    @Override
    public void initialize(IsEnum annotation)
    {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context)
    {
        if (value == null)
        {
            return true; // Use @NotNull separately if required
        }
        return acceptedValues.contains(value.toString());
    }
}
