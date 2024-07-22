package ru.bashcony.currencyconverter.data.currency.repository

import io.reactivex.Single
import ru.bashcony.currencyconverter.data.currency.CurrencyMapper.toEntity
import ru.bashcony.currencyconverter.data.currency.CurrencyMapper.toModel
import ru.bashcony.currencyconverter.data.currency.db.CurrencyDao
import ru.bashcony.currencyconverter.domain.currency.CurrencyLocalRepository
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyLocalEntity
import javax.inject.Inject

class CurrencyLocalRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao
): CurrencyLocalRepository {
    override fun updateCurrencies(currencies: List<CurrencyLocalEntity>) =
        currencyDao.updateCurrencies(currencies.map { it.toModel() })

    override fun getAllCurrencies(): Single<List<CurrencyLocalEntity>> =
        currencyDao.getAllCurrencies().map { it.map { it.toEntity() } }
}