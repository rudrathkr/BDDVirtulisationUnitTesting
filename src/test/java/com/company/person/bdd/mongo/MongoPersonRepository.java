package com.company.person.bdd.mongo;

import com.company.person.model.Person;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class MongoPersonRepository {

    private final DBCollection collection;

    public MongoPersonRepository(DB db) {
        this.collection = db.getCollection("persons");
    }

    public void deleteAll() {
        collection.remove(new BasicDBObject());
    }

    public void insert(Person p) {
        BasicDBObject doc = new BasicDBObject();
        doc.append("firstName", p.firstName());
        doc.append("lastName", p.lastName());
        doc.append("profession", p.profession());
        doc.append("locationX", p.locationX());
        doc.append("locationY", p.locationY());
        doc.append("companyOrg", p.companyOrg());
        doc.append("companyHeadQuarters", p.companyHeadquarters());

        collection.insert(doc);
    }

    public int count() {
        return collection.find().count();
    }

    public String findCompanyOrgByName(String firstName, String lastName) {
        BasicDBObject query = new BasicDBObject();
        query.append("firstName", firstName);
        query.append("lastName", lastName);

        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            Object val = cursor.next().get("companyOrg");
            return val == null ? null : val.toString();
        }
        return null;
    }
}

