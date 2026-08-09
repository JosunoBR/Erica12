package com.erica.metas.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    private val localeBR = Locale("pt", "BR")
    private val currencyFormatter = NumberFormat.getCurrencyInstance(localeBR)

    /**
     * Formata um valor Double para a moeda brasileira (ex: R$ 5.850,39)
     */
    fun formatBRL(amount: Double): String {
        return currencyFormatter.format(amount)
    }

    /**
     * Converte uma string contendo apenas dígitos numéricos (estilo PIX) para o valor em Double.
     * Exemplo: "585039" -> 5850.39
     */
    fun parsePixDigitsToDouble(digits: String): Double {
        val cleanDigits = digits.replace(Regex("[^0-9]"), "")
        if (cleanDigits.isEmpty()) return 0.0
        val value = cleanDigits.toDoubleOrNull() ?: 0.0
        return value / 100.0
    }

    /**
     * Formata a string de dígitos do PIX para exibição formatada R$
     */
    fun formatPixDigitsToBRL(digits: String): String {
        val amount = parsePixDigitsToDouble(digits)
        return formatBRL(amount)
    }

    /**
     * Converte um valor Double para a string de dígitos puros (ex: 5850.39 -> "585039")
     */
    fun doubleToPixDigits(amount: Double): String {
        val cents = Math.round(amount * 100)
        return cents.toString()
    }
}
