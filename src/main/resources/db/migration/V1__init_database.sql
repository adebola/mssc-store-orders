drop table if exists `order_item`;
drop table if exists `order`;

create table `order` (
                         id varchar(36) not null,
                         address varchar(512),
                         order_date datetime(6),
                         order_status smallint not null,
                         telephone varchar(64),
                         total_price decimal(13,2) not null,
                         update_date datetime(6),
                         user_id varchar(36),
                         version integer,
                         primary key (id)
) engine=InnoDB;

create table order_item (
                            id varchar(36) not null,
                            description varchar(255) not null,
                            discount decimal(13,2),
                            product_id varchar(36) not null,
                            quantity integer not null,
                            total_price decimal(13,2),
                            unit_price decimal(13,2),
                            `order_id` varchar(36),
                            primary key (id)
) engine=InnoDB;

alter table order_item
    add constraint FKsgmrolp8skulmo2vyb20c4mfk
        foreign key (`order_id`)
            references `order` (id);