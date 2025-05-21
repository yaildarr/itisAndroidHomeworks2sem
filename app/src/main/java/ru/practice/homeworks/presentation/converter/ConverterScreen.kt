package ru.practice.homeworks.presentation.converter

import android.graphics.drawable.shapes.Shape

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SweepGradient
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practice.homeworks.R

@Composable
fun ConverterScreen(
    modifier: Modifier,
    viewModel: ConverterViewModel = hiltViewModel()
) {

    var fromCurrency = rememberSaveable { mutableStateOf("") }
    var toCurrency = rememberSaveable { mutableStateOf("")}
    var amount = rememberSaveable { mutableStateOf("")}

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when(state){

        is ConverterUiState.Idle -> IdleConverterScreen(
            modifier,
            onClick = { to, from, amountStr ->
                viewModel.convert(from, to, amountStr)
            },
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            amount = amount
        )

        is ConverterUiState.Loading -> LoadingConverterScreen()

        is ConverterUiState.Error -> ErrorConverterScreen(
            { viewModel.resetState() },
            (state as ConverterUiState.Error).message
        )

        is ConverterUiState.Success -> {
            val result = (state as ConverterUiState.Success).result
            SuccessConverterScreen(
                modifier = modifier,
                onClick = { to, from, amountStr ->
                    viewModel.convert(from, to, amountStr)
                },
                result = result,
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                amount = amount
            )
        }
    }

}

@Composable
fun ErrorConverterScreen(onClick: () -> Unit, message: String){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message
        )
        Button(
            onClick = {onClick()}
        ) {
            Text(
                text = stringResource(R.string.retry)
            )
        }
    }
}


@Composable
fun SuccessConverterScreen(modifier: Modifier, onClick: (to : String, from : String, amount : String) -> (Unit), result: String, fromCurrency: MutableState<String>,toCurrency: MutableState<String>,amount: MutableState<String>){
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize()
    ){
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.currency_converter),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.from),
            fontSize = 24.sp
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fromCurrency.value,
            onValueChange = {fromCurrency.value = it},
            label = {
                Text(
                    text = stringResource(R.string.currency_code)
                )
            }
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = "To",
            fontSize = 24.sp
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = toCurrency.value,
            onValueChange = {toCurrency.value = it},
            label = {
                Text(
                    text = stringResource(R.string.currency_code)
                )
            }
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount.value,
            onValueChange = {amount.value = it},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    text = stringResource(R.string.amount)
                )
            }
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                onClick(toCurrency.value,fromCurrency.value,amount.value)
            },
            shape = RoundedCornerShape(40),
        ) {
            Text(
                text = stringResource(R.string.convert),
                fontSize = 24.sp,
            )
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)))

        Text(
            text = stringResource(R.string.result),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        Box(
            modifier = Modifier
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(40)

                )
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = result
            )
        }
    }
}

@Composable
fun LoadingConverterScreen() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        PlaceholderBox(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        PlaceholderBox(
            modifier = Modifier
                .height(24.dp)
                .width(50.dp)
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        PlaceholderBox(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        PlaceholderBox(
            modifier = Modifier
                .height(24.dp)
                .width(30.dp)
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        PlaceholderBox(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        PlaceholderBox(
            modifier = Modifier
                .height(24.dp)
                .width(80.dp)
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        PlaceholderBox(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        PlaceholderBox(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)))

        PlaceholderBox(
            modifier = Modifier
                .height(24.dp)
                .width(60.dp)
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        PlaceholderBox(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun IdleConverterScreen(
    modifier: Modifier,
    onClick: (to : String, from : String, amount : String) -> (Unit) ,
    fromCurrency: MutableState<String>,
    toCurrency : MutableState<String>,
    amount : MutableState<String>
){
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxSize()
    ){
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.currency_converter),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.from),
            fontSize = 24.sp
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fromCurrency.value,
            onValueChange = {fromCurrency.value = it},
            label = {
                Text(
                    text = stringResource(R.string.currency_code)
                )
            }
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = "To",
            fontSize = 24.sp
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = toCurrency.value,
            onValueChange = {toCurrency.value = it},
            label = {
                Text(
                    text = stringResource(R.string.currency_code)
                )
            }
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount.value,
            onValueChange = {amount.value = it},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    text = stringResource(R.string.amount)
                )
            },
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                onClick(toCurrency.value,fromCurrency.value,amount.value)
            },
            shape = RoundedCornerShape(40),
        ) {
            Text(
                text = stringResource(R.string.convert),
                fontSize = 24.sp,
            )
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)))

        Text(
            text = stringResource(R.string.result),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
    }
}

@Composable
fun PlaceholderBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color)
    )
}

@Composable
@Preview
private fun Preview(){
    MaterialTheme{
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            ConverterScreen(modifier = Modifier.padding(padding))
        }
    }
}

