package id.my.agungdh.api.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.Map;

@Controller
public class GraphQLExceptionAdvice {

    @GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraint(ConstraintViolationException ex, DataFetchingEnvironment env) {
        List<Map<String, Object>> violations = ex.getConstraintViolations().stream()
                .map(v -> Map.<String, Object>of(
                        "path", simplifyPath(v.getPropertyPath()),
                        "message", v.getMessage(),
                        "rejected", v.getInvalidValue()
                ))
                .toList();

        return GraphqlErrorBuilder.newError(env)
                .message("Validation failed")
                .errorType(ErrorType.BAD_REQUEST)
                .extensions(Map.of(
                        "code", "BAD_USER_INPUT",
                        "violations", violations
                ))
                .build();
    }

    // Optional: kalau ada binding/type mismatch saat map @Argument -> object
    @GraphQlExceptionHandler(BindException.class)
    public GraphQLError handleBind(BindException ex, DataFetchingEnvironment env) {
        List<Map<String, Object>> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.<String, Object>of(
                        "path", fe.getField(),             // mis. "input.releaseDate"
                        "message", fe.getDefaultMessage(), // pesan kesalahan
                        "rejected", fe.getRejectedValue()  // nilai yang ditolak
                ))
                .toList();

        return GraphqlErrorBuilder.newError(env)
                .message("Validation failed")
                .errorType(ErrorType.BAD_REQUEST)
                .extensions(Map.of(
                        "code", "BAD_USER_INPUT",
                        "violations", violations
                ))
                .build();
    }

    // Ubah PropertyPath Bean Validation jadi "input.xxx[0].yyy"
    private String simplifyPath(Path path) {
        StringBuilder sb = new StringBuilder();
        boolean inInput = false;

        for (Path.Node node : path) {
            String name = node.getName();
            if (name == null) continue;

            if (!inInput) {
                if ("input".equals(name) || "arg0".equals(name)) {
                    sb.append("input");
                    inInput = true;
                }
                // lewati nama method/class dll sampai ketemu input/arg0
                continue;
            }

            // setelah "input"
            if (sb.length() > 0) sb.append('.');
            sb.append(name);
            if (node.getIndex() != null) {
                sb.append('[').append(node.getIndex()).append(']');
            }
        }

        return sb.length() > 0 ? sb.toString() : path.toString(); // fallback
    }
}
