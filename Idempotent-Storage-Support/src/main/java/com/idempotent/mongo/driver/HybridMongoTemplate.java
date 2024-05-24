package com.idempotent.mongo.driver;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by bourne on 2017/8/8.
 */
public class HybridMongoTemplate extends MongoTemplate {
    /**
     * Constructor used for a basic template configuration
     *
     * @param mongoClient  must not be {@literal null}.
     * @param databaseName must not be {@literal null} or empty.
     */
    public HybridMongoTemplate(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    /**
     * Constructor used for a basic template configuration.
     *
     * @param mongoDbFactory must not be {@literal null}.
     */
    public HybridMongoTemplate(MongoDbFactory mongoDbFactory) {
        this(mongoDbFactory, null);
    }

    /**
     * Constructor used for a basic template configuration.
     *
     * @param mongoDbFactory must not be {@literal null}.
     * @param mongoConverter
     */
    public HybridMongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
    }

    public HybridMongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        super(mongoDatabaseFactory);
    }

    public HybridMongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,MongoConverter mongoConverter) {
        super(mongoDatabaseFactory,mongoConverter);
    }

    /**
     * @param collectionName
     * @param document
     */
    public void insertOne(String collectionName, Document document) {
        MongoDatabase database = getDb();

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertOne(document);
    }

    /**
     * @param collectionName
     * @param json
     */
    public void insertOneJson(String collectionName, String json) {
        Document document = Document.parse(json);

        insertOne(collectionName, document);
    }

    /**
     * @param collectionName
     * @param documents
     */
    public void insertMany(String collectionName, List<Document> documents) {
        MongoDatabase database = getDb();

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertMany(documents);
    }

    /**
     * @param collectionName
     * @param jsonList
     */
    public void insertManyJson(String collectionName, List<String> jsonList) {
        List<Document> documents = jsonList.stream()
                .map((json) -> Document.parse(json))
                .collect(Collectors.toList());
        insertMany(collectionName, documents);
    }


    /**
     * @param collectionName
     * @param filter
     * @return
     */
    public DeleteResult delete(String collectionName, Bson filter) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.deleteMany(filter);
    }

    /**
     * @param collectionName
     * @param filter
     * @param deleteOptions
     * @return
     */
    public DeleteResult delete(String collectionName, Bson filter, DeleteOptions deleteOptions) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.deleteMany(filter, deleteOptions);
    }

    /**
     * @param collectionName
     * @param filter
     * @param update
     * @return
     */
    public UpdateResult update(String collectionName, Bson filter, Bson update) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.updateMany(filter, update);
    }

    /**
     * @param collectionName
     * @param filter
     * @param update
     * @param updateOptions
     * @return
     */
    public UpdateResult update(String collectionName, Bson filter, Bson update, UpdateOptions updateOptions) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.updateMany(filter, update, updateOptions);
    }

    /**
     * 按_id查找
     *
     * @param collectionName
     * @param id
     * @return
     */
    public Document findById(String collectionName, String id) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Bson filter = eq("_id", id);

        Document doc = collection.find(filter).first();
        return doc;
    }

    /**
     * 按_id查找
     *
     * @param collectionName
     * @param id
     * @return
     */
    public Document findById(String collectionName, Long id) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Bson filter = eq("_id", id);

        Document doc = collection.find(filter).first();
        return doc;
    }

    /**
     * @param collectionName
     * @return
     */
    public long count(String collectionName) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.countDocuments();
    }

    /**
     * @param collectionName
     * @param filter
     * @return
     */
    public long count(String collectionName, Bson filter) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.countDocuments(filter);
    }

    /**
     * @param collectionName
     * @return
     */
    public FindIterable<Document> queryAll(String collectionName) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        FindIterable<Document> iterable = collection.find();
        return iterable;
    }

    /**
     * @param collectionName
     * @param filter
     * @return
     */
    public FindIterable<Document> query(String collectionName, Bson filter) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        FindIterable<Document> iterable = collection.find(filter);
        return iterable;
    }

    /**
     * @param collectionName
     * @param filter
     * @return
     */
    public FindIterable<Document> query(String collectionName, Bson filter, Bson sort) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        FindIterable<Document> iterable = collection.find(filter).sort(sort);
        return iterable;
    }

    /**
     * @param collectionName
     * @param filter
     * @return
     */
    public FindIterable<Document> query(String collectionName, Bson filter, Bson sort, Bson projection) {
        MongoDatabase database = getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        FindIterable<Document> iterable = collection.find(filter).sort(sort).projection(projection);
        return iterable;
    }

    public List<String> ConvertToJsonList(FindIterable<Document> iterable) {
        List<String> list = new ArrayList<>();
        list = iterable.map((doc) -> doc.toJson()).into(list);
        return list;
    }
}
