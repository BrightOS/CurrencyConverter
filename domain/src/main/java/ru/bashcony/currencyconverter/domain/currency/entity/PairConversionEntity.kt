package ru.bashcony.currencyconverter.domain.currency.entity

class PairConversionEntity(
    val result: String?,
    val documentation: String?,
    val termsOfUse: String?,
    val timeLastUpdateUnix: Long?,
    val timeLastUpdateUtc: String?,
    val timeNextUpdateUnix: Long?,
    val timeNextUpdateUtc: String?,
    val baseCode: String?,
    val targetCode: String?,
    val conversionRate: Double?,
    val conversionResult: Double?
)