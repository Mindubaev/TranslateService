insert into Person (login,password,name) values  ('admin','$2a$10$j753ShKXvta9WRGYo0yfK.4C8n.2.lGTBd3I9jzCQkOpEgNwhj37W','adminTester'),('user','$2a$10$MKt76PTApgdCbfV/CTC68utcbutHFybL12WkQZCIf0Gmi/JpPhklW','userTester');
insert into Project (person_id,name) values (1,'JPA artical translation'),(2,'Java article translation');
insert into Person_project_relation (person_id,project_id) values (2,1);
insert into Translation (project_id,origin,translated) values (1,'Fetching policy','политика выборки'),(1,'automatic dirty checking mechanism','механизм для проведения автоматической первичной проверки');
insert into Document (project_id,name) values (1,'Hibernate Jpa.docx'),(2,'Java 13 Features.doc'),(1,'Disadventages Of Open Session In View Pattern.txt');
insert into Part (document_id,origin, translated) values (1
,'When using JPA and Hibernate, the Fetching policy can have one of the biggest impacts on application performance, and, as explained in my High-Performance JDBC presentation, you should always fetch just as much data you need to fulfil the requirements of a given business logic use case.'
,'При использовании JPA и Hibernate политика выборки может оказать одно из самых значительных воздействий на производительность приложений, и, как объяснено в моей высокопроизводительной JDBC-презентации, вы всегда должны получать столько данных, сколько вам нужно для удовлетворения требований данного бизнеса. логический вариант использования.')
,(1
,'Entities are very useful for read-write transactions because you can benefit from the automatic dirty checking mechanism while preventing lost updates phenomena in multi-request logical transactions.'
,'Объекты очень полезны для транзакций чтения-записи, потому что вы можете воспользоваться механизмом автоматической грязной проверки, предотвращая явления потерянных обновлений в логических транзакциях с несколькими запросами.')
,(2
,'The biggest change in Java 13 is the replacement of the keyword break in the switch expression by yield. The background is the better differentiation between switch statement (with possible break) and expressions (with yield). The yield statement exits the switch and returns the result of the current branch, similar to a return.'
,'Самым большим изменением в Java 13 является замена ключевого слова break в выражении switch на yield. Фоном является лучшее различие между оператором switch (с возможным разрывом) и выражениями (с yield). Оператор yield выходит из переключателя и возвращает результат текущей ветви, аналогично возврату.');
insert into Message (person_id,document_id,date,text) values (1,2,'2020.01.26','I am waiting for new version java 99 this year'),(2,2,'2020.01.27','hold on, we will have the same post soon'),(1,1,'2020.01.25','we must translate it until monday');
insert into History (document_id) values (1),(2);
insert into Record (history_id, part_id,person_id,date,text) values(1,1,1,'2020.01.25','При использовании JPA и Hibernate политика выборки может оказать'),(1,1,1,'2020.01.26','При использовании JPA и Hibernate политика выборки может оказать одно из самых значительных воздействий на производительность');
insert into Comment (person_id,part_id,text,date) values (1,1,'Some text content for comment','2020.01.28'),(2,1,'Some text content for another comment testing','2020.01.30');
