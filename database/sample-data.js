/**
 * TripMate - Smart Travel Planning and Trip Management System
 * Database Initialization with All 8+ Travel Packages & Categories from User Designs
 * Database Name: tripmate_db
 */

db = db.getSiblingDB("tripmate_db");

print("=================================================");
print("Updating tripmate_db with All Category Tour Packages...");
print("=================================================");

// 1. Drop existing collections
const collectionsToDrop = [
    "users", "categories", "destinations", "trips", 
    "trip_members", "itinerary_items", "expenses", 
    "checklist_items", "favourites", "invitations", "notifications"
];
collectionsToDrop.forEach(col => db.getCollection(col).drop());

// 2. Indexes
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "role": 1 });
db.categories.createIndex({ "name": 1 }, { unique: true });
db.destinations.createIndex({ "name": "text", "description": "text", "city": "text", "country": "text" });
db.destinations.createIndex({ "categoryId": 1 });
db.destinations.createIndex({ "rating": -1 });
db.trips.createIndex({ "userId": 1 });
db.trip_members.createIndex({ "tripId": 1, "userId": 1 }, { unique: true });
db.itinerary_items.createIndex({ "tripId": 1, "dayNumber": 1 });
db.expenses.createIndex({ "tripId": 1 });
db.checklist_items.createIndex({ "tripId": 1, "isPacked": 1 });
db.favourites.createIndex({ "userId": 1, "destinationId": 1 }, { unique: true });

// 3. ObjectIds
const adminId = new ObjectId("660000000000000000000001");
const userId1 = new ObjectId("660000000000000000000002");
const userId2 = new ObjectId("660000000000000000000003");

const catBeachId    = new ObjectId("661000000000000000000001");
const catHistoryId  = new ObjectId("661000000000000000000002");
const catMountainId = new ObjectId("661000000000000000000003");
const catCityId     = new ObjectId("661000000000000000000004");
const catNatureId   = new ObjectId("661000000000000000000005");

const trip1Id = new ObjectId("663000000000000000000001");

// Passwords (Admin@123 / User@123)
const adminHash = "$2a$10$gn/okywPF/HFKcFnE9/Y2usWQuSFz2wV1pdzad4.vGkyxMOpNfRcK";
const userHash  = "$2a$10$ua5FsR1YPnchMuKc9f27C.x732yprQP0bxlxHXzOkAG8J9nTZxscS";

// 4. Users
db.users.insertMany([
    { _id: adminId, fullName: "System Administrator", email: "admin@tripmate.com", password: adminHash, role: "ADMIN", phone: "+1 555-0199", profileImage: "assets/images/profiles/admin.png", isActive: true, createdAt: new Date() },
    { _id: userId1, fullName: "John Doe", email: "john.doe@example.com", password: userHash, role: "USER", phone: "+1 555-0144", profileImage: "assets/images/profiles/john.png", isActive: true, createdAt: new Date() },
    { _id: userId2, fullName: "Jane Smith", email: "jane.smith@example.com", password: userHash, role: "USER", phone: "+1 555-0177", profileImage: "assets/images/profiles/jane.png", isActive: true, createdAt: new Date() }
]);

// 5. Travel Categories
db.categories.insertMany([
    {
        _id: catBeachId,
        name: "Beach & Coastal",
        description: "Sun, golden sands, coral reefs, and coastal retreats.",
        icon: "bi-umbrella-fill",
        createdAt: new Date()
    },
    {
        _id: catHistoryId,
        name: "Historical & Heritage",
        description: "Ancient heritage, monumental temples, and historic architecture.",
        icon: "bi-bank2",
        createdAt: new Date()
    },
    {
        _id: catMountainId,
        name: "Mountains & Trekking",
        description: "Alpine peaks, scenic trekking routes, and mountain air.",
        icon: "bi-triangle-fill",
        createdAt: new Date()
    },
    {
        _id: catCityId,
        name: "Urban & City Life",
        description: "Skylines, vibrant street markets, and culinary capitals.",
        icon: "bi-buildings-fill",
        createdAt: new Date()
    },
    {
        _id: catNatureId,
        name: "Wildlife & Nature",
        description: "National parks, wildlife safaris, and pristine reserves.",
        icon: "bi-tree-fill",
        createdAt: new Date()
    }
]);

// 6. Complete Travel Places & Packages (Matching User Screenshot exactly)
const destNorthIndiaId = new ObjectId("662000000000000000000001");
const destHimachalId   = new ObjectId("662000000000000000000002");
const destGujaratId    = new ObjectId("662000000000000000000003");
const destGoldenTrId   = new ObjectId("662000000000000000000004");
const destTharDesertId = new ObjectId("662000000000000000000005");
const destSolangId     = new ObjectId("662000000000000000000006");
const destBodhGayaId   = new ObjectId("662000000000000000000007");
const destSeaLinkId    = new ObjectId("662000000000000000000008");
const destGoaId        = new ObjectId("662000000000000000000009");
const destSwissId      = new ObjectId("662000000000000000000010");

