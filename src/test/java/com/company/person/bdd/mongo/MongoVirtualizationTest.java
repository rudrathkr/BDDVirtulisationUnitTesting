package com.company.person.bdd.mongo;

import com.company.person.model.Person;
import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MongoVirtualizationTest {

    private static DB db;
    private static MongoPersonRepository repo;

    @BeforeAll
    public static void setup() {
        FongoMongoConfig.start();
        db = FongoMongoConfig.database();
        repo = new MongoPersonRepository(db);
    }

    @AfterAll
    public static void tearDown() {
        if (repo != null) {
            repo.deleteAll();
        }
        FongoMongoConfig.stop();
    }

    @Test
    public void testInsertAndCount() {
        repo.deleteAll();

        Person p1 = new Person("Rudra", "Thakur", "IT", 100, 150, "Cognizant USA", "Akron");
        Person p2 = new Person("Raj", "Kumar", "Auto", 200, 250, "Cognizant London", "London");

        repo.insert(p1);
        repo.insert(p2);

        int count = repo.count();
        assertEquals(2, count, "There should be 2 persons in the Mongo collection");
    }

    @Test
    public void testFindCompanyOrgByName() {
        repo.deleteAll();

        Person p1 = new Person("Raj", "Kumar", "Auto", 200, 250, "Cognizant London", "London");
        repo.insert(p1);

        String org = repo.findCompanyOrgByName("Raj", "Kumar");
        assertEquals("Cognizant London", org);
    }
}

