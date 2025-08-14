/* (C) 2025 Agung DH */
package id.my.agungdh.api.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GraphQlErrorInterceptor implements WebGraphQlInterceptor {

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    return chain
        .next(request)
        .map(
            response -> {
              var original = response.getErrors();
              if (original.isEmpty()) return response;

              List<GraphQLError> normalized = original.stream().map(this::normalize).toList();

              return response.transform(b -> b.errors(normalized));
            });
  }

  private GraphQLError normalize(ResponseError err) {
    String classification = String.valueOf(err.getErrorType());
    boolean isValidation = "ValidationError".equalsIgnoreCase(classification);

    Map<String, Object> ext = new LinkedHashMap<>();
    if (err.getExtensions() != null) ext.putAll(err.getExtensions());
    ext.put("code", isValidation ? "VALIDATION_ERROR" : "INTERNAL_ERROR");
    ext.put("classification", classification);

    String raw = err.getMessage() == null ? "" : err.getMessage();
    if (isValidation) {
      // Ambil tipe validasi dari "(WrongType@...)" → "WrongType"
      String vType = extractBetween(raw, "\\(", "@");
      if (vType != null && !vType.isBlank()) {
        ext.put("validationType", vType);
      }

      // Jika pesan mengandung "missing required fields '[a, b, c]'"
      List<String> missing = extractBracketList(raw);
      if (!missing.isEmpty()) {
        List<Map<String, Object>> errors =
            missing.stream()
                .map(
                    f ->
                        Map.<String, Object>of(
                            "field", f,
                            "message", "is required",
                            "reason", "missing_required_field"))
                .toList();
        ext.put("errors", errors);
      }

      return GraphqlErrorBuilder.newError()
          .message("Validation failed")
          .locations(err.getLocations())
          .path(err.getParsedPath())
          .errorType(ErrorType.BAD_REQUEST)
          .extensions(ext)
          .build();
    }

    // Non-validation → tetap seperti semula tapi tambahkan code
    return GraphqlErrorBuilder.newError()
        .message(raw)
        .locations(err.getLocations())
        .path(err.getParsedPath())
        .errorType(ErrorType.INTERNAL_ERROR)
        .extensions(ext)
        .build();
  }

  /**
   * Ambil string antara firstRegexStart dan first occurrence of endChar, mis. "(WrongType@...)" →
   * "WrongType"
   */
  private String extractBetween(String text, String startRegex, String endCharLiteral) {
    Pattern p = Pattern.compile(startRegex);
    Matcher m = p.matcher(text);
    if (m.find()) {
      int start = m.end();
      int end = text.indexOf(endCharLiteral, start);
      if (end > start) return text.substring(start, end);
    }
    return null;
  }

  /**
   * Ambil daftar dalam bracket terakhir, mis. "... '[name, description]'" → ["name","description"]
   */
  private List<String> extractBracketList(String text) {
    int l = text.lastIndexOf('[');
    int r = text.indexOf(']', Math.max(l, 0));
    if (l >= 0 && r > l) {
      String inside = text.substring(l + 1, r);
      String[] parts = inside.split(",");
      List<String> out = new ArrayList<>();
      for (String p : parts) {
        String v = p.trim().replaceAll("^'+|'+$", "");
        if (!v.isEmpty()) out.add(v);
      }
      return out;
    }
    return Collections.emptyList();
  }
}
