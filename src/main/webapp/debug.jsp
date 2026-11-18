<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tripmate.dao.DestinationDAO, com.tripmate.dao.CategoryDAO" %>
<%@ page import="com.tripmate.model.Destination, com.tripmate.model.Category" %>
<%@ page import="java.util.List" %>
<%
    out.println("<h2>TripMate Debug Page</h2>");
    try {
        CategoryDAO catDao = new CategoryDAO();
        List<Category> cats = catDao.findAll();
        out.println("<p>Categories found: " + cats.size() + "</p>");
        for (Category c : cats) {
            out.println("<p> - " + c.getName() + " (id=" + c.getId() + ")</p>");
        }
    } catch (Exception e) {
        out.println("<p style='color:red'>Category Error: " + e.getMessage() + "</p>");
        e.printStackTrace();
    }
    try {
        DestinationDAO destDao = new DestinationDAO();
        List<Destination> dests = destDao.findTopRated(10);
        out.println("<p>Destinations found: " + dests.size() + "</p>");
        for (Destination d : dests) {
            out.println("<p> - " + d.getName() + " | rating=" + d.getRating() + "</p>");
        }
    } catch (Exception e) {
        out.println("<p style='color:red'>Destination Error: " + e.getMessage() + "</p>");
        e.printStackTrace();
    }
%>
