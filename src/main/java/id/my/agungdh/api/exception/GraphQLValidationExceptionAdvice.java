/* (C) 2025 Agung DH */
package id.my.agungdh.api.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Map;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQLValidationExceptionAdvice {

  @GraphQlExceptionHandler(BindException.class)
  public GraphQLError handleBindException(BindException ex, DataFetchingEnvironment env) {
    List<Map<String, Object>> errors =
        ex.getBindingResult().getFieldErrors().stream().map(this::toMap).toList();

    return GraphqlErrorBuilder.newError(env)
        .errorType(ErrorType.BAD_REQUEST)
        .message("Validation failed")
        .extensions(Map.of("code", "VALIDATION_ERROR", "errors", errors))
        .build();
  }

  @GraphQlExceptionHandler(ConstraintViolationException.class)
  public GraphQLError handleConstraintViolation(
      ConstraintViolationException ex, DataFetchingEnvironment env) {
    List<Map<String, Object>> errors =
        ex.getConstraintViolations().stream()
            .map(
                v ->
                    Map.<String, Object>of(
                        "field", leaf(v.getPropertyPath()),
                        "message", v.getMessage(),
                        "rejectedValue", v.getInvalidValue()))
            .toList();

    return GraphqlErrorBuilder.newError(env)
        .errorType(ErrorType.BAD_REQUEST)
        .message("Validation failed")
        .extensions(Map.of("code", "VALIDATION_ERROR", "errors", errors))
        .build();
  }

  private Map<String, Object> toMap(FieldError fe) {
    assert fe.getDefaultMessage() != null;
    assert fe.getRejectedValue() != null;
    return Map.of(
        "field", fe.getField(),
        "message", fe.getDefaultMessage(),
        "rejectedValue", fe.getRejectedValue());
  }

  private String leaf(Path path) {
    String last = null;
    for (Path.Node n : path) {
      if (n.getName() != null && !n.getName().isBlank()) last = n.getName();
    }
    return last;
  }
}
