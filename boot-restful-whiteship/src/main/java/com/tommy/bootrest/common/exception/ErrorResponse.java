package com.tommy.bootrest.common.exception;

import com.tommy.bootrest.index.IndexController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 현재 Validation에서 넘어오는 값들에서 error Default Message를 정의해주고 있다.
 * 외부에서 주입받는 e.getMessage()를 받게되면 너무 디테일한 Message 정보가 나오게 된다.
 * 이를 노출하는게 좋을지는 좀 더 생각을 해보자.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse extends RepresentationModel<ErrorResponse> {

    private String message;
    private int status;
    private List<FieldError> errors;
    private LocalDateTime timestamp;

    private ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.errors = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
        this.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    private ErrorResponse(String message, int status, List<FieldError> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
        this.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    public static ErrorResponse of(String message, int status) {
        return new ErrorResponse(message, status);
    }

    public static ErrorResponse of(String message, int status, BindingResult bindingResult) {
        return new ErrorResponse(message, status, FieldError.of(bindingResult));
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
