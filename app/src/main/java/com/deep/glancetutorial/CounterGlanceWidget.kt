package com.deep.glancetutorial

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

val prefCountKey = intPreferencesKey("pref_count_key")

class CounterGlanceWidget: GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val count = currentState(key = prefCountKey) ?: 0
            CounterGlance(count)
        }
    }
}

@Composable
@GlanceComposable
fun CounterGlance(count: Int) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = ColorProvider(Color.White),
                fontSize = 24.sp
            )
        )
        Spacer(modifier = GlanceModifier.fillMaxWidth().height(4.dp))
        Button(
            text = "Increment Count",
            onClick = actionRunCallback(IncrementCountAction::class.java)
        )
    }
}

class CounterWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterGlanceWidget()
}

object IncrementCountAction: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(
            context,
            glanceId
        ) { prefs ->
            val currentCount = prefs[prefCountKey] ?: 0
            prefs[prefCountKey] = currentCount + 1
        }
        CounterGlanceWidget().update(context, glanceId)
    }
}