CREATE TABLE IF NOT EXISTS role (
    id          BIGINT AUTO_INCREMENT NOT NULL,
    role        VARCHAR(255)          NULL,
    created_at  DATETIME              NULL,
    updated_at  DATETIME              NULL,
    is_deleted  BIT(1)                NOT NULL DEFAULT 0,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user (
    id          BIGINT AUTO_INCREMENT NOT NULL,
    email       VARCHAR(255)          NULL,
    password    VARCHAR(255)          NULL,
    created_at  DATETIME              NULL,
    updated_at  DATETIME              NULL,
    is_deleted  BIT(1)                NOT NULL DEFAULT 0,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id  BIGINT NOT NULL,
    role_id  BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS session (
    id             BIGINT       AUTO_INCREMENT NOT NULL,
    token          VARCHAR(500) NULL,
    expiring_at    DATETIME     NULL,
    user_id        BIGINT       NULL,
    session_status TINYINT      NULL,
    created_at     DATETIME     NULL,
    updated_at     DATETIME     NULL,
    is_deleted     BIT(1)       NOT NULL DEFAULT 0,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE session ADD CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES user (id);
