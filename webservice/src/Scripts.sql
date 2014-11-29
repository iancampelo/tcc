use assistentereflexivo;

create table usuario(
   id         int          not null AUTO_INCREMENT,
   usuario    varchar(50)  not null unique key,
   senha      varchar(15)  not null,
   nome       varchar(150) not null,
   nascimento date         not null,
   funcao     varchar(50), 
   primary key (id));

CREATE TABLE atividade (
  id int(11) NOT NULL auto_increment,
  uid int(11) NOT NULL,
  nome varchar(100) NOT NULL,
  tempo_estimado time NOT NULL,
  predicao int(11) NOT NULL,
  estrategia varchar(200) default NULL,
  recursos varchar(200) default NULL,
  grau_atencao varchar(10) default NULL,
  comprensao varchar(200) default NULL,
  objetivo varchar(200) default NULL,
  anotacoes varchar(200) default NULL,
  kma double default NULL,
  tempo_gasto time default NULL,
  resultado int(11) default NULL,
  kmb double default NULL,
  PRIMARY KEY  (id),
  KEY uid (uid),
  CONSTRAINT atividade_ibfk_1 FOREIGN KEY (uid) REFERENCES usuario (id)
);