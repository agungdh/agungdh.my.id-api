FROM ghcr.io/graalvm/native-image-community:21 AS builder
WORKDIR /workspace

ARG MARCH=x86-64-v1

COPY mvnw pom.xml ./
COPY .mvn/ .mvn/
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src/ src/

RUN ./mvnw -Pnative native:compile \
  -DskipTests \
  -Dspring.native.mode=compatibility \
  -Dgraalvm.native.additional-build-args="\
    --no-fallback \
    --enable-hosted-runtime-option=-XX:CPUFeatures=none \
    --enable-hosted-runtime-option=-XX:+UseSSE=2 \
    --enable-hosted-runtime-option=-XX:-UseAVX \
    --enable-hosted-runtime-option=-XX:-UseFMA"

# Runtime image
FROM debian:bookworm-slim AS runtime
RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=builder /workspace/target/api ./
ENTRYPOINT ["./api"]
