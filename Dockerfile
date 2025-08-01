# --- Stage 1: Build native image with GraalVM CE ---
FROM ghcr.io/graalvm/native-image-community:21 AS builder
WORKDIR /workspace

# Gunakan arg untuk -march
ARG MARCH=x86-64-v1

# Salin file Maven
COPY mvnw pom.xml ./
COPY .mvn/ .mvn/

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src/ src/

# Build native image DENGAN --march, BUKAN --llvm-args
RUN ./mvnw -Pnative native:compile \
  -DskipTests \
  -Dspring.native.mode=compatibility \
  -Dgraalvm.native.additional-build-args="--march=${MARCH}"

# --- Stage 2: Runtime image (minimal) ---
FROM debian:bookworm-slim AS runtime

RUN apt-get update && \
    apt-get install -y --no-install-recommends ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Salin binary dari build stage
COPY --from=builder /workspace/target/api ./

ENTRYPOINT ["./api"]
