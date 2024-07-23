package org.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

public class MongoUsersPojoDao {
    static MongoClient client;
    static MongoDatabase database;
    static {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        client = MongoClients.create(connectionString);
        database = client.getDatabase("test3").withCodecRegistry(pojoCodecRegistry);
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

    public static <T> MongoCollection<T> getCollection(String colName, Class<T> clazz) {
        MongoCollection<T> collection = database.getCollection(colName, clazz);
        return collection;
    }

    public static void main(String[] args) {
        MongoCollection<UserPojo> collection = getCollection("users", UserPojo.class);

        System.out.println("================ 결과 시작 =================");

//        // insertOne
//        UserPojo newUser = new UserPojo(null, "이효석", 40);
//        InsertOneResult result = collection.insertOne(newUser);
//        System.out.println("처리 결과 : " + result.wasAcknowledged());
//        System.out.println("삽입 된 도큐먼트의 _id : " + result.getInsertedId());

//        // insertMany
//        List<UserPojo> newTodos = Arrays.asList(
//                new UserPojo(null, "이효석2", 40),
//                new UserPojo(null, "이효석3", 40),
//                new UserPojo(null, "이효석4", 40)
//        );
//        InsertManyResult manyResult = collection.insertMany(newTodos);
//        System.out.println("처리 결과 : " + manyResult.wasAcknowledged());
//        System.out.println("삽입 된 도큐먼트의 _id : " + manyResult.getInsertedIds());

//        // find()
//        List<UserPojo> users = new ArrayList<>();
//        collection.find().into(users);
//        for(UserPojo user : users) {
//            System.out.println(user);
//        }

//        // findOne()
//        Bson query = eq("name", "이효석");
//        UserPojo findUser = collection.find(query).first();
//        System.out.println("==> findByIdResult : " + findUser);

//        // updateOne
//        Bson updateQuery = eq("name", "이효석");
//        Bson updateOperation = set("age", 41);
//        UpdateResult updateResult = collection.updateOne(updateQuery, updateOperation);
//        System.out.println("수정된 도큐먼트의 수 : " + updateResult.getModifiedCount());


        // deleteOne
        Bson deleteQuery = eq("name", "이효석");
        DeleteResult deleteResult = collection.deleteOne(deleteQuery);
        System.out.println("삭제된 도큐먼트의 수 : " + deleteResult.getDeletedCount());
    }
}

