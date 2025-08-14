/* (C)2025 */
package id.my.agungdh.api.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

@Component
public class GraphQLValidationExceptionAdvice {

    @GraphQlExceptionHandler(BindException.class)
    public GraphQLError handleBindException(BindException ex, DataFetchingEnvironment env) {
        List<Map<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toMap)
                .toList();

        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation failed")
                .extensions(Map.of(
                        "code", "VALIDATION_ERROR",
                        "errors", errors
                ))
                .build();
    }

    @GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraintViolation(ConstraintViolationException ex, DataFetchingEnvironment env) {
        List<Map<String, Object>> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> Map.<String, Object>of(
                        "field", v.getPropertyPath().toString(),
                        "message", v.getMessage(),
                        "rejectedValue", v.getInvalidValue()
                ))
                .toList();

        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation failed")
                .extensions(Map.of(
                        "code", "VALIDATION_ERROR",
                        "errors", errors
                ))
                .build();
    }

    private Map<String, Object> toMap(FieldError fe) {
        return Map.of(
                "field", fe.getField(),
                "message", fe.getDefaultMessage(),
                "rejectedValue", fe.getRejectedValue()
        );
    }
}
