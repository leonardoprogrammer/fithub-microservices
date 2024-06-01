CREATE TABLE user_roles (
    user_id UUID REFERENCES users(id),
    role_id INT REFERENCES role(id),
    PRIMARY KEY(user_id, role_id)
);