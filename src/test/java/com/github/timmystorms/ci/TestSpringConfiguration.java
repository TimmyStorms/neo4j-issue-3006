package com.github.timmystorms.ci;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSpringConfiguration {
    
    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

}
