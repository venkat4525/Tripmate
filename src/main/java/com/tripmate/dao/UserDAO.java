package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tripmate.model.User;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'users' collection.
 */
public class UserDAO {

    private final MongoCollection<Document> collection;

    public UserDAO() {
        this.collection = MongoDBConnection.getCollection("users");
    }

    public boolean create(User user) {
        try {
            Document doc = new Document()
                    .append("fullName", user.getFullName())
                    .append("email", user.getEmail().toLowerCase().trim())
                    .append("password", user.getPassword())
                    .append("role", user.getRole() != null ? user.getRole().toUpperCase() : "USER")
                    .append("phone", user.getPhone())
                    .append("profileImage", user.getProfileImage())
                    .append("isActive", user.isActive())
                    .append("createdAt", new Date())
                    .append("updatedAt", new Date());

            collection.insertOne(doc);
            user.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            if (email == null) return null;
            Document doc = collection.find(Filters.eq("email", email.toLowerCase().trim())).first();
            return mapDocumentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                User u = mapDocumentToUser(doc);
                if (u != null) list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(User user) {
        try {
            if (!ObjectId.isValid(user.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("fullName", user.getFullName()),
                    Updates.set("phone", user.getPhone()),
                    Updates.set("role", user.getRole()),
                    Updates.set("profileImage", user.getProfileImage()),
                    Updates.set("isActive", user.isActive()),
                    Updates.set("updatedAt", new Date())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(user.getId())), updates).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String userId, String newHashedPassword) {
        try {
            if (!ObjectId.isValid(userId)) return false;
            Bson updates = Updates.combine(
                    Updates.set("password", newHashedPassword),
                    Updates.set("updatedAt", new Date())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(userId)), updates).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        try {
            if (!ObjectId.isValid(id)) return false;
            return collection.deleteOne(Filters.eq("_id", new ObjectId(id))).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long countUsers() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            return 0;
        }
    }

    private User mapDocumentToUser(Document doc) {
        if (doc == null) return null;
        User user = new User();
        user.setId(doc.getObjectId("_id").toHexString());
        user.setFullName(doc.getString("fullName"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setRole(doc.getString("role"));
        user.setPhone(doc.getString("phone"));
        user.setProfileImage(doc.getString("profileImage"));
        user.setActive(doc.getBoolean("isActive", true));
        user.setCreatedAt(doc.getDate("createdAt"));
        user.setUpdatedAt(doc.getDate("updatedAt"));
        return user;
    }
}
