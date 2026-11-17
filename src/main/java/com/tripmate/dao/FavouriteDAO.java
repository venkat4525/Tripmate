package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.tripmate.model.Destination;
import com.tripmate.model.Favourite;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'favourites' collection.
 */
public class FavouriteDAO {

    private final MongoCollection<Document> collection;
    private final DestinationDAO destinationDAO;

    public FavouriteDAO() {
        this.collection = MongoDBConnection.getCollection("favourites");
        this.destinationDAO = new DestinationDAO();
    }

    public boolean addFavourite(String userId, String destinationId) {
        try {
            if (!ObjectId.isValid(userId) || !ObjectId.isValid(destinationId)) return false;

            // Check if already added
            if (isFavourited(userId, destinationId)) return true;

            Document doc = new Document()
                    .append("userId", new ObjectId(userId))
                    .append("destinationId", new ObjectId(destinationId))
                    .append("addedAt", new Date());

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavourite(String userId, String destinationId) {
        try {
            if (!ObjectId.isValid(userId) || !ObjectId.isValid(destinationId)) return false;
            return collection.deleteOne(Filters.and(
                    Filters.eq("userId", new ObjectId(userId)),
                    Filters.eq("destinationId", new ObjectId(destinationId))
            )).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFavourited(String userId, String destinationId) {
        try {
            if (!ObjectId.isValid(userId) || !ObjectId.isValid(destinationId)) return false;
            Document doc = collection.find(Filters.and(
                    Filters.eq("userId", new ObjectId(userId)),
                    Filters.eq("destinationId", new ObjectId(destinationId))
            )).first();
            return doc != null;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Favourite> findByUserId(String userId) {
        List<Favourite> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(userId)) return list;
            for (Document doc : collection.find(Filters.eq("userId", new ObjectId(userId)))
                    .sort(Sorts.descending("addedAt"))) {
                Favourite fav = new Favourite();
                fav.setId(doc.getObjectId("_id").toHexString());
                fav.setUserId(doc.getObjectId("userId").toHexString());
                String destId = doc.getObjectId("destinationId").toHexString();
                fav.setDestinationId(destId);
                fav.setAddedAt(doc.getDate("addedAt"));

                // Join Destination
                Destination d = destinationDAO.findById(destId);
                if (d != null) {
                    fav.setDestination(d);
                    list.add(fav);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
