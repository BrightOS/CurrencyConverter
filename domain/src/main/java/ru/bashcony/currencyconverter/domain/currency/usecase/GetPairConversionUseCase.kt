package ru.bashcony.currencyconverter.domain.currency.usecase

import ru.bashcony.currencyconverter.domain.currency.CurrencyRepository
import javax.inject.Inject

class GetPairConversionUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(currencyFrom: String, currencyTo: String, amount: String) =
        currencyRepository.getPairConversion(currencyFrom, currencyTo, amount)
}