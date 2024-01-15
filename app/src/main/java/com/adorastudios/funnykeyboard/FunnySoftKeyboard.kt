package com.adorastudios.funnykeyboard

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.adorastudios.funnykeyboard.ui.theme.FunnyKeyboardTheme
import java.util.Locale
import kotlin.math.max

class FunnySoftKeyboard : InputMethodService() {
    private val keyboardLifecycleOwner = KeyboardLifecycleOwner()

    companion object {
        private const val keyboardPadding = 8
        private const val keyboardHorizontalPadding = 6
    }

    override fun onCreate() {
        super.onCreate()
        keyboardLifecycleOwner.onCreate()
    }

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        keyboardLifecycleOwner.attachToDecorView(
            window?.window?.decorView,
        )

        val view = (layoutInflater.inflate(R.layout.compose_view, null) as ComposeView).apply {
            setContent {
                var state by remember {
                    mutableStateOf(FunnyKeyboardData())
                }
                FunnyKeyboardTheme {
                    Keyboard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(
                                vertical = keyboardPadding.dp,
                                horizontal = keyboardHorizontalPadding.dp,
                            ),
                        state = state,
                        onClick = { data ->
                            when (data) {
                                is FunnyKeyData.Action -> {
                                    when (data.action) {
                                        ActionType.Delete -> {
                                            currentInputConnection.deleteSurroundingText(1, 0)
                                        }

                                        ActionType.Up -> {
                                            state = state.copy(
                                                up = !state.up,
                                            )
                                        }
                                    }
                                }

                                is FunnyKeyData.Symbol -> {
                                    val currentUp = state.up

                                    if (currentUp) {
                                        currentInputConnection.commitText(
                                            data.actualValue.capitalize(),
                                            1,
                                        )
                                        state = state.copy(
                                            up = false,
                                        )
                                    } else {
                                        currentInputConnection.commitText(data.actualValue, 1)
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        keyboardLifecycleOwner.onResume()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        keyboardLifecycleOwner.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardLifecycleOwner.onDestroy()
    }

    @Composable
    fun Keyboard(
        modifier: Modifier,
        state: FunnyKeyboardData,
        onClick: (FunnyKeyData) -> Unit,
    ) {
        val total = remember(state.firstRow, state.secondRow, state.thirdRow) {
            max(max(state.firstRow.size, state.secondRow.size), state.thirdRow.size)
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(keyboardPadding.dp),
        ) {
            KeyboardRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(36.dp),
                keyDataList = state.numbersRow,
                up = state.up,
                onClick = onClick,
            )
            KeyboardRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                keyDataList = state.firstRow,
                up = state.up,
                onClick = onClick,
            )
            KeyboardRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                keyDataList = state.secondRow,
                up = state.up,
                onClick = onClick,
            )
            KeyboardRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                keyDataList = state.thirdRow,
                up = state.up,
                onClick = onClick,
            )
            BottomRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(36.dp),
                total = total,
                onClick = onClick,
            )
        }
    }

    @Composable
    private fun BottomRow(
        modifier: Modifier,
        total: Int,
        onClick: (FunnyKeyData) -> Unit,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(keyboardHorizontalPadding.dp),
        ) {
            val l1 = FunnyKeyData.Symbol("?", "?")
            KeyboardKey(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                keyData = l1,
                up = false,
                onClick = {
                    onClick(l1)
                },
            )
            val l2 = FunnyKeyData.Symbol(",", ",")
            KeyboardKey(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                keyData = l2,
                up = false,
                onClick = {
                    onClick(l2)
                },
            )
            val main = FunnyKeyData.Symbol("Funny Keyboard :)", " ")
            KeyboardKey(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(total - 4f),
                keyData = main,
                up = false,
                onClick = {
                    onClick(main)
                },
            )
            val r1 = FunnyKeyData.Symbol(".", ".")
            KeyboardKey(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                keyData = r1,
                up = false,
                onClick = {
                    onClick(r1)
                },
            )
            val r2 = FunnyKeyData.Symbol("!", "!")
            KeyboardKey(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                keyData = r2,
                up = false,
                onClick = {
                    onClick(r2)
                },
            )
        }
    }

    @Composable
    private fun KeyboardRow(
        modifier: Modifier,
        keyDataList: List<FunnyKeyData>,
        up: Boolean,
        onClick: (FunnyKeyData) -> Unit,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(keyboardHorizontalPadding.dp),
        ) {
            for (keyData in keyDataList) {
                KeyboardKey(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    keyData = keyData,
                    up = up,
                    onClick = {
                        onClick(keyData)
                    },
                )
            }
        }
    }

    @Composable
    private fun KeyboardKey(
        modifier: Modifier,
        keyData: FunnyKeyData,
        up: Boolean,
        onClick: () -> Unit,
    ) {
        when (keyData) {
            is FunnyKeyData.Action -> {
                FilledTonalButton(
                    modifier = modifier.fillMaxSize(),
                    onClick = onClick,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Icon(
                        imageVector = keyData.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            is FunnyKeyData.Symbol -> {
                FilledTonalButton(
                    modifier = modifier.fillMaxSize(),
                    onClick = onClick,
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Text(
                        text = if (up) keyData.content.capitalize() else keyData.content,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(
                    Locale.getDefault(),
                )
            } else {
                it.toString()
            }
        }
    }
}
