package ru.bashcony.currencyconverter.presentation.converted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.bashcony.currencyconverter.domain.currency.usecase.GetCurrenciesUseCase
import ru.bashcony.currencyconverter.domain.currency.usecase.GetPairConversionUseCase
import ru.bashcony.currencyconverter.presentation.selector.SelectorState
import javax.inject.Inject

@HiltViewModel
class ConvertedViewModel @Inject constructor(
    private val getPairConversionUseCase: GetPairConversionUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _currentState: MutableLiveData<ConvertedState> = MutableLiveData(ConvertedState.Initial)
    val currentState: LiveData<ConvertedState>
        get() = _currentState

    fun loadConversion(from: String, to: String, amount: String) {
        _currentState.postValue(ConvertedState.Loading)

        getPairConversionUseCase(from, to, amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _currentState.postValue(ConvertedState.LoadedConversion(it))
                },
                onError = {
                    _currentState.postValue(ConvertedState.Error(it))
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}