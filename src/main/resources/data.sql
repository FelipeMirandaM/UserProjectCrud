-- Initial data --
insert into users(user_id, created, last_login,modified, is_active, name, email, password)
values ('f9389390-cf4e-4f79-b028-95c178bfff6a',CURRENT_DATE, CURRENT_DATE,CURRENT_DATE, true, 'Alice', 'alice1@gmail.com', '$2a$12$juJnJRBUiBAx3VhqZUAt..MADY23c55TJYzB.bcMZ7Si3Iswajj7i'),
        ('ffe0bbf1-2f17-4a94-8e82-d44886df7a5b',CURRENT_DATE, CURRENT_DATE,CURRENT_DATE, true, 'Lana', 'Lana1@gmail.com', '$2a$12$juJnJRBUiBAx3VhqZUAt..MADY23c55TJYzB.bcMZ7Si3Iswajj7i');

insert into phones(phone_id, number, city_code,country_code, user_id)
values ('ab6edb2d-ff54-4e6d-8b05-18e17a376948','12345679','3','+41', 'f9389390-cf4e-4f79-b028-95c178bfff6a'),
       ('e7e1fc46-292b-434d-924c-5074fe6c0f0f','12345678','9', '+56', 'ffe0bbf1-2f17-4a94-8e82-d44886df7a5b');