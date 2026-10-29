package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.AppContent

@Entity(tableName = "app_content")
data class AppContentEntity(
    @PrimaryKey val id: Int = 0,
    val routesJson: String,
    val foodPlacesJson: String,
    val accommodationPlacesJson: String,
    val restaurantsJson: String?,
    val helplinesJson: String,
    val policeBoothsJson: String,
    val safetyGuidelinesJson: String,
    val announcementsJson: String,
    val calendarEventsJson: String,
    val updatedAt: Long,
    val source: String
)

fun AppContentEntity.toModel(converters: Converters): AppContent {
    return AppContent(
        routes = converters.routesFromJson(routesJson),
        foodPlaces = converters.foodFromJson(foodPlacesJson),
        accommodationPlaces = converters.staysFromJson(accommodationPlacesJson),
        restaurants = restaurantsJson?.let { converters.restaurantsFromJson(it) } ?: emptyList(),
        helplines = converters.helplinesFromJson(helplinesJson),
        policeBooths = converters.boothsFromJson(policeBoothsJson),
        safetyGuidelines = converters.guidelinesFromJson(safetyGuidelinesJson),
        announcements = converters.announcementsFromJson(announcementsJson),
        calendarEvents = converters.eventsFromJson(calendarEventsJson),
        updatedAt = updatedAt,
        source = source
    )
}

fun AppContent.toEntity(): AppContentEntity {
    val converters = Converters()
    return AppContentEntity(
        id = 0,
        routesJson = converters.routesToJson(routes),
        foodPlacesJson = converters.foodToJson(foodPlaces),
        accommodationPlacesJson = converters.staysToJson(accommodationPlaces),
        restaurantsJson = converters.restaurantsToJson(restaurants),
        helplinesJson = converters.helplinesToJson(helplines),
        policeBoothsJson = converters.boothsToJson(policeBooths),
        safetyGuidelinesJson = converters.guidelinesToJson(safetyGuidelines),
        announcementsJson = converters.announcementsToJson(announcements),
        calendarEventsJson = converters.eventsToJson(calendarEvents),
        updatedAt = updatedAt,
        source = source
    )
}
