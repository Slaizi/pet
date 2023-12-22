insert into users (username, password)
values ('johndoe@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
        ('mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m');

insert into locations (name, latitude, longitude)
values ('Moscow', '55.7504461', '37.6174943'),
       ('Paris', '48.8588897', '2.3200410217200766'),
       ('Yaroslavl', '57.6263877', '39.8933705');

insert into users_locations (user_id, location_id)
values (1, 1),
       (2, 2),
       (1, 3);

insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');