package com.example.model



data class PandalRoute(
    val id: String,
    val routeNumber: String,
    val title: String,
    val subtitle: String,
    val categoryBadge: String,
    val pandals: List<String>,
    val crowdStatus: String = "Moderate Flow",
    val distance: String = "1.2 km",
    /** "rose" or "neutral" — drives the category pill styling. */
    val badgeStyle: String = "neutral",
    /** flame, route, cloud, water, eye, temple, rail. */
    val badgeIcon: String = "route",
    val routeMapQuery: String = "",
    val stopGroups: List<RouteStopGroup> = emptyList(),
    val detourNote: RouteNote? = null,
    val directionNote: RouteNote? = null,
    val facilities: RouteFacilities? = null
)

data class PandalStop(
    val id: String,
    val name: String,
    val mapQuery: String,
    val bengaliName: String = "",
    val description: String = "",
    val secondaryLabel: String = "",
    val highlighted: Boolean = false,
    /** amber (Boro Maa) or rose (29 Haat Kali). */
    val highlightAccent: String = "amber"
)

data class RouteStopGroup(
    val id: String,
    val title: String = "",
    val stops: List<PandalStop> = emptyList()
)

data class RouteNote(
    val text: String,
    val emoji: String = "",
    val label: String = "",
    val boldPhrases: List<String> = emptyList(),
    val labelOnOwnLine: Boolean = false,
    /** amber, violet or none. */
    val accent: String = "violet"
)

data class CorridorFacility(
    val id: String,
    /** food or stay. */
    val icon: String,
    val name: String,
    val description: String,
    val mapQuery: String
)

data class RouteFacilities(
    val headerSubtitle: String = "",
    val facilities: List<CorridorFacility> = emptyList(),
    val delicaciesTitle: String = "",
    val delicacies: List<String> = emptyList()
)

data class FoodPlace(
    val id: String,
    val name: String,
    val description: String,
    val locationDescription: String,
    val tags: List<String>,
    val openHours: String = "24 Hours (Puja Days)"
)

data class AccommodationPlace(
    val id: String,
    val name: String,
    val locationDescription: String,
    val distance: String,
    val features: String,
    val priceRange: String,
    val phone: String
)

data class RestaurantPlace(
    val id: String,
    val name: String,
    val cuisine: String,
    val locationDescription: String,
    val dishes: List<String>,
    val phone: String,
    val mapQuery: String,
    val categories: List<String> = emptyList(),
    val isLateNight: Boolean = false
)

data class HelplineItem(
    val id: String,
    val title: String,
    val category: String, // "police", "medical", "general"
    val subtitleLabel: String,
    val numbers: String,
    val actionLabel: String = "Call Now",
    val dialNumber: String
)

data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val severity: String = "info", // "info", "alert", "traffic", "event"
    val routeId: String? = null,
    val timestamp: String = "Just now"
)

data class PujaEvent(
    val id: String,
    val title: String,
    val dateLabel: String,
    val timeLabel: String,
    val venue: String,
    val description: String,
    val isHighlight: Boolean = false
)

/**
 * The complete content bundle rendered by the app. The same shape is used for the bundled
 * offline snapshot, the Room cache and the remote API response.
 */
data class AppContent(
    val routes: List<PandalRoute> = emptyList(),
    val foodPlaces: List<FoodPlace> = emptyList(),
    val accommodationPlaces: List<AccommodationPlace> = emptyList(),
    val restaurants: List<RestaurantPlace> = emptyList(),
    val helplines: List<HelplineItem> = emptyList(),
    val policeBooths: List<PoliceBooth> = emptyList(),
    val safetyGuidelines: List<String> = emptyList(),
    val announcements: List<Announcement> = emptyList(),
    val calendarEvents: List<PujaEvent> = emptyList(),
    val updatedAt: Long = 0L,
    val source: String = "bundled"
)

data class PoliceBooth(
    val number: Int,
    val name: String,
    val landmark: String
)

/**
 * Naihati Kali Puja 2026 Theme Pandels — the 13-card list rendered by the Theme Pandel screen
 * and opened from the "Theme Pandel" slot in the bottom navigation bar and sidebar.
 */
