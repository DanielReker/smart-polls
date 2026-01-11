CREATE EXTENSION IF NOT EXISTS vector;


CREATE SEQUENCE IF NOT EXISTS answer_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS choice_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS poll_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS question_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS submission_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS text_answer_tag_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS text_question_summary_tag_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS user_session_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE answer
(
    id            BIGINT      NOT NULL,
    dtype         VARCHAR(31) NOT NULL,
    submission_id BIGINT      NOT NULL,
    question_id   BIGINT      NOT NULL,
    value         TEXT,
    choice_id     BIGINT,
    CONSTRAINT pk_answer PRIMARY KEY (id)
);

CREATE TABLE answer_choice
(
    answer_id BIGINT NOT NULL,
    choice_id BIGINT NOT NULL
);

CREATE TABLE choice
(
    id          BIGINT       NOT NULL,
    question_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    position    INTEGER      NOT NULL,
    CONSTRAINT pk_choice PRIMARY KEY (id)
);

CREATE TABLE poll
(
    id           BIGINT                   NOT NULL,
    status       VARCHAR(50)              NOT NULL,
    name         VARCHAR(255)             NOT NULL,
    description  TEXT,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    owner_id     BIGINT                   NOT NULL,
    CONSTRAINT pk_poll PRIMARY KEY (id)
);

CREATE TABLE question
(
    id          BIGINT       NOT NULL,
    dtype       VARCHAR(31)  NOT NULL,
    poll_id     BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    position    INTEGER      NOT NULL,
    is_required BOOLEAN      NOT NULL,
    max_length  INTEGER,
    CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE submission
(
    id           BIGINT                   NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    poll_id      BIGINT                   NOT NULL,
    owner_id     BIGINT                   NOT NULL,
    CONSTRAINT pk_submission PRIMARY KEY (id)
);

CREATE TABLE text_answer_tag
(
    id        BIGINT       NOT NULL,
    answer_id BIGINT       NOT NULL,
    name      VARCHAR(255) NOT NULL,
    embedding VECTOR(768) NOT NULL,
    CONSTRAINT pk_text_answer_tag PRIMARY KEY (id)
);

CREATE TABLE text_question_summary_tag
(
    id          BIGINT       NOT NULL,
    question_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    count       BIGINT       NOT NULL,
    CONSTRAINT pk_text_question_summary_tag PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role    VARCHAR(255)
);

CREATE TABLE user_session
(
    id           BIGINT       NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expiry_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    token_id     UUID         NOT NULL,
    token_hash   VARCHAR(255) NOT NULL,
    user_id      BIGINT       NOT NULL,
    CONSTRAINT pk_user_session PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            BIGINT                   NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE NOT NULL,
    login         VARCHAR(255),
    password_hash VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_login UNIQUE (login);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_CHOICE FOREIGN KEY (choice_id) REFERENCES choice (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_SUBMISSION FOREIGN KEY (submission_id) REFERENCES submission (id);

ALTER TABLE choice
    ADD CONSTRAINT FK_CHOICE_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE poll
    ADD CONSTRAINT FK_POLL_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_POLL FOREIGN KEY (poll_id) REFERENCES poll (id);

ALTER TABLE submission
    ADD CONSTRAINT FK_SUBMISSION_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE submission
    ADD CONSTRAINT FK_SUBMISSION_ON_POLL FOREIGN KEY (poll_id) REFERENCES poll (id);

ALTER TABLE text_answer_tag
    ADD CONSTRAINT FK_TEXT_ANSWER_TAG_ON_ANSWER FOREIGN KEY (answer_id) REFERENCES answer (id);

ALTER TABLE text_question_summary_tag
    ADD CONSTRAINT FK_TEXT_QUESTION_SUMMARY_TAG_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE user_session
    ADD CONSTRAINT FK_USER_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE answer_choice
    ADD CONSTRAINT fk_anscho_on_choice_entity FOREIGN KEY (choice_id) REFERENCES choice (id);

ALTER TABLE answer_choice
    ADD CONSTRAINT fk_anscho_on_multi_choice_answer_entity FOREIGN KEY (answer_id) REFERENCES answer (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id);