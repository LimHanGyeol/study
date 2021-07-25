package com.tommy.bootrest.common.exception;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tommy.bootrest.index.IndexController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends RepresentationModel<ErrorResource> {

    @JsonUnwrapped
    private Errors errors;

    public ErrorResource(Errors errors) {
        this.errors = errors;
        this.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    public Errors getErrors() {
        return errors;
    }
}
/*
 * 이 부분은 추후 리팩토링 시 link를 RepresentationModel을 상속하여 사용하지 않는 것을 고려
 */
