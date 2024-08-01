package com.pronoidsoftware.core.presentation.designsystem.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.crop(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        fun Dp.toPxInt(): Int = this.toPx().toInt()

        layout(
            placeable.width - (horizontal * 2).toPxInt(),
            placeable.height - (vertical * 2).toPxInt(),
        ) {
            placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
        }
    }

fun Modifier.ignoreColumnPadding(horizontal: Dp = 0.dp): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + (horizontal * 2).roundToPx(),
            ),
        )
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
