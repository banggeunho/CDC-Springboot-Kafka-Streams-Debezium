
# CDC  - Kafka, Debezium, Spring Boot

[https://github.com/banggeunho/CDC-Springboot-Kafka-Debezium](https://github.com/banggeunho/CDC-Springboot-Kafka-Debezium)

### 작업환경

- MacBook Air(M1, 2020)
- OS : macOS Monterey v12.6
- Memory : 16GB

### 버 전

- Docker :  version 20.10.12, build e91ed57
- JDK : 11 AdoptOpenJDK (HotSpot)
- JDBC : spring-jdbc 5.3.9
- OJDBC : ojdbc8 v12.2.0.1
- Oracle : 12c (카카오 인스턴스 도커환경으로 구성)
- Spring boot : 2.5.4
- Spirng boot data JPA : 2.5.4
- Spring-kafka

### 도커 컨테이너 (Compose)

Mysql, Kafka, Zookeper, Debezium

```docker
version: "3.8"

services:
  mysql:
    container_name: mysql
    image: debezium/example-mysql
    restart: always
    ports:
      - 3307:3306
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: db
      
  kafka:
    container_name: kafka
    image: confluentinc/cp-kafka:latest
    restart: always
    depends_on:
      - zookeeper
    ports:
      - 9092:9092
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: **PLAINTEXT://kafka:29092**,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_BROKER_ID: 1
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      
  zookeeper:
    container_name: zookeeper
    image: confluentinc/cp-zookeeper:latest
    ports:
      - 2181:2181
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      
  debezium:
    container_name: debezium
    image: debezium/connect:latest
    ports:
      - 8083:8083
    depends_on:
      - zookeeper
      - kafka
      - mysql
    environment:
      GROUP_ID: 1
      BOOTSTRAP_SERVERS: **kafka:29092**
      CONFIG_STORAGE_TOPIC: my_connect_configs
      OFFSET_STORAGE_TOPIC: my_connect_offsets
      STATUS_STORAGE_TOPIC: my_connect_statuses
```

 * debezium에서 제공하는 example-mysql 및 connect 사용.

 * 추가 설정 및 플러그인 설치 필요 없음.

### 구조도

- 소스 DB : MySQL
- 타겟 DB : Oracle, MySQL
- Source Connector : debezium
- Sink Connector : Spring boot (jdbc, ojdbc)

![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%201.png)

### Source Connector 생성

```json
{
    "name": "mysql-connector",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "tasks.max": "1",
        "database.hostname": "mysql",
        "database.port": "3306",
        "database.user": "root",
        "database.password": "root",
        "database.server.id": "184054",
        "database.server.name": "dbserver1",
        "database.include.list": "src_db",
        "database.history.kafka.bootstrap.servers": "kafka:29092",
        "database.history.kafka.topic": "schema-changes.db",
        "table.whitelist": "src_db.user",
        "include.schema.changes": "true"
    }
}
```

### Topic 확인

![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%202.png)

### DB 구성

- Mysql : src_db, target_db 2개로 나누고 각각의 테이블 구성
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%203.png)
    

- Oracle : TB_TARGET table 구성
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%204.png)
    

- 모든 테이블은 아래와 같이 구성 (아이디와 이름)
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%205.png)
    

- 테이블 작성 SQL문
    
    ```sql
    ### MYSQL ###
    CREATE TABLE user (
    id int primary key not null auto_increment,
    name varchar(255));
    
    ### ORACLE ###
    CREATE TABLE tb_target (
    id number(4) not null primary key,
    name VARCHAR2(255));
    
    ### ORACLE은 auto_increment 지원 X, sequence 생성하여 이용.
    ```
    

### Spring 구성

- Kafka Consumer 설정 (KafkaConsumerConfig.java)
    
    ```sql
       @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> configurations = new HashMap<>();
    				## kafka bootstrap 서버주소
            configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    				## Group ID Debezium -> dbz
            configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "dbz");
    				## 직렬화, 역직렬화 설정
            configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    				## offset 설정 (kafka에 offset이 없을 경우 자동으로 earliest로 재설정)
            configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    				## poll 함수 단일 호출에서 반환된 최대 레코드 수
            configurations.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
            return configurations;
        }
    }
    ```
    

