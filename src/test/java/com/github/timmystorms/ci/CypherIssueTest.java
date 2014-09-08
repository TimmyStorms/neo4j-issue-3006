package com.github.timmystorms.ci;

import static org.neo4j.cypherdsl.CypherQuery.and;
import static org.neo4j.cypherdsl.CypherQuery.as;
import static org.neo4j.cypherdsl.CypherQuery.coalesce;
import static org.neo4j.cypherdsl.CypherQuery.collection;
import static org.neo4j.cypherdsl.CypherQuery.filter;
import static org.neo4j.cypherdsl.CypherQuery.id;
import static org.neo4j.cypherdsl.CypherQuery.identifier;
import static org.neo4j.cypherdsl.CypherQuery.labels;
import static org.neo4j.cypherdsl.CypherQuery.literal;
import static org.neo4j.cypherdsl.CypherQuery.match;
import static org.neo4j.cypherdsl.CypherQuery.node;
import static org.neo4j.cypherdsl.CypherQuery.order;
import static org.neo4j.cypherdsl.CypherQuery.param;
import static org.neo4j.cypherdsl.Order.DESCENDING;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypherdsl.Path;
import org.neo4j.cypherdsl.expression.BooleanExpression;
import org.neo4j.cypherdsl.grammar.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.timmystorms.ci.config.SpringConfiguration;
import com.github.timmystorms.ci.entity.Bookmark;
import com.github.timmystorms.ci.entity.Collaboration;
import com.github.timmystorms.ci.entity.InternalPerson;
import com.github.timmystorms.ci.repo.BookmarkRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, TestSpringConfiguration.class })
public class CypherIssueTest {
    
    private static final String IP_LABEL = "InternalPerson";

    private static final String USR_PARAM = "userId";

    private static final String USR_IDENTIFIER = "u";

    private static final String ENTITY2_IDENTIFIER = "e";

    private static final String LBL_IDENTIFIER = "x";

    private static final String REL_IDENTIFIER = "rel";

    private static final String ENTIT_IDENTIFIER = "ve";

    private static final String VISIBILITY_PROPERTY = "visibility";

    private static final String DATE_PROPERTY = "date";

    private static final BooleanExpression whereClause = and(
            coalesce(identifier(ENTITY2_IDENTIFIER).property(VISIBILITY_PROPERTY), literal("VISIBLE")).ne("DELETED"),
            id(identifier(USR_IDENTIFIER)).eq(param(USR_PARAM)));
    private static final Path matchClause = node(USR_IDENTIFIER).label(IP_LABEL).out("BOOKMARKED").as(REL_IDENTIFIER)
            .node(ENTIT_IDENTIFIER);
    private static final Execute QUERY = match(matchClause)
            .with(identifier(USR_IDENTIFIER),
                    identifier(REL_IDENTIFIER),
                    as(filter(LBL_IDENTIFIER, labels(identifier(ENTIT_IDENTIFIER)),
                            identifier(LBL_IDENTIFIER).in(collection(new String[] { "Collaboration" }))),
                            identifier(ENTITY2_IDENTIFIER))).where(whereClause).returns(identifier(REL_IDENTIFIER))
            .orderBy(order(identifier(REL_IDENTIFIER).property(DATE_PROPERTY), DESCENDING));


    @Autowired
    private BookmarkRepository bookmarkRepo;
    
    @Autowired
    private Neo4jTemplate template;
    
    @Transactional
    @Before
    public void init() throws Exception {
        final InternalPerson person = template.save(new InternalPerson());
        final Collaboration collab = template.save(new Collaboration());
        person.bookmarks(collab);
    }
    
    @Transactional
    @Test
    public void testInvalidCypherStatement() throws Exception {
        final InternalPerson person = template.findAll(InternalPerson.class).singleOrNull();
        Assert.assertNotNull(person);
        final Collaboration collab = template.findAll(Collaboration.class).singleOrNull();
        Assert.assertNotNull(collab);
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", person.getId());
        final Bookmark bookmark = bookmarkRepo.query(QUERY, params).singleOrNull();
        Assert.assertNotNull(bookmark);
    }
    
}
