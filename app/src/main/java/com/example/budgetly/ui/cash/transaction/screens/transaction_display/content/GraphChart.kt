
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer.ColumnProvider.Companion.series
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import ir.kaaveh.sdpcompose.sdp
import kotlin.random.Random


@Composable
fun GraphChart(
    modifier: Modifier = Modifier,
    graphPoints: List<Pair<String, Double>>,
    isBar: Boolean,
    barModelProducer: CartesianChartModelProducer,
    lineModelProducer: CartesianChartModelProducer,
) {
    val xLabels = graphPoints.mapIndexed { index, pair -> index to pair.first }.toMap()

    val xValueFormatter: (CartesianMeasuringContext, Double, Axis.Position.Vertical?) -> CharSequence =
        { _, value, _ -> xLabels[value.toInt()] ?: "_" }

    val yValueFormatter: (CartesianMeasuringContext, Double, Axis.Position.Vertical?) -> CharSequence =
        { _, value, _ -> "${value.toInt()}" }

    val axisX = HorizontalAxis.bottom(
        valueFormatter = xValueFormatter,
        label = rememberAxisLabelComponent(color = textColor),
        line = rememberAxisLineComponent(fill = fill(textColor)),
        tick = rememberAxisTickComponent(strokeThickness = 1.sdp, strokeFill = fill(textColor))
    )

    val axisY = VerticalAxis.start(
        valueFormatter = yValueFormatter,
        label = rememberAxisLabelComponent(color = textColor),
        line = rememberAxisLineComponent(fill = fill(textColor)),
        tick = rememberAxisTickComponent(strokeThickness = 1.sdp, strokeFill = fill(textColor))
    )

    val marker = rememberDefaultCartesianMarker(
        // A vertical line from top to bottom
        guideline = rememberLineComponent(
            fill = fill(secondaryColor) , // ✅ full-length line color
            thickness = 0.5.dp
        ),
        label = rememberTextComponent(
            background = rememberShapeComponent(
                strokeFill = fill(Color.Transparent), // ✅ label background
                fill = fill(Color.Transparent) // ✅ label background
            ),
            typeface = android.graphics.Typeface.DEFAULT_BOLD,
            textSize = TextUnit(16f,TextUnitType.Sp),
            color = primaryColor,
        )
    )
        val columnColors = mutableListOf<Color>()
        graphPoints.forEach{ _ ->
            columnColors.add(Utils.lerpColor(
                Utils.themeColors.random(),
                Utils.themeColors.random(),
                Random.nextFloat()
            ))
        }
        val columnLayer = rememberColumnCartesianLayer(
            columnProvider = series(
                rememberLineComponent(fill(primaryColor), thickness = 16.dp)
            )
        )
    val lineProvider = LineCartesianLayer.LineProvider.series(
        listOf(
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(
                    fill = fill(primaryColor)
                )
            )
        )
    )
    val lineLayer = rememberLineCartesianLayer(
        lineProvider = lineProvider
    )
    val chart = rememberCartesianChart(
        layers = arrayOf(if (isBar) columnLayer else lineLayer),
        bottomAxis = axisX,
        startAxis = axisY,
        marker = marker,
    )
    Column(modifier = modifier.fillMaxWidth().background(shape = RoundedCornerShape(8.sdp), color = secondaryBgColor).padding(6.sdp)){
        if (isBar) {
            CartesianChartHost(
                chart = chart,
                modelProducer = barModelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.sdp)
            )
        } else {
            CartesianChartHost(
                chart = chart,
                modelProducer = lineModelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.sdp)
            )
        }
    }
}


