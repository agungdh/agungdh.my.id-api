-- Membuat tabel project
CREATE TABLE project (
                         id SERIAL PRIMARY KEY,
                         uuid UUID DEFAULT gen_random_uuid(),
                         name VARCHAR(255),
                         description TEXT,
                         release_date DATE,
                         created_at INT,
                         updated_at INT
);

-- Membuat hash index untuk kolom uuid
CREATE INDEX idx_project_uuid ON project USING HASH (uuid);
