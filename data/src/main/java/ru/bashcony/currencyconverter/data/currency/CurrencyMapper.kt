package ru.bashcony.currencyconverter.data.currency

import ru.bashcony.currencyconverter.data.currency.db.CurrencyLocalModel
import ru.bashcony.currencyconverter.data.currency.remote.dto.CurrencyResponse
import ru.bashcony.currencyconverter.data.currency.remote.dto.PairConversionResponse
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyEntity
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyLocalEntity
import ru.bashcony.currencyconverter.domain.currency.entity.PairConversionEntity

object CurrencyMapper {
    fun CurrencyResponse.toEntity() =
        CurrencyEntity(
            result,
            documentation,
            termsOfUse,
            timeLastUpdateUnix,
            timeLastUpdateUtc,
            timeNextUpdateUnix,
            timeNextUpdateUtc,
            baseCode,
            conversionRates
        )

    fun PairConversionResponse.toEntity() =
        PairConversionEntity(
            result,
            documentation,
            termsOfUse,
            timeLastUpdateUnix,
            timeLastUpdateUtc,
            timeNextUpdateUnix,
            timeNextUpdateUtc,
            baseCode,
            targetCode,
            conversionRate,
            conversionResult
        )

    fun CurrencyLocalModel.toEntity(): CurrencyLocalEntity =
        CurrencyLocalEntity(
            code,
            localizedName
        )

    fun CurrencyLocalEntity.toModel(): CurrencyLocalModel =
        CurrencyLocalModel(
            code ?: "",
            localizedName
        )
}