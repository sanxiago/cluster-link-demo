package com.sanxiago;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterMirrorOp;
import org.apache.kafka.clients.admin.AlterMirrorsOptions;
import org.apache.kafka.clients.admin.ClusterLinkListing;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.ConfluentAdmin;
import org.apache.kafka.clients.admin.CreateClusterLinksOptions;
import org.apache.kafka.clients.admin.DescribeMirrorsOptions;
import org.apache.kafka.clients.admin.ListClusterLinksOptions;
import org.apache.kafka.clients.admin.ListMirrorsOptions;
import org.apache.kafka.clients.admin.MirrorTopicDescription;
import org.apache.kafka.clients.admin.NewClusterLink;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.ClusterAuthorizationException;
import org.apache.kafka.common.errors.ClusterLinkDisabledException;
import org.apache.kafka.common.errors.ClusterLinkExistsException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.io.IOException;



public class kli {
    private static final Logger logger = LoggerFactory.getLogger(kli.class);


    public static void main(String[] args) {
        // Load the AdminClient properties to be used to create the link
        Properties properties = loadProperties("kafka-config.properties");
        // Load the Link configuration to be used by the link
        Properties linkConfigProps = loadProperties("link-config.properties");
        // The Link name
        String linkName = "TEST";

        // Create config map from properties
        Map<String, String> configs = new HashMap<>();
        for (Map.Entry<Object, Object> entry : linkConfigProps.entrySet()) {
          configs.put(entry.getKey().toString(), entry.getValue().toString());
        }

        NewClusterLink newClusterLink = new NewClusterLink(linkName, null, configs);



        try (ConfluentAdmin adminClient = ConfluentAdmin.create(properties)) {
            CreateClusterLinksOptions options = new CreateClusterLinksOptions();
            adminClient.createClusterLinks(Collections.singleton(newClusterLink), options).all().get();
            logger.info("Successfully created new cluster link {}", linkName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
