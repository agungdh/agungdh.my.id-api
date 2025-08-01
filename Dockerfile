# --- Stage 1: Build native image with GraalVM CE (open-source) ---
FROM ghcr.io/graalvm/native-image-community:21 AS builder
WORKDIR /workspace

# 1. Salin Maven Wrapper dan pom.xml
COPY mvnw pom.xml ./
COPY .mvn/ .mvn/

# 2. Unduh dependensi secara offline
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# 3. Salin kode sumber dan build native image
COPY src/ src/
# Profile 'native' sudah terkonfigurasi di pom.xml untuk Spring AOT
RUN ./mvnw -Pnative native:compile -DskipTests -Dspring.native.mode=compatibility

# --- Stage 2: Runtime dengan Alpine (open-source) ---
FROM alpine:3 AS runtime
# Pasang sertifikat TLS (CA certificates)
RUN apk add --no-cache ca-certificates

WORKDIR /app

# Salin binary hasil build
# Ganti 'myapp' sesuai <finalName> di pom.xml, misal: 'demo-spring-native'
COPY --from=builder /workspace/target/myapp .

# Jalankan aplikasi
ENTRYPOINT ["./myapp"]
