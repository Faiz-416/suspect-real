package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.DangerButton
import com.example.suspect.ui.theme.DangerBg
import com.example.suspect.ui.theme.DangerRed
import com.example.suspect.ui.theme.DarkCard
import com.example.suspect.ui.theme.TextPrimary

@Composable
fun VoteScreen(
    players: List<Player>,
    votes: Map<String, String>,
    onToggleVote: (String) -> Unit,
    onConfirmVote: () -> Unit
) {
    val selectedPlayerId = votes["GROUP_DECISION"]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        AppHeading("Who is the Impostor?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(players) { p ->
                val isSelected = selectedPlayerId == p.id
                AppCard(
                    onClick = { onToggleVote(p.id) },
                    borderColor = if (isSelected) DangerRed else Color.White.copy(alpha = 0.05f),
                    backgroundColor = if (isSelected) DangerBg else DarkCard,
                    testTag = "vote_card_${p.id}"
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = p.name,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(DangerRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DangerButton(
            text = "Lock In Vote",
            onClick = onConfirmVote,
            enabled = selectedPlayerId != null,
            testTag = "lock_in_vote_button"
        )
    }
}
