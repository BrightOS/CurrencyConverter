package ru.bashcony.currencyconverter.data.currency.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrencyResponse(
    @SerializedName("result") val result: String? = null,
    @SerializedName("documentation") val documentation: String? = null,
    @SerializedName("terms_of_use") val termsOfUse: String? = null,
    @SerializedName("time_last_update_unix") val timeLastUpdateUnix: Long? = null,
    @SerializedName("time_last_update_utc") val timeLastUpdateUtc: String? = null,
    @SerializedName("time_next_update_unix") val timeNextUpdateUnix: Long? = null,
    @SerializedName("time_next_update_utc") val timeNextUpdateUtc: String? = null,
    @SerializedName("base_code") val baseCode: String? = null,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>? = mapOf()
)
