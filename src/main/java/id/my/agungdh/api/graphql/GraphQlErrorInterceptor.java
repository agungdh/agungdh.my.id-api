/* (C)2025 */
package id.my.agungdh.api.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GraphQlErrorInterceptor implements WebGraphQlInterceptor {

  // Contoh pesan: ... is missing required fields '[description, name]'
  private static final Pattern MISSING_FIELDS =
      Pattern.compile("missing required fields '\\[(.*?)\\]'");

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    return chain
        .next(request)
        .map(
            response -> {
              if (response.getErrors().isEmpty()) return response;

              List<GraphQLError> formatted =
                  response.getErrors().stream().map(this::toFormattedError).toList();

              return response.transform(b -> b.errors(formatted));
            });
  }

  private GraphQLError toFormattedError(ResponseError err) {
    Map<String, Object> ext = new LinkedHashMap<>();
    if (err.getExtensions() != null) ext.putAll(err.getExtensions());

    // GraphQL-java kirim "classification":"ValidationError" untuk error validasi request-level
    String classification = String.valueOf(ext.getOrDefault("classification", ""));
    if ("ValidationError".equals(classification)) {
      List<Map<String, Object>> violations = new ArrayList<>();

      // Ekstrak field yang hilang dari pesan
      Matcher m = MISSING_FIELDS.matcher(err.getMessage());
      if (m.find()) {
        String inside = m.group(1); // "description, name"
        Arrays.stream(inside.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .forEach(
                field ->
                    violations.add(
                        Map.of(
                            "path",
                            "input." + field.replace("'", ""),
                            "message",
                            "Field is required")));
      }

      ext.put("code", "GRAPHQL_VALIDATION");
      if (!violations.isEmpty()) {
        ext.put("violations", violations);
      }

      return GraphqlErrorBuilder.newError()
          .message("Validation failed")
          .locations(err.getLocations())
          .path(err.getParsedPath())
          .extensions(ext)
          .build();
    }

    // Default: biarkan seperti aslinya tapi beri code standar
    ext.putIfAbsent("code", "INTERNAL_ERROR");
    return GraphqlErrorBuilder.newError()
        .message(err.getMessage())
        .locations(err.getLocations())
        .path(err.getParsedPath())
        .extensions(ext)
        .build();
  }
}
