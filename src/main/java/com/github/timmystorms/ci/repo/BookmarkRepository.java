package com.github.timmystorms.ci.repo;

import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.github.timmystorms.ci.entity.Bookmark;

@Repository
public interface BookmarkRepository extends CypherDslRepository<Bookmark>, GraphRepository<Bookmark> {

}
