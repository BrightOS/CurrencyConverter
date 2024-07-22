package ru.bashcony.currencyconverter.data.currency.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("currency_table")
data class CurrencyLocalModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("code") val code: String = "",
    @SerializedName("localized_name") val localizedName: String? = "",
)