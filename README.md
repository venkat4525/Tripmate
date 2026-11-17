# TripMate – Smart Travel Planning and Trip Management System

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.0-blue.svg)](https://jakarta.ee/)
[![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.1-yellow.svg)](https://tomcat.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas%20%2F%20Compass-green.svg)](https://www.mongodb.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)

**TripMate** is a modern, responsive, full-stack travel planning and trip management web application developed for **Advanced Java Programming**. Built using **MVC (Model-View-Controller)** architecture with **Jakarta Servlets**, **JSP + JSTL**, and the official **MongoDB Java Sync Driver**.

---

## 🌟 Key Features

- **Destination Discovery & Filters**: Browse curated Indian and international travel packages across categories (*Mountains & Trekking, Historical & Heritage, Wildlife & Nature, Beach & Coastal, Urban & City Life*) with real-time keyword, country, and price search.
- **Trip Creation & Management**: Create personalized trips with estimated budgets, travel dates, notes, and duration tracking.
- **Day-Wise Itinerary**: Organize daily schedules with time slots, activities, estimated costs, and interactive completion checkmarks.
- **Budget & Expense Tracking**: Itemize trip expenses categorized by Accommodation, Food, Transportation, and Activities with live remaining budget calculations.
- **Packing Checklist**: Interactive packing list grouped into Documents, Clothing, Electronics, and Medicine with 1-click status toggling.
- **Group Collaboration**: Invite friends via email to view or edit shared trips.
- **Printable Travel Dossier**: Clean, high-contrast, print-friendly trip summary view with 1-click PDF export.
- **Administrator Panel**: Global system overview, user activation toggling, and destination catalog management.
- **Enterprise Security**: Industry-standard **BCrypt (Blowfish)** password hashing, session fixation prevention, and role-based access control (`ADMIN` vs `USER`).

---

## 🛠️ Technology Stack

| Layer | Technologies |
|:---|:---|
| **Frontend** | HTML5, CSS3, JavaScript, Bootstrap 5.3, Bootstrap Icons, JSP, JSTL (Jakarta Tags Core) |
| **Backend** | Java 17/21, Jakarta Servlet API 6.0, Jakarta JSP API 3.1 |
| **Server** | Apache Tomcat 10.1 (supports Embedded 1-Click Launch and Standalone WAR deployment) |
| **Database** | MongoDB Atlas (Cloud) & MongoDB Compass (Local/GUI), MongoDB Java Sync Driver 5.1.0 |
| **Security** | jBCrypt 0.4 (Blowfish with salt), HttpOnly Cookie Session Management |
| **Build Tool** | Apache Maven 3.9 |

---

## 📂 Project Structure

```text
TripMate/
├── pom.xml                               # Maven dependencies & build configuration
├── run.bat                               # 1-Click Windows launch script
├── database/
│   └── sample-data.js                    # MongoDB initialization script (collections & sample data)
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── tripmate/
        │           ├── Main.java         # Embedded Tomcat 10.1 1-click application launcher
        │           ├── controller/       # Servlets handling routing, auth & business flow
        │           ├── dao/              # Data Access Objects (MongoDB CRUD operations)
        │           ├── model/            # Plain Old Java Objects (Entities)
        │           └── util/             # DB Connection pooling, BCrypt, Date & Validation utils
        └── webapp/
            ├── index.jsp                 # Entry redirector
            ├── assets/
            │   ├── css/style.css         # Custom Navy & Violet theme styles
            │   ├── js/                   # Client-side scripts
            │   └── images/               # Media assets
            └── WEB-INF/
                ├── web.xml               # Servlet descriptors & session config
                └── views/
                    ├── auth/             # Login & Register views
                    ├── user/             # Dashboard, Trip Hub, Itinerary, Expense & Summary views
                    ├── destinations/     # Destinations catalog, details & favourites views
                    ├── admin/            # Admin analytics, user & destination management
                    └── common/           # Reusable header, navbar, footer & sidebar components
```

---

## 🚀 Getting Started

### 1. Prerequisites
- **Java JDK 17 or 21 LTS**
- **MongoDB Compass** or **MongoDB Atlas**
- **Git**

### 2. Database Setup (MongoDB Compass)
1. Open **MongoDB Compass** and connect to `mongodb://localhost:27017` (or your Atlas cluster).
2. Open the **`>_ _MONGOSH`** terminal at the bottom-right.
3. Paste the contents of `database/sample-data.js` and hit **Enter**.
4. The database `tripmate_db` will be initialized with all 11 collections and sample records.

### 3. Running the Project

#### Way 1: 1-Click Launch (Batch Script)
Simply double-click `run.bat` or run in terminal:
```powershell
.\run.bat
```

#### Way 2: Inside IntelliJ IDEA
1. Open the project folder in **IntelliJ IDEA**.
2. Open `src/main/java/com/tripmate/Main.java`.
3. Click the green **Play (▶)** button next to `main()`.

#### Way 3: Via Maven Command Line
```powershell
mvn compile exec:java
```

Once started, open your browser at:
👉 **`http://localhost:8080/TripMate/`** (or `http://localhost:8080/`)

---

## 🔑 Demo Accounts

| Role | Email | Password |
|:---|:---|:---|
| **Administrator** | `admin@tripmate.com` | `Admin@123` |
| **Normal User 1** | `john.doe@example.com` | `User@123` |
| **Normal User 2** | `jane.smith@example.com` | `User@123` |

*(Quick 1-click credential buttons are also available directly on the login page)*.

---

## 📜 License
This project is developed for educational purposes as an Advanced Java Programming college project.
