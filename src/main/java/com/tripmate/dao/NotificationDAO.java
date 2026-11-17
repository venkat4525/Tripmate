package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Notification;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'notifications' collection.
 */
public class NotificationDAO {

    private final MongoCollection<Document> collection;

    public NotificationDAO() {
        this.collection = MongoDBConnection.getCollection("notifications");
    }

    public boolean create(Notification notif) {
        try {
            if (!ObjectId.isValid(notif.getUserId())) return false;

            Document doc = new Document()
                    .append("userId", new ObjectId(notif.getUserId()))
                    .append("title", notif.getTitle())
                    .append("message", notif.getMessage())
                    .append("type", notif.getType() != null ? notif.getType() : "GENERAL")
                    .append("relatedEntityId", ObjectId.isValid(notif.getRelatedEntityId()) 
                            ? new ObjectId(notif.getRelatedEntityId()) : null)
                    .append("isRead", notif.isRead())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            notif.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notification> findByUserId(String userId) {
        List<Notification> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(userId)) return list;
            for (Document doc : collection.find(Filters.eq("userId", new ObjectId(userId)))
                    .sort(Sorts.descending("createdAt"))) {
                Notification notif = mapDocumentToNotification(doc);
                if (notif != null) list.add(notif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public long countUnread(String userId) {
        try {
            if (!ObjectId.isValid(userId)) return 0;
            return collection.countDocuments(Filters.and(
                    Filters.eq("userId", new ObjectId(userId)),
                    Filters.eq("isRead", false)
            ));
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean markAsRead(String id) {
        try {
            if (!ObjectId.isValid(id)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                    Updates.set("isRead", true)).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAllAsRead(String userId) {
        try {
            if (!ObjectId.isValid(userId)) return false;
            return collection.updateMany(Filters.and(
                    Filters.eq("userId", new ObjectId(userId)),
                    Filters.eq("isRead", false)
            ), Updates.set("isRead", true)).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Notification mapDocumentToNotification(Document doc) {
        if (doc == null) return null;
        Notification n = new Notification();
        n.setId(doc.getObjectId("_id").toHexString());
        ObjectId uid = doc.getObjectId("userId");
        if (uid != null) n.setUserId(uid.toHexString());
        n.setTitle(doc.getString("title"));
        n.setMessage(doc.getString("message"));
        n.setType(doc.getString("type"));
        ObjectId relId = doc.getObjectId("relatedEntityId");
        if (relId != null) n.setRelatedEntityId(relId.toHexString());
        n.setRead(doc.getBoolean("isRead", false));
        n.setCreatedAt(doc.getDate("createdAt"));
        return n;
    }
}
