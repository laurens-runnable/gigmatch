create database gigmatch;
\connect gigmatch;

create user gigmatch with password 'gigmatch';
grant all on schema public TO gigmatch;

create database keycloak;
\connect keycloak;

create user keycloak with password 'keycloak';
grant all on schema public TO keycloak;
