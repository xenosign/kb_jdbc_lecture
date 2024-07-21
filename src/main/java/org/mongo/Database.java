package org.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Iterator;

public class Database {
    static MongoClient client;
    static MongoDatabase database;
    static {
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        client = MongoClients.create(connectionString);
        database = client.getDatabase("my_database");
    }

    public static void close() {
        client.close();
    }
    public static MongoDatabase getDatabase() {
        return database;
    }
    public static MongoCollection<Document> getCollection(String colName) {
        MongoCollection<Document> collection = database.getCollection(colName);
        return collection;
    }

    public static void main(String[] args) {
        MongoCollection<Document> users = getCollection("users");
        Document user = new Document();
        user.append("name", "나건우");
        user.append("age", 27);
        users.insertOne(user);

        FindIterable<Document> doc = users.find();
        Iterator itr = doc.iterator();
        while (itr.hasNext()) {
            System.out.println("==> findResultRow : " + itr.next());
        }

    }
}

