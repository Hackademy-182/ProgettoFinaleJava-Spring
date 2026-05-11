CREATE TABLE career_request(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    body TEXT,
    user_id BIGINT,
    Foreign Key (user_id) REFERENCES users (id),
    role_id BIGINT,
    Foreign Key (role_id) REFERENCES roles (id),
    is_checked BOOLEAN
)