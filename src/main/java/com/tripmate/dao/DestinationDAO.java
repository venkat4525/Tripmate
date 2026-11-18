package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Destination;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object for MongoDB 'destinations' collection.
 */
public class DestinationDAO {

    private static final Logger LOGGER = Logger.getLogger(DestinationDAO.class.getName());
    private final MongoCollection<Document> collection;

    public DestinationDAO() {
        this.collection = MongoDBConnection.getCollection("destinations");
    }

    public boolean create(Destination dest) {
        try {
            Document doc = new Document()
                    .append("name", dest.getName())
                    .append("country", dest.getCountry())
                    .append("city", dest.getCity())
                    .append("categoryId", ObjectId.isValid(dest.getCategoryId()) ? new ObjectId(dest.getCategoryId()) : null)
                    .append("description", dest.getDescription())
                    .append("highlights", dest.getHighlights())
                    .append("duration", dest.getDuration())
                    .append("bestSeason", dest.getBestSeason())
                    .append("averageCost", dest.getAverageCost())
                    .append("imageUrl", dest.getImageUrl())
                    .append("rating", dest.getRating())
                    .append("tags", dest.getTags())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            dest.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Destination findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToDestination(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Destination> findAll() {
        List<Destination> list = new ArrayList<>();
        try {
            for (Document doc : collection.find().sort(Sorts.descending("rating"))) {
                Destination d = mapDocumentToDestination(doc);
                if (d != null) list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Destination> findByCategory(String categoryId) {
        List<Destination> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(categoryId)) return list;
            for (Document doc : collection.find(Filters.eq("categoryId", new ObjectId(categoryId)))) {
                Destination d = mapDocumentToDestination(doc);
                if (d != null) list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Destination> search(String query, String categoryId, Double maxCost, String country) {
        List<Destination> list = new ArrayList<>();
        try {
            List<Bson> filters = new ArrayList<>();

            if (query != null && !query.trim().isEmpty()) {
                String q = query.trim();
                filters.add(Filters.or(
                        Filters.regex("name", q, "i"),
                        Filters.regex("city", q, "i"),
                        Filters.regex("country", q, "i"),
                        Filters.regex("description", q, "i")
                ));
            }

            if (categoryId != null && !categoryId.trim().isEmpty() && ObjectId.isValid(categoryId)) {
                filters.add(Filters.eq("categoryId", new ObjectId(categoryId)));
            }

            if (country != null && !country.trim().isEmpty()) {
                filters.add(Filters.regex("country", country.trim(), "i"));
            }

            if (maxCost != null && maxCost > 0) {
                filters.add(Filters.lte("averageCost", maxCost));
            }

            Bson finalFilter = filters.isEmpty() ? new Document() : Filters.and(filters);

            for (Document doc : collection.find(finalFilter).sort(Sorts.descending("rating"))) {
                Destination d = mapDocumentToDestination(doc);
                if (d != null) list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Destination> findTopRated(int limit) {
        List<Destination> list = new ArrayList<>();
        try {
            LOGGER.info("[DestinationDAO] findTopRated called with limit=" + limit);
            long total = collection.countDocuments();
            LOGGER.info("[DestinationDAO] Total destinations in DB: " + total);
            for (Document doc : collection.find().sort(Sorts.descending("rating")).limit(limit)) {
                Destination d = mapDocumentToDestination(doc);
                if (d != null) {
                    list.add(d);
                    LOGGER.info("[DestinationDAO] Loaded: " + d.getName());
                }
            }
            LOGGER.info("[DestinationDAO] findTopRated returning " + list.size() + " items");
        } catch (Exception e) {
            LOGGER.severe("[DestinationDAO] findTopRated FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(Destination dest) {
        try {
            if (!ObjectId.isValid(dest.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("name", dest.getName()),
                    Updates.set("country", dest.getCountry()),
                    Updates.set("city", dest.getCity()),
                    Updates.set("categoryId", ObjectId.isValid(dest.getCategoryId()) ? new ObjectId(dest.getCategoryId()) : null),
                    Updates.set("description", dest.getDescription()),
                    Updates.set("highlights", dest.getHighlights()),
                    Updates.set("duration", dest.getDuration()),
                    Updates.set("bestSeason", dest.getBestSeason()),
                    Updates.set("averageCost", dest.getAverageCost()),
                    Updates.set("imageUrl", dest.getImageUrl()),
                    Updates.set("rating", dest.getRating()),
                    Updates.set("tags", dest.getTags())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(dest.getId())), updates).getModifiedCount() > 0;
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

    public long countDestinations() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private Destination mapDocumentToDestination(Document doc) {
        if (doc == null) return null;
        Destination d = new Destination();
        d.setId(doc.getObjectId("_id").toHexString());
        d.setName(doc.getString("name"));
        d.setCountry(doc.getString("country"));
        d.setCity(doc.getString("city"));
        ObjectId catId = doc.getObjectId("categoryId");
        if (catId != null) {
            d.setCategoryId(catId.toHexString());
        }
        d.setDescription(doc.getString("description"));
        d.setHighlights(doc.getString("highlights"));
        d.setDuration(doc.getString("duration"));
        d.setBestSeason(doc.getString("bestSeason"));
        // averageCost / rating may be stored as Integer or Double in MongoDB
        Object costObj = doc.get("averageCost");
        if (costObj instanceof Number) {
            d.setAverageCost(((Number) costObj).doubleValue());
        } else {
            d.setAverageCost(0.0);
        }
        d.setImageUrl(doc.getString("imageUrl"));
        Object ratingObj = doc.get("rating");
        if (ratingObj instanceof Number) {
            d.setRating(((Number) ratingObj).doubleValue());
        } else {
            d.setRating(5.0);
        }
        List<String> tags = (List<String>) doc.get("tags");
        if (tags != null) {
            d.setTags(tags);
        }
        d.setCreatedAt(doc.getDate("createdAt"));
        return d;
    }
}
