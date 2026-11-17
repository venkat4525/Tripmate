package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Invitation;
import com.tripmate.model.Trip;
import com.tripmate.model.User;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for MongoDB 'invitations' collection.
 */
public class InvitationDAO {

    private final MongoCollection<Document> collection;
    private final UserDAO userDAO;
    private final TripDAO tripDAO;

    public InvitationDAO() {
        this.collection = MongoDBConnection.getCollection("invitations");
        this.userDAO = new UserDAO();
        this.tripDAO = new TripDAO();
    }

    public boolean create(Invitation inv) {
        try {
            if (!ObjectId.isValid(inv.getTripId()) || !ObjectId.isValid(inv.getInvitedBy())) {
                return false;
            }

            String token = UUID.randomUUID().toString();
            inv.setInvitationToken(token);

            Document doc = new Document()
                    .append("tripId", new ObjectId(inv.getTripId()))
                    .append("invitedBy", new ObjectId(inv.getInvitedBy()))
                    .append("inviteeEmail", inv.getInviteeEmail().toLowerCase().trim())
                    .append("role", inv.getRole() != null ? inv.getRole() : "VIEWER")
                    .append("status", "PENDING")
                    .append("invitationToken", token)
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            inv.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Invitation findByToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) return null;
            Document doc = collection.find(Filters.eq("invitationToken", token.trim())).first();
            return mapDocumentToInvitation(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Invitation> findPendingByEmail(String email) {
        List<Invitation> list = new ArrayList<>();
        try {
            if (email == null) return list;
            for (Document doc : collection.find(Filters.and(
                    Filters.eq("inviteeEmail", email.toLowerCase().trim()),
                    Filters.eq("status", "PENDING")
            ))) {
                Invitation inv = mapDocumentToInvitation(doc);
                if (inv != null) {
                    User u = userDAO.findById(inv.getInvitedBy());
                    if (u != null) inv.setInviterName(u.getFullName());
                    Trip t = tripDAO.findById(inv.getTripId());
                    if (t != null) inv.setTripTitle(t.getTitle());
                    list.add(inv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(String id, String newStatus) {
        try {
            if (!ObjectId.isValid(id)) return false;
            return collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                    Updates.set("status", newStatus)).getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Invitation mapDocumentToInvitation(Document doc) {
        if (doc == null) return null;
        Invitation inv = new Invitation();
        inv.setId(doc.getObjectId("_id").toHexString());
        ObjectId tid = doc.getObjectId("tripId");
        if (tid != null) inv.setTripId(tid.toHexString());
        ObjectId uid = doc.getObjectId("invitedBy");
        if (uid != null) inv.setInvitedBy(uid.toHexString());
        inv.setInviteeEmail(doc.getString("inviteeEmail"));
        inv.setRole(doc.getString("role"));
        inv.setStatus(doc.getString("status"));
        inv.setInvitationToken(doc.getString("invitationToken"));
        inv.setCreatedAt(doc.getDate("createdAt"));
        return inv;
    }
}
