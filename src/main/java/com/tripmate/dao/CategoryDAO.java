package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Category;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for MongoDB 'categories' collection.
 */
public class CategoryDAO {

    private final MongoCollection<Document> collection;

    public CategoryDAO() {
        this.collection = MongoDBConnection.getCollection("categories");
    }

    public boolean create(Category category) {
        try {
            Document doc = new Document()
                    .append("name", category.getName().trim())
                    .append("description", category.getDescription())
                    .append("icon", category.getIcon())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            category.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Category findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToCategory(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        try {
            for (Document doc : collection.find().sort(Sorts.ascending("name"))) {
                Category c = mapDocumentToCategory(doc);
                if (c != null) list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(Category category) {
        try {
            if (!ObjectId.isValid(category.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("name", category.getName().trim()),
                    Updates.set("description", category.getDescription()),
                    Updates.set("icon", category.getIcon())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(category.getId())), updates).getModifiedCount() > 0;
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

    public long countCategories() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            return 0;
        }
    }

    private Category mapDocumentToCategory(Document doc) {
        if (doc == null) return null;
        Category c = new Category();
        c.setId(doc.getObjectId("_id").toHexString());
        c.setName(doc.getString("name"));
        c.setDescription(doc.getString("description"));
        c.setIcon(doc.getString("icon"));
        c.setCreatedAt(doc.getDate("createdAt"));
        return c;
    }
}
