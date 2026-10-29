package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.AccommodationPlace
import com.example.model.Announcement
import com.example.model.FoodPlace
import com.example.model.HelplineItem
import com.example.model.PandalRoute
import com.example.model.PoliceBooth
import com.example.model.PujaEvent
import com.example.model.RestaurantPlace
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val routeListType = Types.newParameterizedType(List::class.java, PandalRoute::class.java)
    private val foodListType = Types.newParameterizedType(List::class.java, FoodPlace::class.java)
    private val stayListType = Types.newParameterizedType(List::class.java, AccommodationPlace::class.java)
    private val restaurantListType = Types.newParameterizedType(List::class.java, RestaurantPlace::class.java)
    private val helplineListType = Types.newParameterizedType(List::class.java, HelplineItem::class.java)
    private val boothListType = Types.newParameterizedType(List::class.java, PoliceBooth::class.java)
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val announcementListType = Types.newParameterizedType(List::class.java, Announcement::class.java)
    private val eventListType = Types.newParameterizedType(List::class.java, PujaEvent::class.java)

    private val routeAdapter = moshi.adapter<List<PandalRoute>>(routeListType)
    private val foodAdapter = moshi.adapter<List<FoodPlace>>(foodListType)
    private val stayAdapter = moshi.adapter<List<AccommodationPlace>>(stayListType)
    private val restaurantAdapter = moshi.adapter<List<RestaurantPlace>>(restaurantListType)
    private val helplineAdapter = moshi.adapter<List<HelplineItem>>(helplineListType)
    private val boothAdapter = moshi.adapter<List<PoliceBooth>>(boothListType)
    private val stringAdapter = moshi.adapter<List<String>>(stringListType)
    private val announcementAdapter = moshi.adapter<List<Announcement>>(announcementListType)
    private val eventAdapter = moshi.adapter<List<PujaEvent>>(eventListType)

    @TypeConverter fun routesToJson(list: List<PandalRoute>): String = routeAdapter.toJson(list)
    @TypeConverter fun routesFromJson(json: String): List<PandalRoute> = routeAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun foodToJson(list: List<FoodPlace>): String = foodAdapter.toJson(list)
    @TypeConverter fun foodFromJson(json: String): List<FoodPlace> = foodAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun staysToJson(list: List<AccommodationPlace>): String = stayAdapter.toJson(list)
    @TypeConverter fun staysFromJson(json: String): List<AccommodationPlace> = stayAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun restaurantsToJson(list: List<RestaurantPlace>): String = restaurantAdapter.toJson(list)
    @TypeConverter fun restaurantsFromJson(json: String): List<RestaurantPlace> = restaurantAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun helplinesToJson(list: List<HelplineItem>): String = helplineAdapter.toJson(list)
    @TypeConverter fun helplinesFromJson(json: String): List<HelplineItem> = helplineAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun boothsToJson(list: List<PoliceBooth>): String = boothAdapter.toJson(list)
    @TypeConverter fun boothsFromJson(json: String): List<PoliceBooth> = boothAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun guidelinesToJson(list: List<String>): String = stringAdapter.toJson(list)
    @TypeConverter fun guidelinesFromJson(json: String): List<String> = stringAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun announcementsToJson(list: List<Announcement>): String = announcementAdapter.toJson(list)
    @TypeConverter fun announcementsFromJson(json: String): List<Announcement> = announcementAdapter.fromJson(json) ?: emptyList()

    @TypeConverter fun eventsToJson(list: List<PujaEvent>): String = eventAdapter.toJson(list)
    @TypeConverter fun eventsFromJson(json: String): List<PujaEvent> = eventAdapter.fromJson(json) ?: emptyList()
}