object ThemePandelData {
    val items = listOf(
        ThemePandel(
            id = "theme_01",
            edition = "66th Year",
            committee = "Naihati Swamiji Sangha",
            committeeBengali = "নৈহাটি স্বামীজি সংঘ",
            pandal = "Ayodhyar Ram Mandir",
            pandalBengali = "অযোধ্যার রামমন্দির",
            deity = "Ayodhyar Ram Mandir"
        ),
        ThemePandel(
            id = "theme_02",
            edition = "66th Year",
            committee = "Naihati Loharghat Park Association",
            committeeBengali = "নৈহাটি লোহারঘাট পার্ক অ্যাসোসিয়েশন",
            pandal = "Dharmakshetre Mahashakti",
            pandalBengali = "ধর্মক্ষেত্রে মহাশক্তি",
            deity = "Dharmakshetre Mahashakti"
        ),
        ThemePandel(
            id = "theme_03",
            edition = "35th Year",
            committee = "Naihati Indira Nagar Yuva Samaj Club",
            committeeBengali = "নৈহাটির ইন্দিরা নগর যুব সমাজ ক্লাব",
            pandal = "Ashareeri Jibanta Bhoot",
            pandalBengali = "অশরীরী জীবন্ত ভূত",
            deity = "Ashareeri Jibanta Bhoot"
        ),
        ThemePandel(
            id = "theme_04",
            edition = "76th Year",
            committee = "Naihati 7 No. Bijoynagar Kathgola",
            committeeBengali = "নৈহাটি ৭ নং বিজয় নগর কাঠগোলা",
            pandal = "Maharanir Darbare",
            pandalBengali = "মহারানীর দরবারে",
            deity = "Maharanir Darbare"
        ),
        ThemePandel(
            id = "theme_05",
            edition = "78th Year",
            committee = "Naihati Deshbandhu Pally \"Amra Sabai Club\"",
            committeeBengali = "নৈহাটি দেশবন্ধু পল্লী \"আমরা সবাই ক্লাব\"",
            pandal = "Aranyer Ashru",
            pandalBengali = "অরণ্যের অশ্রু",
            locationNote = "Near Netralok Eye Hospital",
            locationNoteBengali = "নেত্রলোক চক্ষু হাসপাতাল নিকট",
            deity = "Aranyer Ashru"
        ),
        ThemePandel(
            id = "theme_06",
            edition = "2nd Year",
            committee = "Naihati Deulpara Sukanta Sarani \"Saraswati Sangha\"",
            committeeBengali = "নৈহাটি দেউলপাড়া সুকান্ত সরনী সংলগ্ন \"সরস্বতী সংঘ\"",
            pandal = "Raktim Shyamar Aradhana",
            pandalBengali = "রক্তিম শ্যামার আরাধনা",
            deity = "Raktim Shyamar Aradhana"
        ),
        ThemePandel(
            id = "theme_07",
            edition = "Kali Puja 2026",
            committee = "Naihati Narkel Bagan Kali Puja Committee",
            committeeBengali = "নৈহাটি নারকেল বাগান কালীপূজা কমিটি",
            pandal = "Gram Bangla",
            pandalBengali = "গ্রাম বাংলা",
            deity = "Gram Bangla"
        ),
        ThemePandel(
            id = "theme_08",
            edition = "58th Year",
            committee = "Naihati Abhijatri Club",
            committeeBengali = "নৈহাটি অভিযাত্রী ক্লাব",
            pandal = "Kedarnath Mandir",
            pandalBengali = "কেদারনাথ মন্দির",
            deity = "Kedarnath Mandir"
        ),
        ThemePandel(
            id = "theme_09",
            edition = "62nd Year",
            committee = "Naihati Gopal Smriti Sangha",
            committeeBengali = "নৈহাটি গোপাল স্মৃতি সংঘ",
            pandal = "Wooden Mandap inspired by Nepal's Buddhist Temple",
            pandalBengali = "নেপালের বৌদ্ধ মন্দিরের আদলে বিশেষ আকর্ষণ সম্পূর্ণ 'কাঠের তৈরী মণ্ডপ'",
            deity = "Wooden Mandap"
        ),
        ThemePandel(
            id = "theme_10",
            edition = "29th Year",
            committee = "Naihati Parar Sabai Club",
            committeeBengali = "নৈহাটি পাড়ার সবাই ক্লাব",
            pandal = "Mahakal",
            pandalBengali = "মহাকাল",
            locationNote = "Beside Barda Bridge",
            locationNoteBengali = "বরদা ব্রিজের পাশে",
            deity = "Mahakal"
        ),
        ThemePandel(
            id = "theme_11",
            edition = "91st Year",
            committee = "Naihati Katalpara",
            committeeBengali = "নৈহাটি কাটালপাড়া",
            pandal = "Mahanayak 100",
            pandalBengali = "মহানায়ক ১০০",
            deity = "Mahanayak 100"
        ),
        ThemePandel(
            id = "theme_12",
            edition = "50th Year",
            committee = "Naihati Samaj Kalyan Samiti",
            committeeBengali = "নৈহাটি সমাজ কল্যাণ সমিতি",
            pandal = "Baba - Ek Nirab Yoddha",
            pandalBengali = "বাবা - এক নীরব যোদ্ধা",
            deity = "Baba - Ek Nirab Yoddha"
        ),
        ThemePandel(
            id = "theme_13",
            edition = "Kali Puja 2026",
            committee = "Naihati Gouripur Mirabagan Math",
            committeeBengali = "নৈহাটি গৌরীপুর মীরাবাগান মাঠ",
            pandal = "Operation Sindoor",
            pandalBengali = "অপারেশন সিঁদুর",
            deity = "Operation Sindoor",
            isNew = true
        )
    )

