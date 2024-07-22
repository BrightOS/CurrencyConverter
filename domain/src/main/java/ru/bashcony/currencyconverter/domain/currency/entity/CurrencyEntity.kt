package ru.bashcony.currencyconverter.domain.currency.entity

data class CurrencyEntity(
    val result: String?,
    val documentation: String?,
    val termsOfUse: String?,
    val timeLastUpdateUnix: Long?,
    val timeLastUpdateUtc: String?,
    val timeNextUpdateUnix: Long?,
    val timeNextUpdateUtc: String?,
    val baseCode: String?,
    val conversionRates: Map<String, Double>?
)