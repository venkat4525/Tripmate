package com.tripmate.controller;

import com.tripmate.dao.CategoryDAO;
import com.tripmate.dao.DestinationDAO;
import com.tripmate.model.Category;
import com.tripmate.model.Destination;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller serving the Public Landing / Home Page.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private DestinationDAO destinationDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        this.destinationDAO = new DestinationDAO();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        List<Destination> topDestinations = destinationDAO.findTopRated(6);

        req.setAttribute("categories", categories);
        req.setAttribute("topDestinations", topDestinations);
        req.setAttribute("activeNav", "home");
        req.setAttribute("pageTitle", "TripMate - Smart Travel Planning & Itinerary Management");

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
