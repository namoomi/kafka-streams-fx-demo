### Kafka Streams Demo Application

달러(USD)를 원화(KRW)로 환전하여 store에 저장한다.    
달러(USD)는 rails-usd-topic, 원화(KRW)는 rails-krw-topic 으로 다시 프로듀싱 한다.


**실행**
~~~
주키퍼 실행
bin/zookeeper-server-start.sh config/zookeeper.properties

카프카 브로커 실행
bin/kafka-server-start.sh config/server.properties
~~~

**토픽 생성**
~~~
#input topic
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic payment-topic --create

#output topic-1 
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic rails-usd-topic --create

#output topic-2
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic rails-krw-topic --create
~~~

---
- console 및 api 통해서 토픽 데이터를 확인한다.
- mock.txt 데이터를 순차적으로 추가하여 토픽, store에 조건별로 저장된 데이터를 확인한다.

**[input] payment-topic 데이터 추가**
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic payment-topic 
~~~

**[output] rails-krw-topic, rails-usd-topic 토픽 데이터 확인**
~~~
#USD 토픽
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
 --topic rails-usd-topic --from-beginning

#KRW 토픽
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
 --topic rails-krw-topic --from-beginning 
~~~

**[output] store 데이터 확인**
~~~ 
http://localhost:8080/v1/kafka-streams/balance/ABC-0001 
~~~

---
참고 
https://medium.com/lydtech-consulting/kafka-streams-spring-boot-demo-ff0e74e08c9c