    /** The latest addition, used to raise NEW badges in the bottom bar and sidebar. */
    val newest: ThemePandel? = items.firstOrNull { it.isNew }

    /** 1-based position of [item] in the published list, matching the number printed on its card. */
    fun displayNumber(item: ThemePandel): Int = items.indexOf(item) + 1
}

/**
 * Borma Pujo timetable for Naihati, surfaced through the "বড়মা পুজোর নির্ঘণ্ট" slot in the bottom
 * navigation bar and the sidebar panel.
 *
 * [PujoDay] groups the committee bulletin by day label and [PujoEvent] keeps the clock time and the
 * action copy verbatim so the night can be read out without translation; [PujoEvent.highlight] marks
 * the primary puja moment of the observance.
 */
data class PujoEvent(
    val id: String,
    val time: String,
    val detail: String? = null,
    val highlight: Boolean = false
)

data class PujoDay(
    val id: String,
    val dayLabel: String,
    val events: List<PujoEvent> = emptyList(),
    val note: String? = null
)

object BormaPujoScheduleData {
    const val kicker = "বার্ষিক উপলক্ষ"
    const val title = "বড়মা পুজোর নির্ঘণ্ট"
    const val subtitle = "নৈহাটি • কালী পূজা ২০২৬"
    const val timeZoneNote = "সব সময় ভারতীয় প্রমাণ সময় (IST) অনুযায়ী।"
    const val closingNote = "*(৬-১৪ নভেম্বর - সাধারণের জন্য বন্ধ মূল মন্দির দর্শন)"

    val days = listOf(
        PujoDay(
            id = "day_2_7_nov",
            dayLabel = "২ - ৭ নভেম্বর",
            events = listOf(
                PujoEvent(
                    id = "day_2_7_offering",
                    time = "২ - ৭ নভেম্বর",
                    detail = "জমা নেওয়া হবে মানতের সোনা-দানা ও প্রণামী"
                )
            )
        ),
        PujoDay(
            id = "day_7_8_nov",
            dayLabel = "৭ - ৮ নভেম্বর",
            note = "* (৮ নভেম্বর সন্ধে ছ'টার মধ্যে শেষ করতে হবে দন্ডি কাটা)",
            events = listOf(
                PujoEvent(
                    id = "day_7_8_abhisheka",
                    time = "৭ - ৮ নভেম্বর",
                    detail = "অলংকার পরানো ও দন্ডি কাটা"
                ),
                PujoEvent(
                    id = "day_7_8_prasad",
                    time = "৭ - ৮ নভেম্বর",
                    detail = "ফল ও মিষ্টির ডালি গ্রহণ"
                )
            )
        ),
        PujoDay(
            id = "day_8_8_nov",
            dayLabel = "৮ নভেম্বর",
            events = listOf(
                PujoEvent(
                    id = "day_8_8_abhisheka",
                    time = "বিকেল ৫:৩০",
                    detail = "মাকে বরণ"
                ),
                PujoEvent(
                    id = "day_8_8_puja_start",
                    time = "রাত ১২টা",
                    detail = "পুজো শুরু",
                    highlight = true
                ),
                PujoEvent(
                    id = "day_8_8_anjali",
                    time = "রাত ১:৩০",
                    detail = "অঞ্জলি"
                ),
                PujoEvent(
                    id = "day_8_8_prasad_bijoy",
                    time = "রাত ২:৩০",
                    detail = "ভোগ-প্রসাদ বিতরণ (৯-১১ নভেম্বর)"
                ),
                PujoEvent(
                    id = "day_8_8_prasad_dopura",
                    time = "দুপুর ২:৩০",
                    detail = "ভোগ বিতরণ"
                ),
                PujoEvent(
                    id = "day_8_8_prasad_raat",
                    time = "রাত ৯:৩০",
                    detail = "ভোগ বিতরণ"
                ),
                PujoEvent(
                    id = "day_8_8_pratishtha",
                    time = "রাত ১১:৩০",
                    detail = "ঘট প্রতিষ্ঠা"
                )
            )
        ),
        PujoDay(
            id = "day_12_12_nov",
            dayLabel = "১২ নভেম্বর",
            events = listOf(
                PujoEvent(
                    id = "day_12_12_bisaran",
                    time = "সকাল ৭.৩০",
                    detail = "বিসর্জনের পুজো"
                ),
                PujoEvent(
                    id = "day_12_12_maa_khula",
                    time = "সকাল ১০.৩০",
                    detail = "মায়ের অলংকার খোলা"
                ),
                PujoEvent(
                    id = "day_12_12_maa_baran",
                    time = "দুপুর ২টো",
                    detail = "মাকে বরণ"
                ),
                PujoEvent(
                    id = "day_12_12_niranjan",
                    time = "বিকেল ৪টে",
                    detail = "নিরঞ্জন শুরু"
                )
            )
        )
    )
}

