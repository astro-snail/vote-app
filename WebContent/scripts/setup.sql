CREATE TABLE candidates (
	candidate_id SMALLINT NOT NULL,
	candidate_name VARCHAR(50),
	PRIMARY KEY(candidate_id)
);

CREATE TABLE users (
	user_id BIGINT NOT NULL,
	vote_count SMALLINT NOT NULL,
	PRIMARY KEY(user_id));

CREATE TABLE votes (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    candidate_id SMALLINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_candidate_id ON votes(candidate_id);

INSERT INTO candidates (candidate_id, candidate_name) VALUES ("1", "Mickey Mouse");
INSERT INTO candidates (candidate_id, candidate_name) VALUES ("2", "Peppa Pig");
INSERT INTO candidates (candidate_id, candidate_name) VALUES ("3", "Snow White");
INSERT INTO candidates (candidate_id, candidate_name) VALUES ("4", "Daffy Duck");
INSERT INTO candidates (candidate_id, candidate_name) VALUES ("5", "Cinderella");
INSERT INTO candidates (candidate_id, candidate_name) VALUES ("6", "Puss in Boots");
