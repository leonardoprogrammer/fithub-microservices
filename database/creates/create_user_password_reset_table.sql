CREATE TABLE user_password_reset (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_login_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    send_date TIMESTAMP,
    link VARCHAR(255) NOT NULL,
    date_expiration TIMESTAMP NOT NULL,
    reset BOOLEAN NOT NULL,
    reset_date TIMESTAMP,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_login_id) REFERENCES user_login(id)
);
