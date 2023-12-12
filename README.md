# Clone it
git clone https://github.com/sanxiago/cluster-link-demo
# Build it
mvn clean install

# Create the configuration file for the admin client creating the link, i.e a local cluster
cat <<EOF> kafka-config.properties
bootstrap.servers=localhost:9092
EOF

# Create the configuration file for the remote cluster, i.e a cloud cluster, this also includes the link configs
cat <<EOF> kafka-config.properties
ssl.endpoint.identification.algorithm=https
sasl.mechanism=PLAIN
request.timeout.ms=20000
bootstrap.servers=pkc-xxxxx.us-east-2.aws.confluent.cloud:9092
retry.backoff.ms=500
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="xxxxxxxxxxx" password="yyyyyyyyyyyy";
security.protocol=SASL_SSL
EOF

# Download Confluent 
wget https://packages.confluent.io/archive/7.5/confluent-7.5.2.tar.gz

# Unpack
tar xvzf confluent-7.5.2.tar.gz

# Configure Password encoder, required to store the remote link auth
echo password.encoder.secret=\"$(uuidgen)\" >> confluent-7.5.2/etc/kafka/server.properties

# start zookeeper
confluent-7.5.2/bin/zookeeper-server-start confluent-7.5.2/etc/kafka/zookeeper.properties > /tmp/zk.log &

# start kafka
confluent-7.5.2/bin/kafka-server-start confluent-7.5.2/etc/kafka/server.properties > /tmp/kafka.log &

# run create link from jar
java  -cp target/kli-1.0-SNAPSHOT-jar-with-dependencies.jar com.sanxiago.kli
