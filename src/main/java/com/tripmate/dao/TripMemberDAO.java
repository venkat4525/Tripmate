package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tripmate.model.TripMember;
import com.tripmate.model.User;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'trip_members' collection.
 * Manages group trip collaboration and access permissions.
 */
public class TripMemberDAO {

    private final MongoCollection<Document> collection;
    private final UserDAO userDAO;

    public TripMemberDAO() {
        this.collection = MongoDBConnection.getCollection("trip_members");
        this.userDAO = new UserDAO();
    }

    public boolean addMember(TripMember member) {
        try {
            if (!ObjectId.isValid(member.getTripId()) || !ObjectId.isValid(member.getUserId())) {
                return false;
            }

            // Check if already a member
            Document existing = collection.find(Filters.and(
                    Filters.eq("tripId", new ObjectId(member.getTripId())),
                    Filters.eq("userId", new ObjectId(member.getUserId()))
            )).first();

            if (existing != null) {
                return true; // Already joined
            }

            Document doc = new Document()
                    .append("tripId", new ObjectId(member.getTripId()))
                    .append("userId", new ObjectId(member.getUserId()))
                    .append("role", member.getRole() != null ? member.getRole() : "VIEWER")
                    .append("status", member.getStatus() != null ? member.getStatus() : "ACTIVE")
                    .append("joinedAt", new Date());

            collection.insertOne(doc);
            member.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TripMember> findByTripId(String tripId) {
        List<TripMember> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(tripId)) return list;
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))) {
                TripMember m = mapDocumentToTripMember(doc);
                if (m != null) {
                    // Populate user details for UI display
                    User u = userDAO.findById(m.getUserId());
                    if (u != null) {
                        m.setUserName(u.getFullName());
                        m.setUserEmail(u.getEmail());
                        m.setUserProfileImage(u.getProfileImage());
                    }
                    list.add(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> findTripIdsByUserId(String userId) {
        List<String> tripIds = new ArrayList<>();
        try {
            if (!ObjectId.isValid(userId)) return tripIds;
            for (Document doc : collection.find(Filters.and(
                    Filters.eq("userId", new ObjectId(userId)),
                    Filters.eq("status", "ACTIVE")
            ))) {
                ObjectId tid = doc.getObjectId("tripId");
                if (tid != null) tripIds.add(tid.toHexString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripIds;
    }

    public TripMember findMembership(String tripId, String userId) {
        try {
            if (!ObjectId.isValid(tripId) || !ObjectId.isValid(userId)) return null;
            Document doc = collection.find(Filters.and(
                    Filters.eq("tripId", new ObjectId(tripId)),
                    Filters.eq("userId", new ObjectId(userId))
            )).first();
            return mapDocumentToTripMember(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRole(String memberId, String newRole) {
        try {
            if (!ObjectId.isValid(memberId)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(memberId)),
                    Updates.set("role", newRole)).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeMember(String memberId) {
        try {
            if (!ObjectId.isValid(memberId)) return false;
            return collection.deleteOne(Filters.eq("_id", new ObjectId(memberId))).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user has edit permissions on a trip (either OWNER or EDITOR).
     */
    public boolean canEdit(String tripId, String userId) {
        TripMember member = findMembership(tripId, userId);
        if (member == null) return false;
        return "OWNER".equalsIgnoreCase(member.getRole()) || "EDITOR".equalsIgnoreCase(member.getRole());
    }

    private TripMember mapDocumentToTripMember(Document doc) {
        if (doc == null) return null;
        TripMember m = new TripMember();
        m.setId(doc.getObjectId("_id").toHexString());
        ObjectId tid = doc.getObjectId("tripId");
        if (tid != null) m.setTripId(tid.toHexString());
        ObjectId uid = doc.getObjectId("userId");
        if (uid != null) m.setUserId(uid.toHexString());
        m.setRole(doc.getString("role"));
        m.setStatus(doc.getString("status"));
        m.setJoinedAt(doc.getDate("joinedAt"));
        return m;
    }
}
