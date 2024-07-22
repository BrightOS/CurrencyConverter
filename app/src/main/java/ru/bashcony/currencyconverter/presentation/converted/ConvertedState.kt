package ru.bashcony.currencyconverter.presentation.converted

import ru.bashcony.currencyconverter.domain.currency.entity.PairConversionEntity
import ru.bashcony.currencyconverter.presentation.selector.SelectorState

sealed class ConvertedState {
    object Initial: ConvertedState()
    object Loading: ConvertedState()
    class LoadedConversion(val conversion: PairConversionEntity): ConvertedState()
    class Error(val e: Throwable): ConvertedState()
}