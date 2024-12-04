package org.example.logistics.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "logisticsLogs";

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_URI);
            database = mongoClient.getDatabase(DATABASE_NAME);
            // System.out.println("MongoDB 연결 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MongoDB 연결 실패: " + e.getMessage());
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB 연결 종료!");
        }
    }
}