- KafkaListener 함수 (Consumer.java)
    
    ```sql
    @KafkaListener(topics = "dbserver1.src_db.user")
        public void consumeUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
            String consumedValue = record.value();
    
            var jsonNode = mapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            JsonNode after = payload.path("after");
            System.out.println(payload);
            System.out.println(after.get("name").toString());
    
            User mysqlUser = User.builder()
                    .name(after.get("name").toString())
                    .build();
    
            OracleUser oracleUser = OracleUser.builder()
                    .name(after.get("name").toString())
                    .build();
    
            secondUserRepository.save(mysqlUser);
            oracleUserRepository.save(oracleUser);
        }
    ```
    

### 결 과

- 소스DB에 데이터 입력/삽입
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%206.png)
    

- kafka-console-consumer 확인
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%207.png)
    

- spring-boot console 확인
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%208.png)
    

- Mysql target db 확인
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%209.png)
    

- Oracle target db 확인
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%2010.png)
    

# Problem

### 카카오엔터프라이즈 CDC 파일럿 - 오라클 사용

- Oracle DB를 소스DB로 설정 후 Mapping api 호출 시 Invalid Oracle URL 에러 발생
    
    ![스크린샷 2022-10-22 21.04.27.png](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2022-10-22_21.04.27.png)
    
    - Metadata와 step 생성 시 oracle에 맞게 id 지정 후 진행
        
        ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%2011.png)
        
    
    - 11G, 12C 버전으로 시도 → 같은 에러 발생
    

### Spring Boot Project 진행 중 - 오라클 커넥트 생성

- 오라클 커넥트를 아래와 같이 생성
    
    ```json
    {
        "name": "oracle-connector",
        "config": {
            "connector.class" : "io.debezium.connector.oracle.OracleConnector",
            "database.hostname" : "210.109.60.233",
            "database.port" : "1521",
            "database.user" : "logminer",
            "database.password" : "logminer",
            "database.dbname" : "XE",
            "database.server.name" : "dbserver2",
            "tasks.max" : "1",
            "table.include.list" : "LOGMINER.TB_SRC",
            "database.history.kafka.bootstrap.servers" : "kafka:29092",
            "database.history.kafka.topic": "schema-changes.inventory"
        }
    }
    ```
    
    - license 문제로 debezium에서 ojdbc를 제공하지 않음 → 직접 다운받아 libs에 넣어주어야함
- topic 생성 확인
    
    ![Untitled](%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A9%E1%84%89%E1%85%A1%20%E1%84%83%E1%85%A2%E1%84%8E%E1%85%A6%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%20feed4c5b3a3246958e7aa90906df8d5d/Untitled%2012.png)
    

- 초기에는 Consumer가 메세지를 잘 받는 것을 확인, 점점 메세지 받아오는게 느려지더니, debezium connect에서 unregistered 되는 현상 발견…
- 원인 파악 중입니다..

# Reference

- 카카오엔터프라이즈 CDC pilot - Readme
- 5주차 강의자료 - Streaming data with Debezium, DBMS and Kafka
- [https://debezium.io/documentation/reference/stable/connectors/oracle.html](https://debezium.io/documentation/reference/stable/connectors/oracle.html)
- [https://debezium.io/documentation/reference/stable/connectors/mysql.html](https://debezium.io/documentation/reference/stable/connectors/mysql.html)
- [https://semtax.tistory.com/83](https://semtax.tistory.com/83)
- [https://www.baeldung.com/spring-kafka#consuming-messages](https://www.baeldung.com/spring-kafka#consuming-messages)

## 감사합니다. 🥰

201635816 가천대 소프트웨어학과 방근호

PHONE : 010-3767-8836

G-MAIL : panggeunho@gmail.com

Github : [https://github.com/banggeunho](https://github.com/banggeunho)
