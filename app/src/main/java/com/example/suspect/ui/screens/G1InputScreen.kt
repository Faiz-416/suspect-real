package com.example.suspect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppSubHeading
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.theme.AccentPurple
import com.example.suspect.ui.theme.TextMuted
import com.example.suspect.ui.theme.TextPrimary

@Composable
fun G1InputScreen(
    player: Player,
    onSubmitAnswer: (String) -> Unit
) {
    var answerInput by remember(player.id) { mutableStateOf("") }

    val submit = {
        if (answerInput.isNotBlank()) {
            onSubmitAnswer(answerInput.trim())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            AppSubHeading("Answering as")
            AppHeading(player.name)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BasicTextField(
                    value = answerInput,
                    onValueChange = { answerInput = it },
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(AccentPurple),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("g1_answer_text_field"),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (answerInput.isEmpty()) {
                                Text(
                                    text = "...",
                                    color = TextMuted.copy(alpha = 0.3f),
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Enter a value that answers your question honestly.",
                    color = TextMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }

        PrimaryButton(
            text = "Confirm Answer",
            onClick = submit,
            enabled = answerInput.isNotBlank(),
            testTag = "g1_confirm_answer_button"
        )
    }
}
