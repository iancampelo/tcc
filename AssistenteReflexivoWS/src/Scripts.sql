use assistentereflexivo;

create table usuario(
   id int not null AUTO_INCREMENT,
   usuario varchar(50) not null,
   senha varchar(15) not null,
   nome varchar(150) not null,
   nascimento date not null,
   funcao varchar(50),
   primary key (id));

   
create table projeto(
   id int not null auto_increment,
   uid int not null,
   nome varchar(100) not null,
   tempo_estimado float not null,
   tempo_gasto float,
   kma float,
   kmb float,
   primary key (id),
   foreign key (uid) references usuario(id) 
);