/**
 * Special notice (বিশেষ ঘোষণা) surfaced from the sidebar for the annual Borma Pujo observance.
 *
 * [notices] carry the puja closure, darshan window and prasad windows the committee announced for
 * the year; each row reads on its own without the nishita highlight used by the full timetable.
 */
data class PujoNoticeItem(
    val id: String,
    val label: String,
    val detail: String
)

object BormaPujoNoticeData {
    const val title = "বিশেষ ঘোষণা"
    const val subtitle = "বড়মা পুজোর নির্ঘণ্ট • বার্ষিক উপলক্ষে"

    val notices = listOf(
        PujoNoticeItem(
            id = "annual_closure",
            label = "বার্ষিক পুজো উপলক্ষ",
            detail = "আগামী ০৬/১১/২৬ শুক্রবার থেকে ১৪/১১/২৬ শনিবার পর্যন্ত।"
        ),
        PujoNoticeItem(
            id = "mandir_closure",
            label = "মন্দির বন্ধ",
            detail = "বড়মা'র মন্দির সর্বসাধারণের জন্য পূর্ণ রূপে বন্ধ থাকবে। ১৫/১১/২৬ রবিবার থেকে মন্দিরে পূর্ব নির্ধারিত সময় অনুযায়ী পুজো দেওয়া যাবে।"
        ),
        PujoNoticeItem(
            id = "maternal_darshan",
            label = "মাতৃ দর্শন ও পুজো",
            detail = "(সকাল ৮টা থেকে দুপুর ২: ৩০মিঃ এবং বিকাল ৪টে থেকে রাত্রি ৮টা পর্যন্ত মাতৃ দর্শন ও পুজো দেওয়া যাবে)"
        ),
        PujoNoticeItem(
            id = "prasad_closure",
            label = "প্রসাদ গ্রহণ",
            detail = "বার্ষিক পুজো উপলক্ষে ৩১/১০/২৬ থেকে ১৬/১১/২৬ সোমবার পর্যন্ত মন্দিরে বসে প্রসাদ গ্রহণ বন্ধ থাকবে।"
        ),
        PujoNoticeItem(
            id = "prasad_resume",
            label = "পুনরায় প্রসাদ গ্রহণ",
            detail = "পুনরায় মন্দিরে বসে প্রসাদ গ্রহণ শুরু হবে ১৮/১১/২৬ বুধবার থেকে পূর্ব নির্ধারিত সময় অনুযায়ী।"
        )
    )

    const val closingNote = "সময় পরিবর্তন হলে কমিটি জানালে এখানে হালনাগাদ করা হবে।"
}

enum class NavigationMode(val label: String) {
    IN_APP("In-App Kali Puja Radar"),
    GOOGLE_MAPS("Deep-link to Google Maps"),
    APPLE_MAPS("Apple Maps Integration")
}

