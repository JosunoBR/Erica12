package com.erica.metas.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.erica.metas.utils.CurrencyUtils

@Composable
fun CurrencyAmountInput(
    label: String,
    rawDigits: String,
    onDigitsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = rawDigits,
        onValueChange = { newValue ->
            // Filtra apenas os números
            val newDigits = newValue.filter { it.isDigit() }
            // Remove zeros à esquerda desnecessários, mas mantém pelo menos um dígito
            val trimmed = newDigits.trimStart('0')
            onDigitsChange(if (trimmed.isEmpty()) "0" else trimmed)
        },
        label = { Text(label) },
        // KeyboardType.NumberPassword é um truque para abrir o teclado numérico 
        // sem a barra de sugestões que atrapalha a visualização
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = CurrencyVisualTransformation(),
        singleLine = true,
        modifier = modifier
    )
}

/**
 * Transforma visualmente o texto para o formato R$ 0,00 em tempo real
 */
class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formatted = CurrencyUtils.formatPixDigitsToBRL(originalText)

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = formatted.length
                override fun transformedToOriginal(offset: Int): Int = originalText.length
            }
        )
    }
}
