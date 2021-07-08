package com.tommy.bootrest.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeStartArray();
        writeFieldErrorsObject(errors, generator);
        writeGlobalErrorsObject(errors, generator);
        generator.writeEndArray();
    }

    private void writeFieldErrorsObject(Errors errors, JsonGenerator generator) {
        errors.getFieldErrors().forEach(error -> {
            try {
                generator.writeStartObject();
                generator.writeStringField("field", error.getField());
                generator.writeStringField("objectName", error.getObjectName());
                generator.writeStringField("code", error.getCode());
                generator.writeStringField("defaultMessage", error.getDefaultMessage());

                Object rejectedValue = error.getRejectedValue();
                if (rejectedValue != null) {
                    generator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                generator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeGlobalErrorsObject(Errors errors, JsonGenerator generator) {
        errors.getGlobalErrors().forEach(error -> {
            try {
                generator.writeStartObject();
                generator.writeStringField("objectName", error.getObjectName());
                generator.writeStringField("code", error.getCode());
                generator.writeStringField("defaultMessage", error.getDefaultMessage());
                generator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

/*
 * Validation의 Errors는 Java POJO Bean으로 이루어져있지 않기 때문에,
 * ResponseEntity<>에 타입 설정 및 Json 직렬화가 되지 않는다.
 * 이를 위해 Serializer를 설정한다.
 *
 * errors 안에는 error가 여러개라서 배열로 담아주기 위해 startArray, endArray를 설정한다.
 * errors에는 크게 fieldError와 globalError가 있다.
 * validation에서 rejectValue를 쓰면 fieldError로 들어간다.
 * 여러개의 값이 조합된 에러가 발생할 경우 reject로 globalError를 설정할 수 있다.
 *
 * 만들어진 Serializer를 ObjectMapper에 등록해야 하는데,
 * SpringBoot가 제공하는 JsonComponent를 이용하여 쉽게 등록할 수 있다.
 */
