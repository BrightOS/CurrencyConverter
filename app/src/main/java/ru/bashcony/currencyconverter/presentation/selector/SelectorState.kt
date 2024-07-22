package ru.bashcony.currencyconverter.presentation.selector

import ru.bashcony.currencyconverter.domain.currency.entity.CurrencyLocalEntity

sealed class SelectorState {
    object Initial: SelectorState()
    object Loading: SelectorState()
    class LoadedCurrencies(val currencies: List<CurrencyLocalEntity>): SelectorState()
    class Error(val e: Throwable): SelectorState()
}