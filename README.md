Some Examples using ConfluentAdmin and Cluster Link
Examples:
1. Create a link called TEST
2. Create a mirror of source topic test called test


# Clone it
```
git clone https://github.com/sanxiago/cluster-link-demo
```
# Build it
```
mvn clean install
```
# Create the config file to connect and create the link
```
cat <<EOF> kafka-config.properties
bootstrap.servers=localhost:9092
EOF
```
# Create the link config file used by the link
```
cat <<EOF> kafka-config.properties
ssl.endpoint.identification.algorithm=https
sasl.mechanism=PLAIN
request.timeout.ms=20000
bootstrap.servers=pkc-xxxxx.us-east-2.aws.confluent.cloud:9092
retry.backoff.ms=500
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="xxxxxxxxxxx" password="yyyyyyyyyyyy";
security.protocol=SASL_SSL
EOF
```

# Setup Local Kafka Cluster
```
# Download Confluent 
wget https://packages.confluent.io/archive/7.5/confluent-7.5.2.tar.gz

# Unpack
tar xvzf confluent-7.5.2.tar.gz

# Configure Password encoder with a random uuid 
echo password.encoder.secret=\"$(uuidgen)\" >> confluent-7.5.2/etc/kafka/server.properties

# start zookeeper
confluent-7.5.2/bin/zookeeper-server-start confluent-7.5.2/etc/kafka/zookeeper.properties > /tmp/zk.log &

# start kafka
confluent-7.5.2/bin/kafka-server-start confluent-7.5.2/etc/kafka/server.properties > /tmp/kafka.log &
```
# Run create link from jar
```
java  -cp target/kli-1.0-SNAPSHOT-jar-with-dependencies.jar com.sanxiago.kli
```

# Check if it created the link
```
confluent-7.5.2/bin/kafka-cluster-links --bootstrap-server localhost:9092 --command-config kafka-config.properties --describe
```
