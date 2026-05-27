
CREATE TABLE IF NOT EXISTS project (
    id BIGSERIAL PRIMARY KEY,  -- <-- Cambiado a BIGSERIAL (Mapea con Long en Java)
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TYPE task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE task_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,  -- <-- Cambiado a BIGSERIAL por consistencia
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status task_status NOT NULL,
    priority task_priority NOT NULL,
    project_id BIGINT REFERENCES project(id) ON DELETE CASCADE -- <-- Ahora sí coinciden (BIGINT apunta a BIGINT)
);