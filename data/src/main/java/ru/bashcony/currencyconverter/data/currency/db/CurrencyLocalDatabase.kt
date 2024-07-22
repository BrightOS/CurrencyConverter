package ru.bashcony.currencyconverter.data.currency.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyLocalModel::class], version = 1, exportSchema = true)
abstract class CurrencyLocalDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}