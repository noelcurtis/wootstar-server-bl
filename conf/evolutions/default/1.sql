# --- !Ups

create table attribute (
  id                        bigint not null,
  item_id                   varchar(255) not null,
  key                       varchar(255),
  value                     text,
  constraint pk_attribute primary key (id))
;

create table event (
  dtype                     varchar(10) not null,
  id                        varchar(255) not null,
  site                      varchar(255),
  start_date                timestamp,
  end_date                  timestamp,
  title                     text,
  manufacturer_text         text,
  url                       varchar(255),
  text                      text,
  write_up                  text,
  subtitle                  text,
  constraint pk_event primary key (id))
;

create table item (
  id                        varchar(255) not null,
  offer_id                  varchar(255) not null,
  list_price                float,
  purchase_limit            integer,
  sale_price                float,
  constraint pk_item primary key (id))
;

create table offer (
  id                        varchar(255) not null,
  features                  text,
  original_start_date       varchar(255),
  percentage_remaining      integer,
  rank                      integer,
  snippet                   text,
  sold_out                  boolean,
  specs                     text,
  subtitle                  text,
  teaser                    text,
  title                     text,
  url                       varchar(255),
  write_up                  text,
  event_id                  varchar(255),
  constraint pk_offer primary key (id))
;

create table photo (
  id                        bigint not null,
  height                    varchar(255),
  url                       varchar(255),
  width                     varchar(255),
  constraint pk_photo primary key (id))
;

create table quality_post (
  id                        bigint not null,
  offer_id                  varchar(255) not null,
  avatar                    varchar(255),
  posted_on                 timestamp,
  text                      text,
  username                  varchar(255),
  constraint pk_quality_post primary key (id))
;

create table shipping_method (
  id                        bigint not null,
  offer_id                  varchar(255) not null,
  name                      varchar(255),
  per_item_amount           varchar(255),
  per_order_amount          varchar(255),
  constraint pk_shipping_method primary key (id))
;

create table tag (
  id                bigint not null,
  info              text,
  constraint pk_tag primary key(id)
);

create table photo_tags (
  photo_id          bigint not null,
  tag_id            bigint not null,
  constraint pk_photo_tags primary key(photo_id, tag_id)
);

create table wp_photo (
  woot_plus_id              varchar(255) not null,
  id                        bigint not null,
  height                    varchar(255),
  url                       varchar(255),
  width                     varchar(255),
  constraint pk_wp_photo primary key (id),
  constraint fk_wp_photo foreign key (woot_plus_id) references event (id) on delete cascade);

create table offer_photos (
  offer_id                       varchar(255) not null,
  photo_id                       bigint not null,
  constraint pk_offer_photos primary key (offer_id, photo_id))
;

create table application_data (
  datakey       varchar(255) not null,
  datavalue     text,
  constraint pk_application_data primary key(datakey)
);

create sequence offer_seq;

create sequence item_seq;

create sequence attribute_seq;

create sequence photo_seq;

create sequence quality_post_seq;

create sequence shipping_method_seq;

create sequence tag_seq;

create sequence wp_photo_seq;

alter table attribute add constraint fk_attribute_item_1 foreign key (item_id) references item (id) on delete cascade;
create index ix_attribute_item_1 on attribute (item_id);

alter table item add constraint fk_item_offer_2 foreign key (offer_id) references offer (id) on delete cascade;
create index ix_item_offer_2 on item (offer_id);

alter table offer add constraint fk_offer_event_3 foreign key (event_id) references event (id) on delete cascade;
create index ix_offer_event_3 on offer (event_id);

alter table quality_post add constraint fk_quality_post_offer_4 foreign key (offer_id) references offer (id) on delete cascade;
create index ix_quality_post_offer_4 on quality_post (offer_id);

alter table shipping_method add constraint fk_shipping_method_offer_5 foreign key (offer_id) references offer (id) on delete cascade;
create index ix_shipping_method_offer_5 on shipping_method (offer_id);

alter table offer_photos add constraint fk_offer_photos_offer_01 foreign key (offer_id) references offer (id) on delete cascade;

alter table offer_photos add constraint fk_offer_photos_photo_02 foreign key (photo_id) references photo (id) on delete cascade;

alter table photo_tags add constraint fk_photo_tags_01 foreign key (photo_id) references photo (id) on delete cascade;

alter table photo_tags add constraint fk_photo_tags_02 foreign key (tag_id) references tag (id) on delete cascade;

# --- !Downs

drop table if exists application_data;

drop table if exists photo_tags cascade;

drop table if exists tag cascade;

drop table if exists attribute cascade;

drop table if exists event cascade;

drop table if exists item cascade;

drop table if exists offer cascade;

drop table if exists offer_photos cascade;

drop table if exists photo cascade;

drop table if exists wp_photo cascade;

drop table if exists quality_post cascade;

drop table if exists shipping_method cascade;

drop sequence if exists offer_seq;

drop sequence if exists item_seq;

drop sequence if exists attribute_seq;

drop sequence if exists photo_seq;

drop sequence if exists quality_post_seq;

drop sequence if exists shipping_method_seq;

drop sequence if exists tag_seq;

drop sequence if exists wp_photo_seq;

