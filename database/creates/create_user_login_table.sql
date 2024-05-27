CREATE TABLE user_login (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_alt TIMESTAMP
);
