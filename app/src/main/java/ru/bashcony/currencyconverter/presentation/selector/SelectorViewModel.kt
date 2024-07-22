package ru.bashcony.currencyconverter.presentation.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.bashcony.currencyconverter.domain.currency.usecase.GetCurrenciesUseCase
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _currentState: MutableLiveData<SelectorState> =
        MutableLiveData(SelectorState.Initial)
    val currentState: LiveData<SelectorState>
        get() = _currentState

    private val _currentCurrency: MutableLiveData<String> = MutableLiveData("")
    val currentCurrency: LiveData<String>
        get() = _currentCurrency

    fun setFromCurrency(newFromCurrency: String) {
        _currentCurrency.postValue(newFromCurrency)
    }

    fun loadCurrencies(offline: Boolean = false) {
        _currentState.value = SelectorState.Loading
        getCurrenciesUseCase(offline)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                _currentState.value = SelectorState.LoadedCurrencies(it)
            }, onError = {
                it.printStackTrace()
                if (!offline)
                    loadCurrencies(true)
                else
                    _currentState.value = SelectorState.Error(it)
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}