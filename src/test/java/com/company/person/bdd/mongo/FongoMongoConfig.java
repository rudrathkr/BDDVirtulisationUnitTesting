package com.company.person.bdd.mongo;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;

public final class FongoMongoConfig {

    private static Fongo fongo;
    private static DB database;

    private FongoMongoConfig() {
    }

    public static synchronized void start() {
        if (fongo != null) {
            return;
        }

        fongo = new Fongo("bdd-virtual-mongo");
        database = fongo.getDB("persondb_mongo");
    }

    public static synchronized DB database() {
        return database;
    }

    public static synchronized void stop() {
        if (fongo != null) {
            fongo.dropDatabase("persondb_mongo");
            fongo = null;
            database = null;
        }
    }
}
