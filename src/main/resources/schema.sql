Drop table if exists Comment,Person,Project,Person_project_relation,Translation,Document,Message,Part,History,Record;
Create table Person (id serial primary key ,login varchar(30) not null, password varchar(500) not null,name varchar(30) not null);
Create table Project (id serial primary key, person_id integer REFERENCES Person(id),name varchar(100));
Create table Person_project_relation (id serial primary key, person_id integer references Person(id), project_id integer references Project(id));
Create table Translation (id serial primary key,project_id integer references Project(id), origin varchar(100) not null,translated varchar(100) not null);
Create table Document (id serial primary key, project_id integer references Project(id),name varchar(100));
Create table Message (id serial primary key, person_id integer references Person(id),document_id integer references Document(id), date date not null, text varchar(250) not null);
Create table Part (id serial primary key, document_id integer references Document(id),origin text not null,translated text not null);
Create table History(id serial primary key,document_id integer references Document(id));
Create table Record(id serial primary key,history_id integer references History(id),part_id integer references Part(id),person_id integer references Person(id),date date not null,text Text not null);
create table Comment(id serial primary key,person_id integer references Person(id),part_id integer references Part(id),text varchar(250) not null, date Date);

