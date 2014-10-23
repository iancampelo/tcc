use assistentereflexivo;

create table usuario(
   id         int          not null AUTO_INCREMENT,
   usuario    varchar(50)  not null unique key,
   senha      varchar(15)  not null,
   nome       varchar(150) not null,
   nascimento date         not null,
   funcao     varchar(50), 
   kmb        float,
   primary key (id));

create table atividade(
   id             int          not null auto_increment,
   uid            int          not null,
   nome           varchar(100) not null,
   tempo_estimado timestamp    not null,
   predicao       int          not null,
   estrategia     varchar(200),
   recursos       varchar(200),
   grau_atencao   varchar(10),
   comprensao     varchar(200),
   objetivo       varchar(200),
   anotacoes      varchar(200),
   kma            float,
   tempo_gasto    timestamp,
   resultado      int,
   primary key (id),
   foreign key (uid) references usuario(id) 
);