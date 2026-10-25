package com.example.suspect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.theme.AccentPurple
import com.example.suspect.ui.theme.TextMuted

@Composable
fun G1RevealScreen(
    players: List<Player>,
    onStartDiscussion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        AppHeading("All Answers", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(players) { p ->
                AppCard(
                    modifier = Modifier.padding(0.dp),
                    testTag = "g1_reveal_card_${p.id}"
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = p.name,
                            color = TextMuted,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = p.answer ?: "-",
                            color = AccentPurple,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Start Discussion",
            onClick = onStartDiscussion,
            testTag = "g1_start_discussion_button"
        )
    }
}
