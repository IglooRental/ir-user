--DROP TABLE IF EXISTS "users";
--CREATE TABLE IF NOT EXISTS "users" ("id" VARCHAR(128) PRIMARY KEY, "name" VARCHAR(256) NOT NULL, "email" VARCHAR(256) NOT NULL, "password_hash" VARCHAR(128) NOT NULL);
INSERT INTO "users" ("id", "name", "email", "password_hash") VALUES ('0', 'User Name 1', 'user1@example.com', 'password hash very secure');
INSERT INTO "users" ("id", "name", "email", "password_hash") VALUES ('1', 'User Name 2', 'user2@example.com', 'password hash very secure too');
INSERT INTO "users" ("id", "name", "email", "password_hash") VALUES ('2', 'User Name 3', 'user3@example.com', 'passwort veri stronk yes yses');
INSERT INTO "users" ("id", "name", "email", "password_hash") VALUES ('3', 'User Name 4', 'user4@example.com', 'new pass very very string!');
