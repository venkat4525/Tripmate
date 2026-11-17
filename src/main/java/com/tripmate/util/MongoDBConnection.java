package com.tripmate.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe Singleton MongoDB Connection Manager.
 * Connects to MongoDB Atlas or local MongoDB using the MongoDB Java Sync Driver.
 */
public class MongoDBConnection {

    private static final Logger LOGGER = Logger.getLogger(MongoDBConnection.class.getName());

    // Default configuration with fallback to environment variables
    private static final String DEFAULT_URI = "mongodb://localhost:27017";
    private static final String DEFAULT_DATABASE = "tripmate_db";

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    private MongoDBConnection() {
        // Private constructor to enforce Singleton pattern
    }

    /**
     * Retrieves the active MongoDatabase instance.
     * Initializes the client on first invocation.
     */
    public static synchronized MongoDatabase getDatabase() {
        if (database == null) {
            initConnection();
        }
        return database;
    }

    /**
     * Convenience method to retrieve a specific MongoCollection.
     *
     * @param collectionName Name of the MongoDB collection.
     * @return MongoCollection<Document>
     */
    public static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    /**
     * Initializes the MongoClient with connection pooling settings.
     */
    private static void initConnection() {
        try {
            // Read environment variables if provided, otherwise use defaults
            String envUri = System.getenv("MONGODB_URI");
            String envDb = System.getenv("MONGODB_DB");

            String connectionUri = (envUri != null && !envUri.trim().isEmpty()) ? envUri.trim() : DEFAULT_URI;
            String databaseName = (envDb != null && !envDb.trim().isEmpty()) ? envDb.trim() : DEFAULT_DATABASE;

            LOGGER.info("Connecting to MongoDB at: " + (connectionUri.contains("@") 
                    ? connectionUri.replaceAll("://.*@", "://***:***@") // Hide credentials in logs
                    : connectionUri));

            ConnectionString connString = new ConnectionString(connectionUri);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .applyToConnectionPoolSettings(builder -> builder
                            .maxSize(50)                    // Max 50 connections in the pool
                            .minSize(5)                     // Keep at least 5 warm connections
                            .maxWaitTime(5000, TimeUnit.MILLISECONDS)
                            .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                    )
                    .applyToSocketSettings(builder -> builder
                            .connectTimeout(5000, TimeUnit.MILLISECONDS)
                            .readTimeout(10000, TimeUnit.MILLISECONDS)
                    )
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(databaseName);

            LOGGER.info("Connected successfully to MongoDB Database: " + databaseName);

            // Register JVM shutdown hook to close client cleanly on server stop
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                closeConnection();
            }));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize MongoDB connection", e);
            throw new RuntimeException("Could not connect to MongoDB database: " + e.getMessage(), e);
        }
    }

    /**
     * Closes the MongoClient connection pool cleanly.
     */
    public static synchronized void closeConnection() {
        if (mongoClient != null) {
            try {
                LOGGER.info("Closing MongoDB connection pool...");
                mongoClient.close();
                mongoClient = null;
                database = null;
                LOGGER.info("MongoDB connection closed cleanly.");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing MongoDB connection", e);
            }
        }
    }
}
