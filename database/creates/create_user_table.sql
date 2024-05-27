CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    date_birth DATE NOT NULL,
    gender VARCHAR(10),
	status VARCHAR(1) NOT NULL,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_alt TIMESTAMP
);
