package ru.bashcony.currencyconverter.domain.currency

import io.reactivex.Single
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyEntity
import ru.bashcony.currencyconverter.domain.currency.entity.PairConversionEntity

interface CurrencyRepository {
    fun getCurrencyInfo(currency: String): Single<CurrencyEntity>
    fun getPairConversion(currencyFrom: String, currencyTo: String, amount: String): Single<PairConversionEntity>
}