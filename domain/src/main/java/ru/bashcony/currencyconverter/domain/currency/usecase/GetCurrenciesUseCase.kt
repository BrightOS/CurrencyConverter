package ru.bashcony.currencyconverter.domain.currency.usecase

import android.util.Log
import io.reactivex.Single
import ru.bashcony.currencyconverter.domain.common.SettingsRepository
import ru.bashcony.currencyconverter.domain.currency.CurrencyLocalRepository
import ru.bashcony.currencyconverter.domain.currency.CurrencyRepository
import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyLocalEntity
import java.util.Currency
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val currencyLocalRepository: CurrencyLocalRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(offline: Boolean = false): Single<List<CurrencyLocalEntity>> =
        if (System.currentTimeMillis() / 1000L > settingsRepository.timeNextUpdateUnix && !offline) {
            Log.w("GetCurrenciesUseCase", "Load online")
            currencyRepository
                .getCurrencyInfo("USD")
                .map {
                    settingsRepository.timeNextUpdateUnix = it.timeNextUpdateUnix ?: 0L
                    it.conversionRates.orEmpty().keys.map {
                        CurrencyLocalEntity(
                            it,
                            Currency.getInstance(it).getDisplayName(Locale("RU"))
                        )
                    }.also { currencyLocalRepository.updateCurrencies(it) }
                }
        } else {
            Log.w("GetCurrenciesUseCase", "Load offline")
            currencyLocalRepository.getAllCurrencies()
        }
}