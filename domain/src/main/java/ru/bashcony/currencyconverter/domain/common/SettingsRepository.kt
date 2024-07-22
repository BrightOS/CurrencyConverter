package ru.bashcony.currencyconverter.domain.common

interface SettingsRepository {
    var timeNextUpdateUnix: Long
}