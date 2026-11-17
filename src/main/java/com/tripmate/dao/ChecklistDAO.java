package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tripmate.model.ChecklistItem;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'checklist_items' collection.
 */
public class ChecklistDAO {

    private final MongoCollection<Document> collection;

    public ChecklistDAO() {
        this.collection = MongoDBConnection.getCollection("checklist_items");
    }

    public boolean create(ChecklistItem item) {
        try {
            if (!ObjectId.isValid(item.getTripId())) return false;
            Document doc = new Document()
                    .append("tripId", new ObjectId(item.getTripId()))
                    .append("assignedTo", ObjectId.isValid(item.getAssignedTo()) ? new ObjectId(item.getAssignedTo()) : null)
                    .append("category", item.getCategory() != null ? item.getCategory() : "MISCELLANEOUS")
                    .append("itemName", item.getItemName())
                    .append("isPacked", item.isPacked())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            item.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ChecklistItem findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToChecklistItem(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ChecklistItem> findByTripId(String tripId) {
        List<ChecklistItem> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(tripId)) return list;
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))) {
                ChecklistItem item = mapDocumentToChecklistItem(doc);
                if (item != null) list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean togglePacked(String id, boolean isPacked) {
        try {
            if (!ObjectId.isValid(id)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                    Updates.set("isPacked", isPacked)).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ChecklistItem item) {
        try {
            if (!ObjectId.isValid(item.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("category", item.getCategory()),
                    Updates.set("itemName", item.getItemName()),
                    Updates.set("isPacked", item.isPacked()),
                    Updates.set("assignedTo", ObjectId.isValid(item.getAssignedTo()) ? new ObjectId(item.getAssignedTo()) : null)
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(item.getId())), updates).getModifiedCount() > 0;
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

    public boolean deleteByTripId(String tripId) {
        try {
            if (!ObjectId.isValid(tripId)) return false;
            collection.deleteMany(Filters.eq("tripId", new ObjectId(tripId)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ChecklistItem mapDocumentToChecklistItem(Document doc) {
        if (doc == null) return null;
        ChecklistItem item = new ChecklistItem();
        item.setId(doc.getObjectId("_id").toHexString());
        ObjectId tid = doc.getObjectId("tripId");
        if (tid != null) item.setTripId(tid.toHexString());
        ObjectId uid = doc.getObjectId("assignedTo");
        if (uid != null) item.setAssignedTo(uid.toHexString());
        item.setCategory(doc.getString("category"));
        item.setItemName(doc.getString("itemName"));
        item.setPacked(doc.getBoolean("isPacked", false));
        item.setCreatedAt(doc.getDate("createdAt"));
        return item;
    }
}
