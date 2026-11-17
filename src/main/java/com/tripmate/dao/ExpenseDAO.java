package com.tripmate.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.tripmate.model.Expense;
import com.tripmate.model.User;
import com.tripmate.util.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for MongoDB 'expenses' collection.
 */
public class ExpenseDAO {

    private final MongoCollection<Document> collection;
    private final UserDAO userDAO;

    public ExpenseDAO() {
        this.collection = MongoDBConnection.getCollection("expenses");
        this.userDAO = new UserDAO();
    }

    public boolean create(Expense expense) {
        try {
            if (!ObjectId.isValid(expense.getTripId())) return false;
            Document doc = new Document()
                    .append("tripId", new ObjectId(expense.getTripId()))
                    .append("paidBy", ObjectId.isValid(expense.getPaidBy()) ? new ObjectId(expense.getPaidBy()) : null)
                    .append("title", expense.getTitle())
                    .append("category", expense.getCategory() != null ? expense.getCategory() : "MISCELLANEOUS")
                    .append("amount", expense.getAmount())
                    .append("expenseDate", expense.getExpenseDate() != null ? expense.getExpenseDate() : new Date())
                    .append("paymentMethod", expense.getPaymentMethod() != null ? expense.getPaymentMethod() : "CARD")
                    .append("notes", expense.getNotes())
                    .append("createdAt", new Date());

            collection.insertOne(doc);
            expense.setId(doc.getObjectId("_id").toHexString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Expense findById(String id) {
        try {
            if (!ObjectId.isValid(id)) return null;
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return mapDocumentToExpense(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Expense> findByTripId(String tripId) {
        List<Expense> list = new ArrayList<>();
        try {
            if (!ObjectId.isValid(tripId)) return list;
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))
                    .sort(Sorts.descending("expenseDate"))) {
                Expense exp = mapDocumentToExpense(doc);
                if (exp != null) {
                    if (exp.getPaidBy() != null) {
                        User u = userDAO.findById(exp.getPaidBy());
                        if (u != null) exp.setPayerName(u.getFullName());
                    }
                    list.add(exp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public double calculateTotalByTrip(String tripId) {
        double total = 0.0;
        try {
            if (!ObjectId.isValid(tripId)) return total;
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))) {
                Double amt = doc.getDouble("amount");
                if (amt != null) total += amt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public Map<String, Double> getExpensesByCategory(String tripId) {
        Map<String, Double> categoryMap = new HashMap<>();
        try {
            if (!ObjectId.isValid(tripId)) return categoryMap;
            for (Document doc : collection.find(Filters.eq("tripId", new ObjectId(tripId)))) {
                String cat = doc.getString("category");
                Double amt = doc.getDouble("amount");
                if (cat != null && amt != null) {
                    categoryMap.put(cat, categoryMap.getOrDefault(cat, 0.0) + amt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryMap;
    }

    public boolean update(Expense expense) {
        try {
            if (!ObjectId.isValid(expense.getId())) return false;
            Bson updates = Updates.combine(
                    Updates.set("title", expense.getTitle()),
                    Updates.set("category", expense.getCategory()),
                    Updates.set("amount", expense.getAmount()),
                    Updates.set("expenseDate", expense.getExpenseDate()),
                    Updates.set("paymentMethod", expense.getPaymentMethod()),
                    Updates.set("notes", expense.getNotes())
            );
            return collection.updateOne(Filters.eq("_id", new ObjectId(expense.getId())), updates).getModifiedCount() > 0;
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

    private Expense mapDocumentToExpense(Document doc) {
        if (doc == null) return null;
        Expense exp = new Expense();
        exp.setId(doc.getObjectId("_id").toHexString());
        ObjectId tid = doc.getObjectId("tripId");
        if (tid != null) exp.setTripId(tid.toHexString());
        ObjectId pby = doc.getObjectId("paidBy");
        if (pby != null) exp.setPaidBy(pby.toHexString());
        exp.setTitle(doc.getString("title"));
        exp.setCategory(doc.getString("category"));
        exp.setAmount(doc.getDouble("amount") != null ? doc.getDouble("amount") : 0.0);
        exp.setExpenseDate(doc.getDate("expenseDate"));
        exp.setPaymentMethod(doc.getString("paymentMethod"));
        exp.setNotes(doc.getString("notes"));
        exp.setCreatedAt(doc.getDate("createdAt"));
        return exp;
    }
}
