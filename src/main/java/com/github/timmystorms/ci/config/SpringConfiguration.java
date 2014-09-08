package com.github.timmystorms.ci.config;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.neo4j.aspects.config.Neo4jAspectConfiguration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy
@ComponentScan("com.github.timmystorms.ci")
@EnableNeo4jRepositories(basePackages = { "com.github.timmystorms.ci.repo" })
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class SpringConfiguration extends Neo4jAspectConfiguration {
    
    SpringConfiguration() {
        setBasePackage("com.github.timmystorms.ci.entity");
    }
    
    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder("data/neo4j.db")
                .newGraphDatabase();
    }
    
    @Bean
    public ExecutionEngine executionEngine() {
        return new ExecutionEngine(graphDatabaseService());
    }

}
