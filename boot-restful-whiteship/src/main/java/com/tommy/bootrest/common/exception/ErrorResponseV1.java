package com.tommy.bootrest.common.exception;

import com.tommy.bootrest.index.IndexController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseV1 extends RepresentationModel<ErrorResponseV1> {

    private String message;
    private int status;
    private List<FieldError> errors;

    private ErrorResponseV1(String message, int status) {
        this.message = message;
        this.status = status;
        this.errors = new ArrayList<>();
        this.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    private ErrorResponseV1(String message, int status, List<FieldError> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    public static ErrorResponseV1 of(String message, int status) {
        return new ErrorResponseV1(message, status);
    }

    public static ErrorResponseV1 of(String message, int status, BindingResult bindingResult) {
        return new ErrorResponseV1(message, status, FieldError.of(bindingResult));
    }

    /**
     * 필요 시 error.getCode()를 이용하여 ErrorCode를 추가할 수 있음.
     * 필요 시 error.objectName()을 이용하여 Error가 발생한 ObjectName을 추가할 수 있음.
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FieldError {

        private static final String EMPTY_MESSAGE = "";

        private String field;
        private String rejectedValue;
        private String defaultMessage;

        private FieldError(String field, String rejectedValue, String defaultMessage) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.defaultMessage = defaultMessage;
        }

        public static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(fieldError -> new FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? EMPTY_MESSAGE : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
