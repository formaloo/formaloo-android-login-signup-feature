package com.formaloo.boardauthentication.data.local.convertor

import androidx.room.TypeConverter
import com.formaloo.boardauthentication.data.model.*
import com.formaloo.boardauthentication.data.model.submitForm.RenderedData
import com.formaloo.boardauthentication.data.model.submitForm.Row
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap

class AuthConverters {


    @TypeConverter
    fun fromJsonObject(data: JsonObject?): String? {
        val type = object : TypeToken<JsonObject>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toJsonObject(json: String?): JsonObject? {
        val type = object : TypeToken<JsonObject>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromChoiceItem(data: ChoiceItem?): String? {
        val type = object : TypeToken<ChoiceItem>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toChoiceItem(json: String?): ChoiceItem? {
        val type = object : TypeToken<ChoiceItem>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromRenderedData(data: RenderedData?): String? {
        val type = object : TypeToken<RenderedData>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toRenderedData(json: String?): RenderedData? {
        val type = object : TypeToken<RenderedData>() {}.type
        return Gson().fromJson(json, type)
    }
    @TypeConverter
    fun fromRenderedDataMap(data:  HashMap<String, RenderedData>?): String? {
        val type = object : TypeToken< HashMap<String, RenderedData>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toRenderedDataMap(json: String?):  HashMap<String, RenderedData>? {
        val type = object : TypeToken< HashMap<String, RenderedData>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromRow(data: Row?): String? {
        val type = object : TypeToken<Row>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toRow(json: String?): Row? {
        val type = object : TypeToken<Row>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromForm(data: Form?): String? {
        val type = object : TypeToken<Form>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toForm(json: String?): Form? {
        val type = object : TypeToken<Form>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromFieldsList(data: ArrayList<Fields>?): String? {
        val type = object : TypeToken<ArrayList<Fields>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFieldsList(json: String?): ArrayList<Fields>? {
        val type = object : TypeToken<ArrayList<Fields>>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromFields(data: Fields?): String? {
        val type = object : TypeToken<Fields>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFields(json: String?): Fields? {
        val type = object : TypeToken<Fields>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromFieldsSlugs(data: List<String>?): String? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFieldsSlugs(json: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromMutiPart(data: HashMap<String, Fields>?): String? {
        val type = object : TypeToken<HashMap<String, Fields>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toMutiPart(json: String?): HashMap<String, Fields>? {
        val type = object : TypeToken<HashMap<String, Fields>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromReqHash(data: HashMap<String, String>?): String? {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toReqHash(json: String?): HashMap<String, String>? {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(json, type)
    }


}
