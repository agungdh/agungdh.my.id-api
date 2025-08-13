package id.my.agungdh.api.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.persistence.EntityNotFoundException; // contoh; sesuaikan dengan exception-mu
import java.util.Map;

@ControllerAdvice // berlaku global untuk semua @Controller GraphQL
public class GraphQLExceptionAdvice {

    @GraphQlExceptionHandler(EntityNotFoundException.class)
    public GraphQLError handleNotFound(EntityNotFoundException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .extensions(Map.of("code", "NOT_FOUND"))
                .build();
    }

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleBadRequest(IllegalArgumentException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .extensions(Map.of("code", "BAD_REQUEST"))
                .build();
    }

    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleAll(Exception ex, DataFetchingEnvironment env) {
        // log ex di sini kalau perlu
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Something went wrong")
                .extensions(Map.of("code", "INTERNAL_ERROR"))
                .build();
    }

    @GraphQlExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public GraphQLError handleValidation(jakarta.validation.ConstraintViolationException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation failed")
                .extensions(Map.of("code", "VALIDATION_ERROR"))
                .build();
    }

    @GraphQlExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public GraphQLError handleIntegrity(org.springframework.dao.DataIntegrityViolationException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Data integrity violation")
                .extensions(Map.of("code", "DATA_INTEGRITY"))
                .build();
    }
}
