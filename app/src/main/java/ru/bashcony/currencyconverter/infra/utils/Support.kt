package ru.bashcony.currencyconverter.infra.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToLong


fun Double?.format() =
    DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.US)).apply {
        maximumFractionDigits = 340
    }.format(this ?: .0)