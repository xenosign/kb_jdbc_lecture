package org.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.Iterator;

public class MongoUsersDao {
    static MongoClient client;
    static MongoDatabase database;
    static {
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        client = MongoClients.create(connectionString);
        database = client.getDatabase("test2");
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

    public static void printAllUsers() {
        MongoCollection<Document> users = getCollection("users");
        FindIterable<Document> doc = users.find();
        Iterator itr = doc.iterator();
        while (itr.hasNext()) {
            System.out.println("==> findResultRow : " + itr.next());
        }
    }

    public static void main(String[] args) {
        MongoCollection<Document> users = getCollection("users");
        System.out.println("======== 메인 메서드 시작 ========");


        // CREATE
        Document newUser = new Document();
        newUser.append("name", "이효석");
        newUser.append("age", 40);
        users.insertOne(newUser);
//
//        // READ
//        FindIterable<Document> doc = users.find();
//        Iterator itr = doc.iterator();
//        while (itr.hasNext()) {
//            System.out.println("==> findResultRow : " + itr.next());
//        }

//        // UPDATE
//        System.out.println("=== UPDATE 시작 ===");
//        // 업데이트 연습을 위한 더미 데이터 삽입
//        Document user = new Document();
//        user.append("name", "이효석2");
//        user.append("age", 20);
//        users.insertOne(user);
//
//        printAllUsers();
//
//        // MongoDB 의 쿼리는 결국 객체 형태로 전달 되므로 Bson 으로 만듭니다!
//        Bson updateFilter = eq("name", "이효석2");
//        Bson updateOperation = set("age", 40);
//        // 업데이트 실행
//        users.updateOne(updateFilter, updateOperation);
//
//        System.out.println("사기꾼 검거");
//        printAllUsers();


        // DELETE
        System.out.println("=== DELETE 시작 ===");
        Bson deleteFilter = eq("name", "이효석2");
        users.deleteOne(deleteFilter);
        System.out.println("사기꾼 Out");

        printAllUsers();
    }
}