db.destinations.insertMany([
    {
        _id: destNorthIndiaId,
        name: "Delightful North India Tour Package",
        country: "India",
        city: "Delhi, Haridwar, Mussoorie, Shimla, Manali, Chandigarh",
        categoryId: catMountainId,
        duration: "12 Nights 13 Days",
        description: "Grand exploration across Northern India covering historic imperial monuments, holy Ganga aarti in Haridwar, scenic Queen of Hills Mussoorie, and snowy Himalayan retreats.",
        highlights: "Har Ki Pauri Ganga Aarti, Kempty Falls, Solang Valley Snow, Mall Road Shimla, Rock Garden Chandigarh",
        bestSeason: "March to June & September to November",
        averageCost: 1150.00,
        imageUrl: "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=800&q=80",
        rating: 4.90,
        tags: ["North India", "Himalayas", "Heritage", "Snow"],
        createdAt: new Date()
    },
    {
        _id: destHimachalId,
        name: "Enchanting Himachal Tour",
        country: "India",
        city: "Delhi, Shimla, Manali, Chandigarh",
        categoryId: catMountainId,
        duration: "6 Nights 7 Days",
        description: "Experience the magic of Himachal Pradesh with snow-covered mountain peaks, pine forests, hot springs, and colonial charm in Shimla and Manali.",
        highlights: "Kufri Viewpoint, Hadimba Devi Temple, Solang Valley, Rohtang Pass View, Pinjore Gardens",
        bestSeason: "April to June (Summer) & Dec to Feb (Snow)",
        averageCost: 680.00,
        imageUrl: "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=800&q=80",
        rating: 4.85,
        tags: ["Himachal", "Mountains & Trekking", "Snow", "Cottages"],
        createdAt: new Date()
    },
    {
        _id: destGujaratId,
        name: "Summer Special Holiday Package",
        country: "India",
        city: "Ahmedabad, Dwarka, Porbandar, Sasan Gir, Gondal",
        categoryId: catNatureId,
        duration: "5 Nights 6 Days",
        description: "Discover the vibrant heritage of Gujarat featuring sacred coastal temples, Mahatma Gandhi birth memorial, and exhilarating Asiatic Lion safaris in Sasan Gir National Park.",
        highlights: "Dwarkadhish Temple, Asiatic Lion Open Jeep Safari, Somnath Jyotirlinga, Kirti Mandir",
        bestSeason: "October to March",
        averageCost: 590.00,
        imageUrl: "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=800&q=80",
        rating: 4.80,
        tags: ["Wildlife & Nature", "Temples", "Safaris", "Gujarat"],
        createdAt: new Date()
    },
    {
        _id: destGoldenTrId,
        name: "Grand Delhi Agra Jaipur Tour",
        country: "India",
        city: "Delhi, Agra, Jaipur(B)",
        categoryId: catHistoryId,
        duration: "3 Nights 4 Days",
        description: "India's renowned Golden Triangle circuit encompassing Delhi's Mughal architecture, the world-wonder Taj Mahal in Agra, and the royal pink sandstone palaces of Jaipur.",
        highlights: "Sunrise at Taj Mahal, Agra Fort, Amber Fort Palace, Hawa Mahal, Qutub Minar",
        bestSeason: "October to April",
        averageCost: 450.00,
        imageUrl: "https://images.unsplash.com/photo-1599661046827-dacff0c0f09a?auto=format&fit=crop&w=800&q=80",
        rating: 4.92,
        tags: ["Historical & Heritage", "Golden Triangle", "Taj Mahal", "Palaces"],
        createdAt: new Date()
    },
    {
        _id: destTharDesertId,
        name: "Royal Rajasthan & Thar Desert Safari",
        country: "India",
        city: "Jaisalmer, Sam Sand Dunes, Jodhpur",
        categoryId: catHistoryId,
        duration: "4 Nights 5 Days",
        description: "Ride golden camel caravans over undulating sand dunes at sunset, stay in luxurious desert camps, and marvel at the golden sandstone fortresses of Rajasthan.",
        highlights: "Sunset Camel Trek, Sam Sand Dunes Camping, Kalbeliya Dance, Jaisalmer Living Fort",
        bestSeason: "October to March",
        averageCost: 540.00,
        imageUrl: "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?auto=format&fit=crop&w=800&q=80",
        rating: 4.90,
        tags: ["Desert Safari", "Camels", "Sand Dunes", "Rajasthan"],
        createdAt: new Date()
    },
    {
        _id: destSolangId,
        name: "Solang Valley Ropeway & Ski Resort",
        country: "India",
        city: "Manali, Solang Valley, Rohtang, Kullu",
        categoryId: catMountainId,
        duration: "4 Nights 5 Days",
        description: "Thrilling alpine playground famous for its state-of-the-art gondola ropeway, snow slopes, paragliding, skiing, and snowmobile tours.",
        highlights: "Solang Ropeway Cable Car, Skiing & Snowboarding, Zorbing, Atal Tunnel Excursion",
        bestSeason: "December to April",
        averageCost: 520.00,
        imageUrl: "https://images.unsplash.com/photo-1595815771614-ade9d652a65d?auto=format&fit=crop&w=800&q=80",
        rating: 4.88,
        tags: ["Mountains & Trekking", "Skiing", "Ropeway", "Adventure"],
        createdAt: new Date()
    },
    {
        _id: destBodhGayaId,
        name: "Spiritual Bodh Gaya Buddhist Circuit",
        country: "India",
        city: "Bodh Gaya, Rajgir, Nalanda, Patna",
        categoryId: catHistoryId,
        duration: "5 Nights 6 Days",
        description: "Journey to the holy cradle of Buddhism where Lord Buddha attained supreme enlightenment under the sacred Bodhi Tree.",
        highlights: "Mahabodhi Temple UNESCO Site, 80-Foot Great Buddha Statue, Ancient Nalanda University, Vulture Peak Rajgir",
        bestSeason: "October to March",
        averageCost: 550.00,
        imageUrl: "https://images.unsplash.com/photo-1544735716-392fe2489ffa?auto=format&fit=crop&w=800&q=80",
        rating: 4.90,
        tags: ["Historical & Heritage", "Spiritual", "Buddhism", "Monuments"],
        createdAt: new Date()
    },
    {
        _id: destSeaLinkId,
        name: "Mumbai Coastal Wonders & Sea Link",
        country: "India",
        city: "Mumbai, Bandra, Marine Drive",
        categoryId: catCityId,
        duration: "3 Nights 4 Days",
        description: "Experience the bustling City of Dreams featuring the architectural marvel of the Bandra-Worli Sea Link, Queen's Necklace, and Bollywood glamour.",
        highlights: "Bandra-Worli Sea Link, Gateway of India, Marine Drive Promenade, Elephanta Caves",
        bestSeason: "November to February",
        averageCost: 480.00,
        imageUrl: "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=800&q=80",
        rating: 4.82,
        tags: ["Urban & City Life", "Sea Link", "Metropolis", "Architecture"],
        createdAt: new Date()
    },
    {
        _id: destGoaId,
        name: "Goa Beach Holiday & Coastal Retreat",
        country: "India",
        city: "Panaji, Calangute, Baga, Palolem",
        categoryId: catBeachId,
        duration: "4 Nights 5 Days",
        description: "Relax on sun-kissed golden beaches with swaying coconut palms, Portuguese colonial heritage churches, and exciting coastal water adventures.",
        highlights: "Baga & Calangute Watersports, Sunset Cruise on Mandovi River, Fort Aguada, Dudhsagar Waterfalls",
        bestSeason: "November to March",
        averageCost: 490.00,
        imageUrl: "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
        rating: 4.86,
        tags: ["Beach & Coastal", "Beaches", "Watersports", "Nightlife"],
        createdAt: new Date()
    },
    {
        _id: destSwissId,
        name: "Swiss Alps Winter Retreat",
        country: "Switzerland",
        city: "Zermatt, Interlaken",
        categoryId: catMountainId,
        duration: "6 Nights 7 Days",
        description: "Towering snow-clad peaks, scenic alpine trains, world-class skiing, and peaceful chalet villages.",
        highlights: "Matterhorn Viewpoint, Glacier Express, Gornergrat Railway, Fondue Dining",
        bestSeason: "December to April",
        averageCost: 2400.00,
        imageUrl: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=800&q=80",
        rating: 4.90,
        tags: ["Mountains & Trekking", "Snow", "Skiing", "Scenic"],
        createdAt: new Date()
    }
]);

// 7. Sample Trip for testing
db.trips.insertMany([
    {
        _id: trip1Id,
        userId: userId1,
        title: "North India Grand Vacation 2026",
        destinationId: destNorthIndiaId,
        destinationName: "Delightful North India Tour Package",
        startDate: new Date("2026-10-10T00:00:00Z"),
        endDate: new Date("2026-10-23T00:00:00Z"),
        estimatedBudget: 1150.00,
        actualExpenses: 420.00,
        status: "PLANNING",
        visibility: "SHARED",
        notes: "Scenic road tour across Haridwar, Shimla, and Manali.",
        createdAt: new Date()
    }
]);

// 8. Trip Members
db.trip_members.insertMany([
    { tripId: trip1Id, userId: userId1, role: "OWNER", status: "ACTIVE", joinedAt: new Date() },
    { tripId: trip1Id, userId: userId2, role: "EDITOR", status: "ACTIVE", joinedAt: new Date() }
]);

print("=================================================");
print("SUCCESS: tripmate_db loaded with all travel packages!");
print("=================================================");
