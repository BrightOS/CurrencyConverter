package ru.bashcony.currencyconverter.infra.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

fun Context.getEmojiByCountryCode(countryCode: String): String =
    readJSONFromAsset("country-data.json")?.let {
        JSONObject(it).getString(countryCode)
    } ?: "❓"

fun Context.getEmojiByCurrencyCode(currencyCode: String): String =
    readJSONFromAsset("currency-data.json")?.let {
        JSONObject(it).getString(currencyCode)
    } ?: "❓"

private fun Context.readJSONFromAsset(assetFileName: String): String? {
    var json: String? = null
    try {
        val inputStream = assets.open(assetFileName)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}