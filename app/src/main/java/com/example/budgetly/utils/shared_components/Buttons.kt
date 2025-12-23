package com.example.budgetly.utils.shared_components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.btnGradientColor1
import com.example.budgetly.utils.grey
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.white

@Composable
fun SmallRoundedButton(isActive:Boolean, buttonText:String, onButtonClick: () -> Unit){
    Box(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 5.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    if(isActive){
                        listOf(
                            secondaryColor,
                            secondaryColor
                        )
                    }else{
                        listOf(
                            inactiveButtonColor,
                            inactiveButtonColor
                        )
                    }
                ),
                CircleShape
            )
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .safeClickAble {
                onButtonClick.invoke()
            }, contentAlignment = Alignment.Center
    ) {
        Text(text = buttonText, style = SubtitleMedium, color = if(isActive) white else grey)
    }
}
@Composable
fun PrimaryButtonRounded(
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 40,
    paddingValues: PaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
    gradientColors: List<Color> = listOf(primaryColor, primaryColor),
    isRound:Boolean = true,
    borderRadius:Float = 12f,
    onButtonClick: () -> Unit,
    content: @Composable () -> Unit
) {
//    val gradientColors = listOf(GradientColor1, GradientColor2)
//    val gradientColors = listOf(NewPrimaryColor, NewPrimaryColor)
    val transparentColors = listOf(Color.Transparent, Color.Transparent)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding.dp)
            .clip(if(isRound)CircleShape else RoundedCornerShape(borderRadius))
            .background(Brush.linearGradient(gradientColors), if(isRound)CircleShape else RoundedCornerShape(borderRadius))
            .border(
                width = 1.dp, brush = Brush.linearGradient(transparentColors), shape = if(isRound)CircleShape else RoundedCornerShape(borderRadius)
            )
            .padding(paddingValues)
            .safeClickAble {
                onButtonClick.invoke()

            }, contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun SecondaryButtonRounded(
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 40,
    paddingValues: PaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
    gradientColors: List<Color> = listOf(btnGradientColor1, btnGradientColor1),
    transparentColors: List<Color> = listOf(Color.Transparent, Color.Transparent),
    onButtonClick: () -> Unit,
    isRound:Boolean = true,
    borderRadius:Float = 12f,
    content: @Composable () -> Unit
) {
//    val gradientColors = listOf(GradientColor1, GradientColor2)
//    val transparentColors = listOf(Color.Transparent, Color.Transparent)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding.dp)
            .clip(if(isRound)CircleShape else RoundedCornerShape(borderRadius))
            .background(Brush.linearGradient(transparentColors), if(isRound)CircleShape else RoundedCornerShape(borderRadius))
            .border(
                width = 1.dp, brush = Brush.linearGradient(gradientColors), shape = if(isRound)CircleShape else RoundedCornerShape(borderRadius)
            )
            .padding(paddingValues)
            .safeClickAble {
                onButtonClick.invoke()

            }, contentAlignment = Alignment.Center
    ) {
        content()
    }
}