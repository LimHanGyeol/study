package com.tommy.bootrest.event.dto.validator;

import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.validator.annotation.EventCreateValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * ConstraintValidator를 사용하면 한 곳에서 Validation의 책임을 부여할 수 있지만,
 * Annotataion 기반의 Validation을 온전히 활용하진 못한다.
 * 각 활용시 어떤 점이 더 좋을지 고민하며 사용할 필요가 있을 것 같다.
 */
public class EventCreateValidator implements ConstraintValidator<EventCreateValid, EventCreateRequest> {

    @Override
    public void initialize(EventCreateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        if (!validateName(eventCreate, context)) {
            return false;
        }
        if (!validateDescription(eventCreate, context)) {
            return false;
        }
        if (!validateLimitOfEnrollment(eventCreate, context)) {
            return false;
        }
        if (!validatePrice(eventCreate, context)) {
            return false;
        }
        return validateEventDateTime(eventCreate, context);
    }

    private boolean validateName(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        if (eventCreate.getName() == null) {
            addConstraintViolation(context, "name is null", "name");
            return false;
        }
        return true;
    }

    private boolean validateDescription(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        if (eventCreate.getDescription() == null) {
            addConstraintViolation(context, "description is null", "description");
            return false;
        }
        return true;
    }

    private boolean validateLimitOfEnrollment(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        if (eventCreate.getLimitOfEnrollment() < 0) {
            addConstraintViolation(context, "limitOfEnrollment is wrong", "limitOfEnrollment");
            return false;
        }
        return true;
    }

    private boolean validateEventDateTime(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        LocalDateTime endEventDateTime = eventCreate.getEndEventDateTime();
        if (endEventDateTime == null) {
            addConstraintViolation(context, "endEventDateTime is null", "endEventDateTime");
            return false;
        }
        LocalDateTime beginEventDateTime = eventCreate.getBeginEventDateTime();
        if (beginEventDateTime == null) {
            addConstraintViolation(context, "beginEventDateTime is null", "beginEventDateTime");
            return false;
        }
        LocalDateTime closeEnrollmentDateTime = eventCreate.getCloseEnrollmentDateTime();
        if (closeEnrollmentDateTime == null) {
            addConstraintViolation(context, "closeEnrollmentDateTime", "closeEnrollmentDateTime");
            return false;
        }
        LocalDateTime beginEnrollmentDateTime = eventCreate.getBeginEnrollmentDateTime();
        if (beginEnrollmentDateTime == null) {
            addConstraintViolation(context, "beginEnrollmentDateTime", "beginEnrollmentDateTime");
            return false;
        }

        if (endEventDateTime.isBefore(beginEventDateTime) ||
                endEventDateTime.isBefore(closeEnrollmentDateTime) ||
                endEventDateTime.isBefore(beginEnrollmentDateTime)) {
            addConstraintViolation(context, "EndEventDateTime is wrong", "endEventDateTime");
            return false;
        }
        return true;
    }

    private boolean validatePrice(EventCreateRequest eventCreate, ConstraintValidatorContext context) {
        int basePrice = eventCreate.getBasePrice();
        int maxPrice = eventCreate.getMaxPrice();

        if (basePrice < 0) {
            addConstraintViolation(context, "basePrice is wrong", "basePrice");
            return false;
        }
        if (maxPrice < 0) {
            addConstraintViolation(context, "maxPrice is wrong", "maxPrice");
            return false;
        }

        if (basePrice > maxPrice && maxPrice > 0) {
            addConstraintViolation(context, "MaxPrice is wrong", "maxPrice");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage, String wrongField) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(wrongField)
                .addConstraintViolation();
    }
}
