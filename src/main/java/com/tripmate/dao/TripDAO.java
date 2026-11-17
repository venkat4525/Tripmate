package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Trip;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'trips' collection.
 */
public class TripDAO {

    private final MongoCollection<Document> collection;

    public TripDAO() {
        this.collection = MongoDBConnection.getCollection("trips");
    }

    public boolean create(Trip trip) {
        try {
            Document doc = new Document()
                    .append("userId", ObjectId.isValid(trip.getUserId()) ? new ObjectId(trip.getUserId()) : null)
                    .append("title", trip.getTitle())
                    .append("destinationId", ObjectId.isValid(trip.getDestinationId()) ? new ObjectId(trip.getDestinationId()) : null)
                    .append("destinationName", trip.getDestinationName())
                    .append("startDate", trip.getStartDate())
                    .append("endDate", trip.getEndDate())
                    .append("estimatedBudget", trip.getEstimatedBudget())
                    .append("actualExpenses", trip.getActualExpenses())
                    .append("status", trip.getStatus() != null ? trip.getStatus() : "PLANNING")
                    .append("visibility", trip.getVisibility() != null ? trip.getVisibility() : "PRIVATE")
                    .append("notes", trip.getNotes())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            trip.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Trip findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToTrip(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Trip> findByUserId(String userId) {
        List<Trip> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(userId)) return list;
            for (Document doc : collection.find(Filters.eq("userId", new ObjectId(userId))).sort(Sorts.descending("createdAt"))) {
                Trip t = mapDocumentToTrip(doc);
                if (t != null) list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Trip> findAll() {
        List<Trip> list = new ArrayList<>();
        try {
            for (Document doc : collection.find().sort(Sorts.descending("createdAt"))) {
                Trip t = mapDocumentToTrip(doc);
                if (t != null) list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(Trip trip) {
        try {
            if (!ObjectId.isValid(trip.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("title", trip.getTitle()),
                    Updates.set("destinationId", ObjectId.isValid(trip.getDestinationId()) ? new ObjectId(trip.getDestinationId()) : null),
                    Updates.set("destinationName", trip.getDestinationName()),
                    Updates.set("startDate", trip.getStartDate()),
                    Updates.set("endDate", trip.getEndDate()),
                    Updates.set("estimatedBudget", trip.getEstimatedBudget()),
                    Updates.set("status", trip.getStatus()),
                    Updates.set("visibility", trip.getVisibility()),
                    Updates.set("notes", trip.getNotes())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(trip.getId())), updates).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateActualExpenses(String tripId, double actualExpenses) {
        try {
            if (!ObjectId.isValid(tripId)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(tripId)),
                    Updates.set("actualExpenses", actualExpenses)).getModifiedCount() > 0;
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

    public long countTrips() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            return 0;
        }
    }

    public long countTripsByUser(String userId) {
        try {
            if (!ObjectId.isValid(userId)) return 0;
            return collection.countDocuments(Filters.eq("userId", new ObjectId(userId)));
        } catch (Exception e) {
            return 0;
        }
    }

    private Trip mapDocumentToTrip(Document doc) {
        if (doc == null) return null;
        Trip t = new Trip();
        t.setId(doc.getObjectId("_id").toHexString());
        ObjectId uid = doc.getObjectId("userId");
        if (uid != null) t.setUserId(uid.toHexString());
        t.setTitle(doc.getString("title"));
        ObjectId destId = doc.getObjectId("destinationId");
        if (destId != null) t.setDestinationId(destId.toHexString());
        t.setDestinationName(doc.getString("destinationName"));
        t.setStartDate(doc.getDate("startDate"));
        t.setEndDate(doc.getDate("endDate"));
        t.setEstimatedBudget(doc.getDouble("estimatedBudget") != null ? doc.getDouble("estimatedBudget") : 0.0);
        t.setActualExpenses(doc.getDouble("actualExpenses") != null ? doc.getDouble("actualExpenses") : 0.0);
        t.setStatus(doc.getString("status"));
        t.setVisibility(doc.getString("visibility"));
        t.setNotes(doc.getString("notes"));
        t.setCreatedAt(doc.getDate("createdAt"));
        return t;
    }
}
