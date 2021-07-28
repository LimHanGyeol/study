package com.tommy.bootrest.event.dto.validator.annotation;

import com.tommy.bootrest.event.dto.validator.EventCreateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EventCreateValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventCreateValid {

    String message() default "Price fields is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
