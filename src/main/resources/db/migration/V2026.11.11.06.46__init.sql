CREATE TABLE answer
(
    id            UUID        NOT NULL,
    dtype         VARCHAR(31) NOT NULL,
    version       INTEGER     NOT NULL,
    submission_id UUID        NOT NULL,
    question_id   UUID        NOT NULL,
    choice_id     UUID,
    value         TEXT,
    CONSTRAINT pk_answer PRIMARY KEY (id)
);

CREATE TABLE answer_choice
(
    answer_id UUID NOT NULL,
    choice_id UUID NOT NULL
);

CREATE TABLE choice
(
    id          UUID         NOT NULL,
    version     INTEGER      NOT NULL,
    question_id UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    position    INTEGER      NOT NULL,
    is_correct  BOOLEAN      NOT NULL,
    CONSTRAINT pk_choice PRIMARY KEY (id)
);

CREATE TABLE hint
(
    id               UUID           NOT NULL,
    version          INTEGER        NOT NULL,
    question_id      UUID           NOT NULL,
    sequential_order INTEGER        NOT NULL,
    score_penalty    numeric(10, 3) NOT NULL,
    text             TEXT           NOT NULL,
    CONSTRAINT pk_hint PRIMARY KEY (id)
);

CREATE TABLE question
(
    id                    UUID           NOT NULL,
    dtype                 VARCHAR(31)    NOT NULL,
    version               INTEGER        NOT NULL,
    quiz_id               UUID           NOT NULL,
    name                  VARCHAR(255)   NOT NULL,
    description           TEXT,
    position              INTEGER        NOT NULL,
    is_required           BOOLEAN        NOT NULL,
    max_score             numeric(10, 3) NOT NULL,
    max_length            INTEGER,
    sample_correct_answer TEXT,
    CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE quiz
(
    id           UUID                     NOT NULL,
    version      INTEGER                  NOT NULL,
    status       VARCHAR(50)              NOT NULL,
    name         VARCHAR(255)             NOT NULL,
    description  TEXT,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    owner_id     UUID                     NOT NULL,
    CONSTRAINT pk_quiz PRIMARY KEY (id)
);

CREATE TABLE submission
(
    id           UUID                     NOT NULL,
    version      INTEGER                  NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    quiz_id      UUID                     NOT NULL,
    owner_id     UUID                     NOT NULL,
    CONSTRAINT pk_submission PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id UUID NOT NULL,
    role    VARCHAR(255)
);

CREATE TABLE user_session
(
    id           UUID                        NOT NULL,
    version      INTEGER                     NOT NULL,
    created_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    expiry_date  TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    token_id     UUID                        NOT NULL,
    token_hash   VARCHAR(255)                NOT NULL,
    user_id      UUID                        NOT NULL,
    CONSTRAINT pk_user_session PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID                     NOT NULL,
    version       INTEGER                  NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE NOT NULL,
    login         VARCHAR(255),
    password_hash VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_login UNIQUE (login);

CREATE UNIQUE INDEX idx_users_login ON users (login);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_CHOICE FOREIGN KEY (choice_id) REFERENCES choice (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_SUBMISSION FOREIGN KEY (submission_id) REFERENCES submission (id);

CREATE INDEX idx_answer_submission_id ON answer (submission_id);

ALTER TABLE choice
    ADD CONSTRAINT FK_CHOICE_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

CREATE INDEX idx_choice_question_id ON choice (question_id);

ALTER TABLE hint
    ADD CONSTRAINT FK_HINT_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_QUIZ FOREIGN KEY (quiz_id) REFERENCES quiz (id);

CREATE INDEX idx_question_quiz_id ON question (quiz_id);

ALTER TABLE quiz
    ADD CONSTRAINT FK_QUIZ_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

CREATE INDEX idx_quiz_owner_id ON quiz (owner_id);

ALTER TABLE submission
    ADD CONSTRAINT FK_SUBMISSION_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE submission
    ADD CONSTRAINT FK_SUBMISSION_ON_QUIZ FOREIGN KEY (quiz_id) REFERENCES quiz (id);

CREATE INDEX idx_submission_quiz_id ON submission (quiz_id);

ALTER TABLE user_session
    ADD CONSTRAINT FK_USER_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE answer_choice
    ADD CONSTRAINT fk_anscho_on_choice_entity FOREIGN KEY (choice_id) REFERENCES choice (id);

ALTER TABLE answer_choice
    ADD CONSTRAINT fk_anscho_on_multi_choice_answer_entity FOREIGN KEY (answer_id) REFERENCES answer (id);

CREATE INDEX idx_answer_choice_answer_id ON answer_choice (answer_id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id);