
CREATE TABLE users(
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL
);

CREATE TABLE user_roles(
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);