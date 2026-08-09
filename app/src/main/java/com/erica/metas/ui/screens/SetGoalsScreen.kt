package com.erica.metas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.data.SalesViewModel
import com.erica.metas.ui.components.CurrencyAmountInput
import com.erica.metas.utils.CurrencyUtils

@Composable
fun SetGoalsScreen(
    viewModel: SalesViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val context = LocalContext.current

    var metaDigits by remember(goals.meta) {
        mutableStateOf(CurrencyUtils.doubleToPixDigits(goals.meta))
    }
    var superMetaDigits by remember(goals.superMeta) {
        mutableStateOf(CurrencyUtils.doubleToPixDigits(goals.superMeta))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Definir Metas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Insira os valores da Meta e da Super Meta com os números em formato PIX.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // 1. Campo Editável: Meta (Máscara Moeda BRL / PIX)
        CurrencyAmountInput(
            label = "Meta Principal (R$)",
            rawDigits = metaDigits,
            onDigitsChange = { newDigits -> metaDigits = newDigits },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Campo Editável: Super Meta (Máscara Moeda BRL / PIX)
        CurrencyAmountInput(
            label = "Super Meta (R$)",
            rawDigits = superMetaDigits,
            onDigitsChange = { newDigits -> superMetaDigits = newDigits },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.updateGoals(metaDigits, superMetaDigits)
                Toast.makeText(context, "Metas atualizadas com sucesso!", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Salvar Metas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
