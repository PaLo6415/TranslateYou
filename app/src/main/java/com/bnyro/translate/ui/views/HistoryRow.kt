package com.bnyro.translate.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.Preferences

@Composable
fun HistoryRow(
    navController: NavController,
    translationModel: TranslationModel,
    historyItem: HistoryItem,
    onDelete: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    fun loadTranslation() {
        showDialog = false
        translationModel.insertedText = historyItem.insertedText
        translationModel.translateNow()
        navController.navigate("translate")
    }

    val compactHistory = Preferences.get(
        Preferences.compactHistory,
        true
    )

    val maxLines = if (compactHistory) 3 else Int.MAX_VALUE

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (compactHistory) {
                    showDialog = true
                } else {
                    loadTranslation()
                }
            }
            .padding(
                start = 15.dp,
                end = 5.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f)

        ) {
            Text(
                historyItem.insertedText,
                fontSize = 18.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )

            Text(
                historyItem.translatedText,
                fontSize = 14.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        StyledIconButton(
            imageVector = Icons.Default.Delete
        ) {
            onDelete.invoke()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            text = {
                Column {
                    Text(
                        historyItem.insertedText,
                        fontSize = 18.sp
                    )

                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )

                    Text(
                        historyItem.translatedText,
                        fontSize = 14.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        loadTranslation()
                    }
                ) {
                    Text(stringResource(R.string.open))
                }
            }
        )
    }
}
