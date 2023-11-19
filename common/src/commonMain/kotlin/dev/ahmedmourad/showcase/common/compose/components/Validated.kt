package dev.ahmedmourad.showcase.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ahmedmourad.showcase.common.utils.Message
import dev.ahmedmourad.showcase.common.utils.MessageStrategy
import dev.ahmedmourad.showcase.common.utils.asStrategy
import dev.ahmedmourad.showcase.common.utils.showsAsError
import dev.ahmedmourad.showcase.common.*
import dev.icerock.moko.resources.desc.desc

@Composable
fun Validated(
    violations: List<Message>,
    modifier: Modifier = Modifier,
    content: @Composable (showError: Boolean) -> Unit
) {
    Column(modifier) {
        val strategy = remember(violations) { violations.asStrategy() }
        val showsAsError = remember(strategy) { strategy.showsAsError() }
        content.invoke(showsAsError)
        when (strategy) {
            is MessageStrategy.Required -> Unit
            is MessageStrategy.Composite -> {
                MessageSection(
                    header = "${RR.strings.must_contain.desc().get()}:",
                    messages = strategy.mustHave
                )
                MessageSection(
                    header = "${RR.strings.must_not_contain.desc().get()}:",
                    messages = strategy.cannotHave
                )
                MessageSection(
                    header = "${RR.strings.illegal_names.desc().get()}:",
                    messages = strategy.illegalNames
                )
                MessageSection(
                    header = "${RR.strings.intersects_with.desc().get()}:",
                    messages = strategy.rangeIntersections
                )
                MessageSection(
                    header = null,
                    messages = strategy.raw
                )
            }
            is MessageStrategy.Fallback -> {
                MessageSection(
                    header = null,
                    messages = strategy.m
                )
            }
            MessageStrategy.None -> Unit
        }
    }
}

@Composable
private fun MessageSection(
    header: String?,
    messages: List<String>,
    modifier: Modifier = Modifier
) {
    if (messages.isNotEmpty()) {
        Column(modifier) {
            Spacer(Modifier.size(8.dp))
            if (header != null) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Box(Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = header,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                messages.forEachIndexed { index, message ->
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    if (index != messages.lastIndex) {
                        Spacer(Modifier.size(4.dp))
                    }
                }
            } else {
                messages.forEachIndexed { index, message ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary, CircleShape)
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = message,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    if (index != messages.lastIndex) {
                        Spacer(Modifier.size(4.dp))
                    }
                }
            }
        }
    }
}
