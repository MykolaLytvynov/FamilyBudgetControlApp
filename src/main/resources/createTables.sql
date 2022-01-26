create table roles
(
    id   SERIAL PRIMARY KEY,
    name varchar(35) NOT NULL
);

create table account
(
    id     SERIAL PRIMARY KEY,
    number int NOT NULL,
    amount int
);

create table family
(
    id                          SERIAL PRIMARY KEY,
    name                        varchar(35) NOT NULL,
    restrictions_maximum_amount int,
    account_id                  int         NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id)
);

create table users
(
    id                          SERIAL PRIMARY KEY,
    username                    varchar(35)  NOT NULL,
    email                       varchar(50)  NOT NULL,
    password                    varchar(150) NOT NULL,
    restrictions_maximum_amount int,
    family_id                   int          NOT NULL,
    FOREIGN KEY (family_id) REFERENCES family (id)
);



create table user_roles
(
    user_id int NOT NULL,
    role_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES users (id)
);

INSERT INTO roles (name)
VALUES ('ROLE_USER');
INSERT INTO roles (name)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (name)
VALUES ('ROLE_GLOBAL_ADMIN');

INSERT INTO account (number, amount)
VALUES (123456789, 1000);
INSERT INTO account (number, amount)
VALUES (999999999, 500);

INSERT INTO family (name, restrictions_maximum_amount, account_id)
VALUES ('family1', 0, 1);
INSERT INTO family (name, restrictions_maximum_amount, account_id)
VALUES ('family2', 100, 2);

-- password -> 1234
INSERT INTO users(username, email, password, restrictions_maximum_amount, family_id)
VALUES ('user1', 'er@ja.tu',
        '$2a$04$pjri1KTInYfhBZh2iax7E.riLi/wgULK0RGAuT4Whz7O.mHjTqaZu',
        0, 1);
INSERT INTO users(username, email, password, restrictions_maximum_amount, family_id)
VALUES ('user2', 'wer@ja.tu',
        '$2a$04$pjri1KTInYfhBZh2iax7E.riLi/wgULK0RGAuT4Whz7O.mHjTqaZu',
        0, 1);

INSERT INTO users(username, email, password, restrictions_maximum_amount, family_id)
VALUES ('user3', 'weewdr@ja.tu',
        '$2a$04$pjri1KTInYfhBZh2iax7E.riLi/wgULK0RGAuT4Whz7O.mHjTqaZu',
        0, 2);

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id)
VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 1);
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 2);
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 3);
