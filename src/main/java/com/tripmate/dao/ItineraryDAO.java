package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.ItineraryItem;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'itinerary_items' collection.
 */
public class ItineraryDAO {

    private final MongoCollection<Document> collection;

    public ItineraryDAO() {
        this.collection = MongoDBConnection.getCollection("itinerary_items");
    }

    public boolean create(ItineraryItem item) {
        try {
            if (!ObjectId.isValid(item.getTripId())) return false;
            Document doc = new Document()
                    .append("tripId", new ObjectId(item.getTripId()))
                    .append("dayNumber", item.getDayNumber())
                    .append("timeSlot", item.getTimeSlot())
                    .append("activityTitle", item.getActivityTitle())
                    .append("location", item.getLocation())
                    .append("estimatedCost", item.getEstimatedCost())
                    .append("notes", item.getNotes())
                    .append("isCompleted", item.isCompleted())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            item.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ItineraryItem findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToItineraryItem(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ItineraryItem> findByTripId(String tripId) {
        List<ItineraryItem> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(tripId)) return list;
            // Sort by day number ascending, then time slot
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))
                    .sort(Sorts.ascending("dayNumber", "timeSlot"))) {
                ItineraryItem item = mapDocumentToItineraryItem(doc);
                if (item != null) list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ItineraryItem> findByTripAndDay(String tripId, int dayNumber) {
        List<ItineraryItem> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(tripId)) return list;
            for (Document doc : collection.find(Filters.and(
                    Filters.eq("tripId", new ObjectId(tripId)),
                    Filters.eq("dayNumber", dayNumber)
            )).sort(Sorts.ascending("timeSlot"))) {
                ItineraryItem item = mapDocumentToItineraryItem(doc);
                if (item != null) list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(ItineraryItem item) {
        try {
            if (!ObjectId.isValid(item.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("dayNumber", item.getDayNumber()),
                    Updates.set("timeSlot", item.getTimeSlot()),
                    Updates.set("activityTitle", item.getActivityTitle()),
                    Updates.set("location", item.getLocation()),
                    Updates.set("estimatedCost", item.getEstimatedCost()),
                    Updates.set("notes", item.getNotes()),
                    Updates.set("isCompleted", item.isCompleted())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(item.getId())), updates).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean toggleCompletion(String id, boolean isCompleted) {
        try {
            if (!ObjectId.isValid(id)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                    Updates.set("isCompleted", isCompleted)).getModifiedCount() > 0;
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

    private ItineraryItem mapDocumentToItineraryItem(Document doc) {
        if (doc == null) return null;
        ItineraryItem item = new ItineraryItem();
        item.setId(doc.getObjectId("_id").toHexString());
        ObjectId tid = doc.getObjectId("tripId");
        if (tid != null) item.setTripId(tid.toHexString());
        item.setDayNumber(doc.getInteger("dayNumber", 1));
        item.setTimeSlot(doc.getString("timeSlot"));
        item.setActivityTitle(doc.getString("activityTitle"));
        item.setLocation(doc.getString("location"));
        item.setEstimatedCost(doc.getDouble("estimatedCost") != null ? doc.getDouble("estimatedCost") : 0.0);
        item.setNotes(doc.getString("notes"));
        item.setCompleted(doc.getBoolean("isCompleted", false));
        item.setCreatedAt(doc.getDate("createdAt"));
        return item;
    }
}
