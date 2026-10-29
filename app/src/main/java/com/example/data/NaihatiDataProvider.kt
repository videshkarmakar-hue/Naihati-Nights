package com.example.data

import com.example.model.AccommodationPlace
import com.example.model.Announcement
import com.example.model.AppContent
import com.example.model.CorridorFacility
import com.example.model.FoodPlace
import com.example.model.HelplineItem
import com.example.model.PandalRoute
import com.example.model.PandalStop
import com.example.model.PoliceBooth
import com.example.model.PujaEvent
import com.example.model.RestaurantPlace
import com.example.model.RouteFacilities
import com.example.model.RouteNote
import com.example.model.RouteStopGroup

object NaihatiDataProvider {

    val routes = listOf(
        PandalRoute(
            id = "route_1",
            routeNumber = "01",
            title = "Exit of Platform 1 — Naihati Ferryghat Road",
            subtitle = "Walking from Naihati Station along Aurobindo Road straight towards Ferryghat",
            categoryBadge = "Boro Maa Special",
            pandals = listOf(
                "Ramukali",
                "Bechakali",
                "Ganjakali",
                "Hawkerskali",
                "Boro Kali (Boro Maa)",
                "Pravatkali",
                "Kadamkali"
            ),
            crowdStatus = "High Devotional Flow",
            distance = "900m direct walkway",
            badgeStyle = "rose",
            badgeIcon = "flame",
            routeMapQuery = "Naihati Station to Ferryghat Kali Puja",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_1_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_1_stop_1",
                            name = "Ramukali",
                            bengaliName = "রামু কালী",
                            description = "Historic idol near station exit, Station Road",
                            mapQuery = "Naihati Ramu Kali Puja"
                        ),
                        PandalStop(
                            id = "route_1_stop_2",
                            name = "Bechakali",
                            bengaliName = "বেচা কালী",
                            description = "Traditional heritage idol on Aurobindo Road",
                            mapQuery = "Naihati Becha Kali Puja"
                        ),
                        PandalStop(
                            id = "route_1_stop_3",
                            name = "Ganjakali",
                            bengaliName = "গাঁজা কালী",
                            description = "Devotional gathering along Aurobindo Road",
                            mapQuery = "Naihati Ganja Kali Puja"
                        ),
                        PandalStop(
                            id = "route_1_stop_4",
                            name = "Hawkerskali",
                            bengaliName = "হকার্স কালী",
                            description = "Iconic bazaar association celebration",
                            mapQuery = "Naihati Hawkers Kali Puja"
                        ),
                        PandalStop(
                            id = "route_1_stop_5",
                            name = "Boro Kali (Boro Maa)",
                            bengaliName = "বড়কালী / বড়মা",
                            description = "Historic 21-ft Naihati Boro Maa Temple & Grand Pilgrimage Compound",
                            mapQuery = "Naihati Boro Maa Kali Mandir",
                            highlighted = true,
                            highlightAccent = "amber"
                        ),
                        PandalStop(
                            id = "route_1_stop_6",
                            name = "Pravatkali",
                            bengaliName = "প্রভাত কালী",
                            description = "Devotional deity before ferry terminus",
                            mapQuery = "Naihati Pravat Kali Puja"
                        ),
                        PandalStop(
                            id = "route_1_stop_7",
                            name = "Kadamkali",
                            bengaliName = "কদম কালী",
                            description = "Traditional riverfront deity at Ferryghat terminus",
                            mapQuery = "Naihati Kadam Kali Puja"
                        )
                    )
                )
            ),
            detourNote = RouteNote(
                label = "Special Alley Detour:",
                text = "Turn left into Nolini Bhattacharya Lane to visit \"Bot Sangha\" (বট সংঘ).",
                boldPhrases = listOf("Bot Sangha"),
                accent = "amber"
            ),
            facilities = RouteFacilities(
                headerSubtitle = "Station to Ferryghat",
                facilities = listOf(
                    CorridorFacility(
                        id = "route_1_food",
                        icon = "food",
                        name = "Hangla's & Annapurna Bhojanalaya",
                        description = "Station Road • 2 min walk • Bengali Thali & Tandoor",
                        mapQuery = "Restaurants near Naihati Station"
                    ),
                    CorridorFacility(
                        id = "route_1_stay",
                        icon = "stay",
                        name = "Hotel Royal Palace & Guest House",
                        description = "Near Platform 1 • 350m • AC Deluxe Rooms • 24/7 Desk",
                        mapQuery = "Hotels near Naihati Railway Station"
                    )
                ),
                delicaciesTitle = "Must-Try Delicacies",
                delicacies = listOf(
                    "Station Hot Hing Kachori",
                    "Naihati Malai Chamcham",
                    "Special Mutton Mughlai"
                )
            )
        ),
        PandalRoute(
            id = "route_2",
            routeNumber = "02",
            title = "From Boro Kali turn left to Shyam Road Link",
            subtitle = "Via Sanjib Chatterjee Road to RBC Road connection",
            categoryBadge = "Shyam Road Link",
            pandals = listOf(
                "Adi Maitri Sangha",
                "Balak Sangha on Sanjib Chatterjee Road",
                "Bharatmata Seba Samiti on R.B.C. Road"
            ),
            crowdStatus = "Moderate Density",
            distance = "1.1 km",
            badgeStyle = "neutral",
            badgeIcon = "route",
            routeMapQuery = "Naihati Shyam Road Kali Puja",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_2_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_2_stop_1",
                            name = "Adi Maitri Sangha",
                            bengaliName = "আদি মৈত্রী সংঘ",
                            description = "Primary gateway pandal of Shyam Road with dazzling lighting",
                            mapQuery = "Naihati Adi Maitri Sangha"
                        ),
                        PandalStop(
                            id = "route_2_stop_2",
                            name = "Balak Sangha on Sanjib Chatterjee Road",
                            bengaliName = "সঞ্জীব চ্যাটার্জি রোডের বালক সংঘ",
                            description = "Iconic puja in the literary hub neighborhood of Naihati",
                            mapQuery = "Naihati Balak Sangha Sanjeeb Chatterjee Road"
                        ),
                        PandalStop(
                            id = "route_2_stop_3",
                            name = "Bharatmata Seba Samiti on R.B.C. Road",
                            bengaliName = "আর.বি.সি রোডের ভারতমাতা সেবা সমিতি",
                            description = "Crucial intersection pandal connecting straight to R.B.C Road",
                            mapQuery = "Naihati Bharatmata Seba Samity"
                        )
                    )
                )
            ),
            directionNote = RouteNote(
                emoji = "🧭",
                label = "Direction Note:",
                text = "From Boro Kali, take a left towards Shyam Road, visit Adi Maitri Sangha, then Balak Sangha on Sanjib Chatterjee Road, and reach RBC Road to see Bharatmata Seba Samiti.",
                accent = "violet"
            ),
            facilities = RouteFacilities(
                headerSubtitle = "Shyam Rd & RBC Concourse",
                facilities = listOf(
                    CorridorFacility(
                        id = "route_2_food",
                        icon = "food",
                        name = "RBC Dine-In & Sanjib Cafe",
                        description = "RBC Road • 3 min walk • Chinese Gravy Chow & Moglai",
                        mapQuery = "Restaurants in RBC Road Naihati"
                    ),
                    CorridorFacility(
                        id = "route_2_stay",
                        icon = "stay",
                        name = "Shyam Residency & Tourist Lodge",
                        description = "Shyam Road Crossing • 400m • Clean AC & Non-AC",
                        mapQuery = "Lodge near RBC Road Naihati"
                    )
                )
            )
        ),
        PandalRoute(
            id = "route_3",
            routeNumber = "03",
            title = "Near \"Swapnabithi Park Road\" (স্বপ্নবীথির পার্ক রাস্তা)",
            subtitle = "Bankimchandra Memorial & Lichubagan Math mega clubs",
            categoryBadge = "Park Road",
            pandals = listOf(
                "Adi Bankim Sangha",
                "Akshay Smriti Sangha",
                "Bharat Sangha — Lichubagan Math",
                "Naba Matri Sangha — Lichubagan Math"
            ),
            crowdStatus = "Peak Illumination Zone",
            distance = "1.4 km",
            badgeStyle = "neutral",
            badgeIcon = "cloud",
            routeMapQuery = "Swapnabithi Park Naihati Kali Puja",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_3_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_3_stop_1",
                            name = "Adi Bankim Sangha",
                            bengaliName = "১. আদি বঙ্কিম সংঘ",
                            description = "Heritage artistic theme pandal at the park entrance",
                            mapQuery = "Naihati Adi Bankim Sangha"
                        ),
                        PandalStop(
                            id = "route_3_stop_2",
                            name = "Akshay Smriti Sangha",
                            bengaliName = "২. অক্ষয় স্মৃতি সংঘ",
                            description = "Renowned illumination design and aesthetic craftsmanship",
                            mapQuery = "Naihati Akshay Smriti Sangha"
                        ),
                        PandalStop(
                            id = "route_3_stop_3",
                            name = "Bharat Sangha — Lichubagan Math",
                            bengaliName = "৩. ভারত সংঘ - লিচুবাগান মাঠ",
                            description = "Massive open-ground setup with vibrant festive crowds",
                            mapQuery = "Naihati Bharat Sangha Lichubagan"
                        ),
                        PandalStop(
                            id = "route_3_stop_4",
                            name = "Naba Matri Sangha — Lichubagan Math",
                            bengaliName = "৪. নব মাতৃ সংঘ - লিচুবাগান মাঠ",
                            description = "Awe-inspiring deity artistry leading forward",
                            mapQuery = "Naihati Naba Matri Sangha"
                        )
                    )
                )
            ),
            directionNote = RouteNote(
                emoji = "🧭",
                label = "Direction Note:",
                text = "Following this road straight leads onto R.B.C Road.",
                accent = "violet"
            ),
            facilities = RouteFacilities(
                headerSubtitle = "Park Road & Lichubagan",
                facilities = listOf(
                    CorridorFacility(
                        id = "route_3_food",
                        icon = "food",
                        name = "Swapnabithi Garden Eatery & Kitchen",
                        description = "Near Park Gate • 1 min walk • Kolkata Dum Biryani",
                        mapQuery = "Swapnabithi Park Restaurants Naihati"
                    ),
                    CorridorFacility(
                        id = "route_3_stay",
                        icon = "stay",
                        name = "Lichubagan Heritage Homestay",
                        description = "Near Adi Bankim Math • 500m • Family Suites Available",
                        mapQuery = "Guest House Naihati Park Road"
                    )
                )
            )
        ),
        PandalRoute(
            id = "route_4",
            routeNumber = "04",
            title = "Jan Mohammad Ghat & Kanaidas Bairagi Sarani",
            subtitle = "Mahakali Tala & near St. Luke's School",
            categoryBadge = "Ganga Ghat",
            pandals = listOf(
                "Ekata Sangha",
                "Amra Sobai Club",
                "Sanghasree Club",
                "Lohaghat Park Association",
                "Mahakali Tala Puja",
                "Subhash Samiti — opposite St. Luke's School"
            ),
            crowdStatus = "Serene River Corridor",
            distance = "1.0 km",
            badgeStyle = "neutral",
            badgeIcon = "water",
            routeMapQuery = "Jan Mahammad Ghat Road Naihati",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_4_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_4_stop_1",
                            name = "Ekata Sangha",
                            bengaliName = "একতা সংঘ",
                            description = "Popular community celebration on the ghat approaches",
                            mapQuery = "Naihati Ekata Sangha"
                        ),
                        PandalStop(
                            id = "route_4_stop_2",
                            name = "Amra Sobai Club",
                            bengaliName = "আমরা সবাই ক্লাব",
                            description = "Leading to the right alley towards Jan Mohammad Ghat",
                            mapQuery = "Naihati Amra Sobai Club"
                        ),
                        PandalStop(
                            id = "route_4_stop_3",
                            name = "Sanghasree Club",
                            bengaliName = "সংঘশ্রী ক্লাব",
                            description = "Kanaidas Bairagi Sarani riverside puja compound",
                            mapQuery = "Naihati Sanghashree Club"
                        ),
                        PandalStop(
                            id = "route_4_stop_4",
                            name = "Lohaghat Park Association",
                            bengaliName = "লোহাঘাট পার্ক অ্যাসোসিয়েশন",
                            description = "Serene ambiance adjacent to the banks of the sacred Ganges",
                            mapQuery = "Naihati Lohaghat Park"
                        ),
                        PandalStop(
                            id = "route_4_stop_5",
                            name = "Mahakali Tala Puja",
                            bengaliName = "মহাকালী তলার পুজো",
                            description = "Straight along Jan Mohammad Ghat road",
                            mapQuery = "Naihati Mahakali Tala"
                        ),
                        PandalStop(
                            id = "route_4_stop_6",
                            name = "Subhash Samiti — opposite St. Luke's School",
                            bengaliName = "St. Luke স্কুলের উল্টো দিকে সুভাষ সমিতি",
                            description = "Heritage pandal opposite school grounds",
                            mapQuery = "Naihati Subhash Samiti St Luke"
                        )
                    )
                )
            ),
            directionNote = RouteNote(
                emoji = "📍",
                label = "Detailed Direction Guide:",
                text = "After Ekata Sangha, pass Amra Sobai Club and enter the right alley up to Jan Mohammad Ghat Road, proceed towards Kanaidas Bairagi Sarani to see Sanghasree & Lohaghat Park Association. Return along that path, continue along Jan Mohammad Ghat straight to Mahakali Tala, then proceed opposite St. Luke's School to visit Subhash Samiti, return, and walk straight along RBC Road turning left.",
                labelOnOwnLine = true,
                accent = "none"
            )
        ),
        PandalRoute(
            id = "route_5",
            routeNumber = "05",
            title = "After crossing \"GREEN VIEW Nursing Home\"",
            subtitle = "Towards Barda Bridge & Mirabagan",
            categoryBadge = "Green View",
            pandals = listOf(
                "Bibadi Sangha",
                "Yubak Sangha",
                "Gopal Smriti Sangha"
            ),
            crowdStatus = "Steady Movement",
            distance = "1.3 km",
            badgeStyle = "neutral",
            badgeIcon = "eye",
            routeMapQuery = "Green View Nursing Home Naihati",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_5_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_5_stop_1",
                            name = "Bibadi Sangha",
                            bengaliName = "১. বিবাদী সংঘ",
                            description = "Artistic pandal nestled in a refreshing lush green ambiance",
                            mapQuery = "Naihati Bibadi Sangha"
                        ),
                        PandalStop(
                            id = "route_5_stop_2",
                            name = "Yubak Sangha",
                            bengaliName = "২. যুবক সংঘ",
                            description = "Grand festive stage and community pandal",
                            mapQuery = "Naihati Yubak Sangha"
                        ),
                        PandalStop(
                            id = "route_5_stop_3",
                            name = "Gopal Smriti Sangha",
                            bengaliName = "৩. গোপাল স্মৃতি সংঘ",
                            description = "Traditional lighting and cultural displays",
                            mapQuery = "Naihati Gopal Smriti Sangha"
                        )
                    )
                )
            ),
            directionNote = RouteNote(
                label = "Direction & Milestone:",
                text = "Advancing further, Barda Bridge will appear on the right side (going straight leads to Mirabagan Puja).",
                accent = "violet"
            )
        ),
        PandalRoute(
            id = "route_6",
            routeNumber = "06",
            title = "Crossing Barda Bridge, Blue Star Bridge & Power House More (Turn Left)",
            subtitle = "BDO Office More, Bijoynagar Kathgola & Palli Area",
            categoryBadge = "29 Haat Kali",
            pandals = listOf(
                "United Club — Under Barda Bridge",
                "7 No. Bijoynagar Kathgola Adhibasibrinda",
                "Agragami Sangha",
                "29 Haat Kali — BDO Office Puja",
                "Bidhan Sangha",
                "Tarama Sangha",
                "Boys Club — Inside the lane",
                "Amra Kobhai Sangha",
                "New Star",
                "Amra Sobai Club — Deshbandhupally"
            ),
            crowdStatus = "Major Attraction",
            distance = "1.7 km",
            badgeStyle = "rose",
            badgeIcon = "temple",
            routeMapQuery = "Power House More Naihati Kali Puja",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_6_barda",
                    title = "Barda & Blue Star Bridge Region:",
                    stops = listOf(
                        PandalStop(
                            id = "route_6_stop_1",
                            name = "United Club — Under Barda Bridge",
                            bengaliName = "ইউনাইটেড ক্লাব - বরোদা ব্রিজের নীচে",
                            mapQuery = "Naihati United Club Barda Bridge"
                        ),
                        PandalStop(
                            id = "route_6_stop_2",
                            name = "7 No. Bijoynagar Kathgola Adhibasibrinda",
                            bengaliName = "৭ নং বিজয়নগর কাঠগোলা অধিবাসীবৃন্দ",
                            mapQuery = "7 No Bijoynagar Kathgola Naihati"
                        ),
                        PandalStop(
                            id = "route_6_stop_3",
                            name = "Agragami Sangha",
                            bengaliName = "অগ্রগামী সংঘ",
                            mapQuery = "Naihati Agragami Sangha"
                        )
                    )
                ),
                RouteStopGroup(
                    id = "route_6_power_house",
                    title = "Near Power House More (Turn Left):",
                    stops = listOf(
                        PandalStop(
                            id = "route_6_stop_4",
                            name = "29 Haat Kali — BDO Office Puja",
                            bengaliName = "২৯ হাত কালী - বি ডি ও অফিসের পুজো",
                            mapQuery = "Naihati 29 Haat Kali",
                            highlighted = true,
                            highlightAccent = "rose"
                        ),
                        PandalStop(
                            id = "route_6_stop_5",
                            name = "Bidhan Sangha",
                            bengaliName = "বিধান সংঘ",
                            mapQuery = "Naihati Bidhan Sangha"
                        ),
                        PandalStop(
                            id = "route_6_stop_6",
                            name = "Tarama Sangha",
                            bengaliName = "তারামা সংঘ",
                            mapQuery = "Naihati Tarama Sangha"
                        ),
                        PandalStop(
                            id = "route_6_stop_7",
                            name = "Boys Club — Inside the lane",
                            bengaliName = "বয়েজ ক্লাব - গলির মধ্যে",
                            mapQuery = "Naihati Boys Club"
                        ),
                        PandalStop(
                            id = "route_6_stop_8",
                            name = "Amra Kobhai Sangha",
                            bengaliName = "আমরা কভাই সংঘ",
                            mapQuery = "Naihati Amra Kobhai Sangha"
                        ),
                        PandalStop(
                            id = "route_6_stop_9",
                            name = "New Star",
                            bengaliName = "নিউ স্টার",
                            mapQuery = "Naihati New Star Club"
                        ),
                        PandalStop(
                            id = "route_6_stop_10",
                            name = "Amra Sobai Club — Deshbandhupally",
                            bengaliName = "আমরা সবাই ক্লাব - দেশবন্ধুপল্লী",
                            mapQuery = "Naihati Amra Sobai Deshbandhupally"
                        )
                    )
                )
            )
        ),
        PandalRoute(
            id = "route_7",
            routeNumber = "07",
            title = "Right Side of \"Power House More\"",
            subtitle = "Anandabazar Railmath, Hawkers Union & Dishari",
            categoryBadge = "Railway & Field",
            pandals = listOf(
                "Swamiji Sangha",
                "Amra Kajan Club — Kanai's Puja",
                "Hawkers Union",
                "Railway Colony Puja",
                "Anandabazar Railmath — Dishari"
            ),
            crowdStatus = "Spacious Festival Ground",
            distance = "1.5 km",
            badgeStyle = "neutral",
            badgeIcon = "rail",
            routeMapQuery = "Naihati Railway Colony Kali Puja",
            stopGroups = listOf(
                RouteStopGroup(
                    id = "route_7_stops",
                    stops = listOf(
                        PandalStop(
                            id = "route_7_stop_1",
                            name = "Swamiji Sangha",
                            bengaliName = "স্বামীজি সংঘ",
                            mapQuery = "Naihati Swamiji Sangha"
                        ),
                        PandalStop(
                            id = "route_7_stop_2",
                            name = "Amra Kajan Club — Kanai's Puja",
                            bengaliName = "আমরা কজন ক্লাব - কানাই এর পুজো",
                            mapQuery = "Naihati Amra Kajan Club"
                        ),
                        PandalStop(
                            id = "route_7_stop_3",
                            name = "Hawkers Union",
                            bengaliName = "হকার্স ইউনিয়ন",
                            mapQuery = "Naihati Hawkers Union Kali Puja"
                        ),
                        PandalStop(
                            id = "route_7_stop_4",
                            name = "Railway Colony Puja",
                            bengaliName = "রেলওয়ে কলোনীর পুজো",
                            mapQuery = "Naihati Railway Colony Kali Puja"
                        ),
                        PandalStop(
                            id = "route_7_stop_5",
                            name = "Anandabazar Railmath — Dishari",
                            bengaliName = "আনন্দবাজার রেলমাঠ - দিশারী",
                            secondaryLabel = "Grand Carnival & Fair Grounds",
                            mapQuery = "Naihati Dishari Anandabazar Rail Field"
                        )
                    )
                )
            ),
            directionNote = RouteNote(
                label = "Alley Connection Note:",
                text = "From Tarama Sangha's road, you can access Abhijatri Club (অভিযাত্রী ক্লাব) navigating through the alleys reaching up to New Star; on that pathway you will find Samaj Kalyan Samiti (সমাজ কল্যাণ সমিতি).",
                boldPhrases = listOf("Abhijatri Club", "Samaj Kalyan Samiti"),
                labelOnOwnLine = true,
                accent = "violet"
            )
        )
    )

    val foodPlaces = listOf(
        FoodPlace(
            id = "food_1",
            name = "Station & Aurobindo Road Biryani & Snack Zone",
            description = "Dada Boudi style biryani, hot kachori, mughlai paratha, and premier tea stalls",
            locationDescription = "Walking distance from Naihati Junction Platform 1",
            tags = listOf("Chicken Biryani", "Naihati Sweets", "Egg Roll & Chowmein")
        ),
        FoodPlace(
            id = "food_2",
            name = "Shyam Road & RBC Road Cafe Hub",
            description = "Fast food joints, momos, chilled beverages & family dining destinations",
            locationDescription = "Adjacent to Shyam Road Crossing and RBC College link",
            tags = listOf("Steamed / Fried Momo", "Bengali Thali", "Ice Cream Parlor")
        ),
        FoodPlace(
            id = "food_3",
            name = "Power House & Bijoynagar Food Corner",
            description = "Late night tea stalls, crispy phuchka & pandal hopping snacks refreshments",
            locationDescription = "Power House More Junction & Bijoynagar Kathgola",
            tags = listOf("Crispy Phuchka", "Special Chai", "Moglai Paratha")
        )
    )

    val accommodationPlaces = listOf(
        AccommodationPlace(
            id = "acc_1",
            name = "Hotel Royal Palace & AC Deluxe Suites",
            locationDescription = "Station Road, Naihati (Near Platform 1) • 350m • 24/7 Room Service & Security",
            distance = "350m from Platform 1",
            features = "AC Deluxe, Power Backup, Safe Luggage Storage",
            priceRange = "₹1,400 - ₹2,800/night",
            phone = "+91 98300 24150"
        ),
        AccommodationPlace(
            id = "acc_2",
            name = "Utsav Inn & Banquet Residence",
            locationDescription = "BDO Office More, Bijoynagar • 450m from 29 Haat Kali • Parking & In-House Kitchen",
            distance = "450m from 29 Haat Kali",
            features = "Free Wi-Fi, Banquet Dining, Ample Parking",
            priceRange = "₹1,200 - ₹2,400/night",
            phone = "+91 98312 88420"
        )
    )

    val restaurants = listOf(
        RestaurantPlace(
            id = "rest_1",
            name = "Dada Boudi Biryani",
            cuisine = "Biryani & Mughlai",
            locationDescription = "Station Road, near Platform 1 Exit (180m)",
            dishes = listOf("Special Mutton Biryani", "Chicken Chaap", "Shahi Firni"),
            phone = "+91 98300 00000",
            mapQuery = "Dada Boudi Restaurant Naihati",
            categories = listOf("Biryani & Mughlai"),
            isLateNight = true
        ),
        RestaurantPlace(
            id = "rest_2",
            name = "Bijoynagar Bhojohori Hindu Hotel",
            cuisine = "Traditional Bengali Thali",
            locationDescription = "Bijoynagar Crossing, near 29 Haat Kali (Route 6)",
            dishes = listOf("Ilish Bhapa", "Kosha Mangsho & Luchi", "Basanti Pulao", "Katla Macher Kalia"),
            phone = "+91 98310 00000",
            mapQuery = "Bijoynagar Food Plaza Naihati",
            categories = listOf("Bengali Thali")
        ),
        RestaurantPlace(
            id = "rest_3",
            name = "Shyam Road Cafe & Rolls",
            cuisine = "Cafes & Snacks",
            locationDescription = "Shyam Road Concourse near Adi Maitri Sangha (Route 2)",
            dishes = listOf("Double Egg Mutton Roll", "Chicken Bharta", "Steamed Momos", "Cold Coffee"),
            phone = "+91 98312 00000",
            mapQuery = "Restaurants Shyam Road Naihati",
            categories = listOf("Cafes & Snacks", "Street Food")
        ),
        RestaurantPlace(
            id = "rest_4",
            name = "Aurobindo Road Food Street",
            cuisine = "Street Food & Snacks",
            locationDescription = "Aurobindo Road, between Boro Kali Temple & Station",
            dishes = listOf("Gondhoraj Phuchka", "Moglai Paratha", "Matir Bharer Malai Chai", "Hing Kachori"),
            phone = "+91 98313 00000",
            mapQuery = "Aurobindo Road Street Food Naihati",
            categories = listOf("Street Food"),
            isLateNight = true
        ),
        RestaurantPlace(
            id = "rest_5",
            name = "Naihati Mishti Mahal & Dairy",
            cuisine = "Sweets & Desserts",
            locationDescription = "Near Kadamtala & Ferryghat Road terminus",
            dishes = listOf("Naihati Special Chamcham", "Nolen Gur Sandesh", "Kesar Rabri", "Rasmalai"),
            phone = "+91 98314 00000",
            mapQuery = "Naihati Sweet Shops Ferryghat Road",
            categories = listOf("Sweets & Desserts")
        ),
        RestaurantPlace(
            id = "rest_6",
            name = "Royal Treat Multi-Cuisine",
            cuisine = "North Indian & Chinese",
            locationDescription = "Station Road Crossing, close to Hotel Royal Palace",
            dishes = listOf("Butter Naan & Paneer Butter", "Murgh Reshmi Kebab", "Chilli Chicken"),
            phone = "+91 98315 00000",
            mapQuery = "Restaurants near Naihati Station",
            categories = listOf("Biryani & Mughlai"),
            isLateNight = true
        ),
        RestaurantPlace(
            id = "rest_7",
            name = "Sagar Fast Food Corner",
            cuisine = "Indo-Chinese & Chowmein",
            locationDescription = "Near Mirabagan & Barda Bridge approach (Route 5 & 6)",
            dishes = listOf("Hakka Chowmein", "Crispy Chilli Babycorn", "Egg Chicken Roll"),
            phone = "+91 98316 00000",
            mapQuery = "Fast Food Barda Bridge Naihati",
            categories = listOf("Street Food", "Cafes & Snacks"),
            isLateNight = true
        ),
        RestaurantPlace(
            id = "rest_8",
            name = "Boro Maa Bhog & Annapurna Canteen",
            cuisine = "Satvik Bhog & Khichuri",
            locationDescription = "Boro Kali Mandir Pilgrimage Complex, Aurobindo Road",
            dishes = listOf("Bhog Khichuri & Labra", "Beguni & Chutney", "Payesh / Kheer", "Sacred Prasadam"),
            phone = "+91 98317 00000",
            mapQuery = "Naihati Boro Maa Kali Mandir",
            categories = listOf("Bengali Thali")
        )
    )

    val helplines = listOf(
        HelplineItem(
            id = "help_police",
            title = "Naihati Police Station Control Room",
            category = "police",
            subtitleLabel = "EMERGENCY HOTLINE",
            numbers = "033 2581 2223 / 100",
            actionLabel = "Call Now",
            dialNumber = "03325812223"
        ),
        HelplineItem(
            id = "help_boromaa",
            title = "Boro Maa Trust Emergency Service",
            category = "general",
            subtitleLabel = "ASSISTANCE HELPLINE",
            numbers = "033 2581 0000",
            actionLabel = "Call Desk",
            dialNumber = "03325810000"
        ),
        HelplineItem(
            id = "help_hospital",
            title = "Naihati State General Hospital",
            category = "medical",
            subtitleLabel = "HOSPITAL PHONE",
            numbers = "033 2581 2145",
            actionLabel = "Call Hospital",
            dialNumber = "03325812145"
        ),
        HelplineItem(
            id = "help_ambulance",
            title = "Ambulance SOS Emergency",
            category = "medical",
            subtitleLabel = "Ambulance SOS",
            numbers = "102 / 108",
            actionLabel = "Call Ambulance",
            dialNumber = "102"
        ),
        HelplineItem(
            id = "help_fire",
            title = "Naihati Fire Station",
            category = "police",
            subtitleLabel = "HOTLINE NUMBER",
            numbers = "101 / 033 2581 2222",
            actionLabel = "Call Fire Station",
            dialNumber = "101"
        ),
        HelplineItem(
            id = "help_municipality",
            title = "Naihati Municipality Control Room",
            category = "police",
            subtitleLabel = "CONTROL ROOM PHONE",
            numbers = "033 2581 2098",
            actionLabel = "Call Desk",
            dialNumber = "03325812098"
        ),
        HelplineItem(
            id = "help_women",
            title = "Women Helpline",
            category = "police",
            subtitleLabel = "QUICK ASSIST",
            numbers = "1091",
            actionLabel = "Call Now",
            dialNumber = "1091"
        ),
        HelplineItem(
            id = "help_child",
            title = "Child Assistance",
            category = "general",
            subtitleLabel = "HELPLINE 24X7",
            numbers = "1098",
            actionLabel = "Call Now",
            dialNumber = "1098"
        ),
        HelplineItem(
            id = "help_traffic",
            title = "Traffic Control Helpline",
            category = "police",
            subtitleLabel = "PUJA TRAFFIC SQUAD",
            numbers = "1073 / 033 2592 0000",
            actionLabel = "Call Desk",
            dialNumber = "1073"
        )
    )

    val policeBooths = listOf(
        PoliceBooth(1, "Naihati Station Platform 1 & Exit Booth", "Platform 1 Gate Concourse"),
        PoliceBooth(2, "Ferryghat More & River Ghat Booth", "Aurobindo Road End"),
        PoliceBooth(3, "Power House More & Bijoynagar Cross Point", "Major Traffic Intersection")
    )

    val safetyGuidelines = listOf(
        "Keep a slip of paper with guardian phone numbers in children's pockets.",
        "Avoid excessive pushing and strictly follow the designated one-way route guidelines.",
        "Report any suspicious unattended items immediately to the police booth.",
        "Contact nearest volunteer camp or call 102 in case of medical emergency."
    )

    val announcements = listOf(
        Announcement(
            id = "ann_1",
            title = "Boro Maa Darshan Open",
            message = "Boro Maa tala darshan is open. Queue currently moves in approximately 18 minutes.",
            severity = "event",
            routeId = "route_1",
            timestamp = "8 min ago"
        ),
        Announcement(
            id = "ann_2",
            title = "Route 6 Diversion",
            message = "Barda Bridge north approach is one-way after 9 PM. Use the Blue Star Bridge detour.",
            severity = "traffic",
            routeId = "route_6",
            timestamp = "12 min ago"
        ),
        Announcement(
            id = "ann_3",
            title = "Ferryghat Ganga Arati",
            message = "Evening Ganga Arati begins at 6:45 PM at the Ferryghat riverfront pavilion.",
            severity = "info",
            routeId = "route_1",
            timestamp = "25 min ago"
        ),
        Announcement(
            id = "ann_4",
            title = "Medical Camp Active",
            message = "Volunteer medical camp with drinking water is active near 29 Haat Kali.",
            severity = "info",
            routeId = "route_6",
            timestamp = "40 min ago"
        )
    )

    val calendarEvents = listOf(
        PujaEvent(
            id = "event_1",
            title = "Boro Maa Kalash Sthapan",
            dateLabel = "Sat, 24 Oct",
            timeLabel = "5:30 AM",
            venue = "Naihati Boro Maa Tala",
            description = "Vedic kalash sthapan and morning sankirtan to begin the 2026 Kali Puja.",
            isHighlight = true
        ),
        PujaEvent(
            id = "event_2",
            title = "Sandhi Puja & 108 Diya Arati",
            dateLabel = "Sat, 24 Oct",
            timeLabel = "10:40 PM",
            venue = "Aurobindo Road Sarbajanin Prangan",
            description = "Sandhi puja with 108 lamps, dhunuchi naach and live dhak ensemble.",
            isHighlight = true
        ),
        PujaEvent(
            id = "event_3",
            title = "Ganga Arati at Ferryghat",
            dateLabel = "Sat, 24 Oct",
            timeLabel = "6:45 PM",
            venue = "Ferryghat Riverfront Pavilion",
            description = "Riverfront arati with illuminated boats and devotional choir.",
            isHighlight = false
        ),
        PujaEvent(
            id = "event_4",
            title = "29 Haat Boro Kali Darshan",
            dateLabel = "Sun, 25 Oct",
            timeLabel = "4:00 AM",
            venue = "29 Haat Boro Kali Mandap",
            description = "Pre-dawn darshan window for the monumental Kali idol before peak crowd.",
            isHighlight = true
        ),
        PujaEvent(
            id = "event_5",
            title = "Sindoor Khela",
            dateLabel = "Sun, 25 Oct",
            timeLabel = "3:00 PM",
            venue = "Shyam Road Jubak Sangha",
            description = "Traditional sindoor khela followed by darpan visarjan procession.",
            isHighlight = false
        )
    )

    fun content(): AppContent = AppContent(
        routes = routes,
        foodPlaces = foodPlaces,
        accommodationPlaces = accommodationPlaces,
        restaurants = restaurants,
        helplines = helplines,
        policeBooths = policeBooths,
        safetyGuidelines = safetyGuidelines,
        announcements = announcements,
        calendarEvents = calendarEvents,
        updatedAt = 0L,
        source = "bundled"
    )
}
