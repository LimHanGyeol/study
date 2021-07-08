package com.tommy.bootrest.event.dto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventCreateRequest eventCreateRequest, Errors errors) {
        validatePrice(eventCreateRequest, errors);
        validateEndEventDateTime(eventCreateRequest, errors);
        // beginEventDateTime 해야함
        // closeEnrollmentDateTime 해야함
    }

    private void validatePrice(EventCreateRequest eventCreateRequest, Errors errors) {
        int basePrice = eventCreateRequest.getBasePrice();
        int maxPrice = eventCreateRequest.getMaxPrice();
        if (basePrice > maxPrice && maxPrice > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
        }
    }

    private void validateEndEventDateTime(EventCreateRequest eventCreateRequest, Errors errors) {
        LocalDateTime endEventDateTime = eventCreateRequest.getEndEventDateTime();
        LocalDateTime beginEventDateTime = eventCreateRequest.getBeginEventDateTime();
        LocalDateTime closeEnrollmentDateTime = eventCreateRequest.getCloseEnrollmentDateTime();
        LocalDateTime beginEnrollmentDateTime = eventCreateRequest.getBeginEnrollmentDateTime();

        if (endEventDateTime.isBefore(beginEventDateTime)
                || endEventDateTime.isBefore(closeEnrollmentDateTime)
                || endEventDateTime.isBefore(beginEnrollmentDateTime)) {
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
        }
    }
}
