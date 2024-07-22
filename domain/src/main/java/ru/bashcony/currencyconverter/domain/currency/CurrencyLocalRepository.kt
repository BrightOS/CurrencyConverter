package ru.bashcony.currencyconverter.domain.currency

import io.reactivex.Single
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyLocalEntity

interface CurrencyLocalRepository {
    fun updateCurrencies(currencies: List<CurrencyLocalEntity>)
    fun getAllCurrencies(): Single<List<CurrencyLocalEntity>>
}