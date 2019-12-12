package com.xpayworld.payment.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

object HashMapTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: JsonElement): HashMap<String, String> {
        return Gson().fromJson(value,  object : TypeToken<HashMap<String, String>>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun mapToString(value: HashMap<String, String>?): String {
        return if(value == null) "" else Gson().toJson(value)
    }
}