-- create databases if not exists SAE_RESEAUX;
-- use SAE_RESEAUX



create table UTILISATEUR(
    id_U int not null;
    pseudo varchar(100);
    email varchar(100);
    mdp varchar(100);
    primary key (id_U);
);

create table AMIS(
    suiveur int not null;
    suivi int not null;
    foreign key (suiveur) references UTILISATEUR(id_U);
    foreign key (suivi) references UTILISATEUR(id_U);
    primary key (suiveur, suivi);
);
create table MESSAGE(
    id_M int not null;
    id_U int not null;
    contenu varchar(100);
    date DATETIME;
    foreign key (id_U) references UTILISATEUR(id_U);
    primary key (id_M);
);
create table LIKE(
    id_M int not null;
    id_U int not null;
    foreign key (id_M) references MESSAGE(id_M);
    foreign key (id_U) references UTILISATEUR(id_U);
    primary key (id_M, id_U);
);
