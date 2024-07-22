package ru.bashcony.currencyconverter.data.currency.repository

import ru.bashcony.currencyconverter.data.currency.CurrencyMapper.toEntity
import ru.bashcony.currencyconverter.data.currency.remote.api.CurrencyApi
import ru.bashcony.currencyconverter.domain.currency.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi
) : CurrencyRepository {

    override fun getCurrencyInfo(
        currency: String
    ) = currencyApi
        .getCurrencyInfo(currency).map { it.toEntity() }

    override fun getPairConversion(
        currencyFrom: String,
        currencyTo: String,
        amount: String
    ) = currencyApi
        .getPairConversion(currencyFrom, currencyTo, amount)
        .map { it.toEntity() }

}