package com.idempotent.mongo.driver;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.eq;

public class HybridMongoDriver {

    private MongoClient mongoClient;

    private MongoTemplate mongoTemplate;

    private MongoDatabase database ;

    private String dbName;

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public HybridMongoDriver(){
        database = mongoClient.getDatabase(dbName);
    }

    public void insertOne(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertOne(document);
    }

    public void insertOneJson(String collectionName, String json) {
        Document document = Document.parse(json);

        insertOne(collectionName,document);
    }

    public void insertMany(String collectionName, List<Document> documents) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertMany(documents);
    }

    public void insertManyJson(String collectionName, List<String> jsonList) {
        List<Document> documents;

        Stream<String> sl = jsonList.stream();
        Stream<Document> docStream = sl.map(this::parseDocument);

        documents = docStream.collect(Collectors.toList());

        insertMany(collectionName,documents);
    }

    public Document findById(String collectionName,String id){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Bson filter = eq("_id",id);

        Document doc = collection.find(filter).first();
        return doc;
    }


    public Document parseDocument(String json) {
        List<Character> result = new ArrayList<>();
        Document document = Document.parse(json);
        return document;
    }
}
