-- Membuat tabel project
CREATE TABLE project
(
    id           BIGSERIAL PRIMARY KEY,
    uuid         UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    release_date DATE,
    created_at   TIMESTAMPTZ,
    updated_at   TIMESTAMPTZ
);

-- Membuat hash index untuk kolom uuid
CREATE INDEX idx_project_uuid ON project USING HASH (uuid);