enum class InquiryType(val title: String, val subtitle: String) {
    ROUTE_ALERT("Route Alert", "Barricade, detour..."),
    PANDAL_UPDATE("Pandal Update", "Queue, timings"),
    SUGGESTION("Suggestion", "App feature ideas..."),
    EXPERIENCE("Experience", "General review...")
}

enum class UrgencyLevel(val label: String) {
    NORMAL("Normal"),
    IMPORTANT("Important"),
    URGENT_DIVERSION("Urgent Diversion")
}

data class AppSettings(
    // Parikrama & Navigation
    val queueEstimator: Boolean = true,
    val avoidCongestedAlleys: Boolean = true,
    val offlineCorridors: Boolean = true,
    val gpsHighPrecision: Boolean = true,

    // Map & Pandal Radar Filters
    val grandBudgetPandals: Boolean = true,
    val traditionalSabeki: Boolean = true,
    val chandannagarIllumination: Boolean = true,
    val showAmenitiesOverlay: Boolean = true,
    val navigationMode: NavigationMode = NavigationMode.IN_APP,

    // Display & Power Optimization
    val oledTrueBlack: Boolean = true,
    val nightGlareDimmer: Boolean = true,
    val motionReduction: Boolean = false,
    val hapticFeedback: Boolean = true,
    val textLegibilityScale: Int = 1, // 1: Standard (100%), 2: Medium (115%), 3: Large (130% - Night Hopping)

    // Safety & Municipal Alerts
    val thanaFlashBroadcasts: Boolean = true, // mandatory / protected
    val emergencySosBeacon: Boolean = true,
    val bhogAndWaterHubs: Boolean = true,
    val anonymousFootTraffic: Boolean = true,
    val communityReportingSync: Boolean = true,

    // Update channel
    /** Checks GitHub Releases on launch and shows the update prompt when a newer build exists. */
    val autoUpdateCheck: Boolean = true,

    /** Currently selected app theme; Indigo Night is the launch default. */
    val themeStyle: ThemeStyle = ThemeStyle.INDIGO_NIGHT
)

enum class ThemeStyle(
    val label: String,
    val description: String,
    val swatch: Long,
    val swatchContainer: Long,
    val textBg: Long = 0xFF050505L,
    val text: Long = 0xFFF4F4F5L,
    val textMuted: Long = 0xFF94A3B8L
) {
    REGAL_VIOLET("Regal Violet", "Signature obsidian & regal violet", 0xFF8257E5, 0xFF3A0CA3, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    SACRED_GOLD("Sacred Gold", "Warm gold on deep amber", 0xFFE5A93B, 0xFF5A3E08, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    KUMARTULI_TEAL("Kumartuli Teal", "Sea-glass teal & deep pine", 0xFF2DD4BF, 0xFF0B4F49, 0xFF04211FL, 0xFFCCFBE6L, 0xFF94A3B8),
    AZURE_SIGNAL("Azure Signal", "Crisp sky blue on deep ink", 0xFF38BDF8, 0xFF0C4A6E, 0xFF04202E, 0xFFB8E4FD, 0xFF94A3B8),
    DARK_RED("Dark Red", "Deep crimson on blood plum", 0xFFC8102E, 0xFF450A0A, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    FOREST_GREEN("Forest Green", "Deep forest green on pine ink", 0xFF16A34A, 0xFF052E16, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    INDIGO_NIGHT("Indigo Night", "Deep indigo on night plum", 0xFF6366F1, 0xFF1E1B4B, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    ROYAL_AMBER("Royal Amber", "Burnt amber on deep bronze", 0xFFD97706, 0xFF451A03, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8),
    PLUM_NOIR("Plum Noir", "Muted plum on ink plum", 0xFFA855F7, 0xFF3B1160, 0xFF050505, 0xFFF4F4F5, 0xFF94A3B8)
}

/**
 * Festival edition detail card shown in the Theme Pandel slot.
 *
 * [isNew] marks an entry added after the first release of the guide so the bottom bar and sidebar
 * can advertise it without duplicating the Bengali copy.
 */
data class ThemePandel(
    val id: String = "",
    val edition: String,
    val committee: String,
    val committeeBengali: String = "",
    val pandal: String,
    val pandalBengali: String = "",
    val locationNote: String = "",
    val locationNoteBengali: String = "",
    val deity: String = "",
    val isNew: Boolean = false
)
