package ru.practice.homeworks.presentation.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practice.homeworks.R


@Composable
fun ChartScreen(
    modifier: Modifier,
    viewModel: ChartViewModel = hiltViewModel()
){

    var pointsCount = rememberSaveable { mutableStateOf("")}
    var valuesInput = rememberSaveable { mutableStateOf("") }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = pointsCount.value,
            onValueChange = {
                pointsCount.value = it },
            label = { Text(stringResource(R.string.count_points)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = valuesInput.value,
            onValueChange = {valuesInput.value = it},
            label = { Text(stringResource(R.string.cherez_zap)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {viewModel.validateAndDrawGraph(pointsCount.value,valuesInput.value)},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.draw_graph))
        }


        when (state){
            is ChartUiState.Error -> {
                val message = (state as ChartUiState.Error).message
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            is ChartUiState.Success -> {
                val result = (state as ChartUiState.Success).result
                DrawGraph(result)
            }
            ChartUiState.Idle -> {}
        }
    }
}


@Composable
fun DrawGraph(points: List<Pair<Float, Float>>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp)
    ) {
        val width = size.width
        val height = size.height


        drawLine(
            color = Color.Gray,
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Gray,
            start = Offset(0f, 0f),
            end = Offset(0f, height),
            strokeWidth = 2f
        )

        if (points.isEmpty()) return@Canvas


        val xValues = points.map { it.first }
        val yValues = points.map { it.second }

        val minX = xValues.minOrNull() ?: 0f
        val maxX = xValues.maxOrNull() ?: 1f
        val rangeX = maxX - minX

        val minY = yValues.minOrNull() ?: 0f
        val maxY = yValues.maxOrNull() ?: 1f
        val rangeY = maxY - minY


        fun scaleX(x: Float): Float = ((x - minX) / rangeX) * width
        fun scaleY(y: Float): Float = height - ((y - minY) / rangeY) * height


        val path = Path().apply {
            moveTo(scaleX(points[0].first), scaleY(points[0].second))
        }

        points.forEachIndexed { index, point ->
            val x = scaleX(point.first)
            val y = scaleY(point.second)

            if (index > 0) {
                path.lineTo(x, y)
            }

            drawCircle(
                color = Color.Gray,
                radius = 5f,
                center = Offset(x, y)
            )
        }

        drawPath(
            path = path,
            color = Color(0xFF9A7A1B),
            style = Stroke(width = 2f)
        )


        val gradientPath = Path().apply {
            addPath(path)
            lineTo(scaleX(points.last().first), height)
            lineTo(scaleX(points.first().first), height)
            close()
        }

        drawPath(
            path = gradientPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFD8C993).copy(alpha = 0.5f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = height
            )
        )
    }
}