CREATE TABLE IF NOT EXISTS users(
    id bigserial PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks(
    id bigserial PRIMARY KEY,
    title VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR NOT NULL,
    created_date DATE NOT NULL,
    deadline DATE NOT NULL,
    status VARCHAR(100) NOT NULL,
    priority VARCHAR(100) NOT NULL,
    author_id BIGINT, FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
    assignee_id BIGINT, FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS comments(
    id bigserial PRIMARY KEY,
    content VARCHAR NOT NULL,
    created_date DATE NOT NULL,
    author_id BIGINT,
    task_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);