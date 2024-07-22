package ru.bashcony.currencyconverter.data.currency

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.bashcony.currencyconverter.data.common.module.NetworkModule
import ru.bashcony.currencyconverter.data.currency.db.CurrencyDao
import ru.bashcony.currencyconverter.data.currency.db.CurrencyLocalDatabase
import ru.bashcony.currencyconverter.data.currency.remote.api.CurrencyApi
import ru.bashcony.currencyconverter.data.currency.repository.CurrencyLocalRepositoryImpl
import ru.bashcony.currencyconverter.data.currency.repository.CurrencyRepositoryImpl
import ru.bashcony.currencyconverter.domain.currency.CurrencyLocalRepository
import ru.bashcony.currencyconverter.domain.currency.CurrencyRepository
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class CurrencyModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideCurrencyRepository(CurrencyApi: CurrencyApi): CurrencyRepository =
        CurrencyRepositoryImpl(CurrencyApi)

    @Singleton
    @Provides
    fun provideCurrencyLocalRepository(CurrencyDao: CurrencyDao): CurrencyLocalRepository =
        CurrencyLocalRepositoryImpl(CurrencyDao)

    @Singleton
    @Provides
    fun provideCurrencyDao(database: CurrencyLocalDatabase) = database.currencyDao()

    @Singleton
    @Provides
    fun provideCurrencyLocalDatabase(@ApplicationContext context: Context): CurrencyLocalDatabase =
        Room.databaseBuilder(context, CurrencyLocalDatabase::class.java, "currencies_database")
            .fallbackToDestructiveMigration()
            .setQueryCallback({ sqlQuery, bindArgs ->
                println("SQL Query: $sqlQuery SQL Args: $bindArgs")
            }, Executors.newSingleThreadExecutor())
            .build()
}