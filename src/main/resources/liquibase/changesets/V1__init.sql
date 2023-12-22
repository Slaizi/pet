create table if not exists users
    (
      id bigserial primary key not null,
      username varchar(255) not null unique,
      password varchar(255) not null
    );

create table if not exists locations
    (
        id bigserial primary key not null,
        name varchar(255) not null,
        latitude numeric not null,
        longitude numeric not null
    );

create table if not exists users_locations
    (
        user_id bigint not null,
        location_id bigint not null,
        primary key (user_id, location_id),
        constraint fk_users_location_users foreign key (user_id) references users on delete cascade on update no action,
        constraint fk_users_location_location foreign key (location_id) references locations on delete cascade on update no action
    );

create table if not exists users_roles
    (
        user_id bigint not null,
        role varchar(255) not null,
        primary key (user_id, role),
        constraint fk_users_roles_users foreign key (user_id) references users on delete cascade on update no action
    );