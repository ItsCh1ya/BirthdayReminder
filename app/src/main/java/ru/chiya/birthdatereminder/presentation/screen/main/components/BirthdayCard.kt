package ru.chiya.birthdatereminder.presentation.screen.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.chiya.birthdatereminder.R
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.presentation.screen.main.MainViewModel
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
@Composable
fun BirthdayCard(birthday: BirthdayEntity, viewModel: MainViewModel) {
    val expanded = remember { mutableStateOf(false) }
    val maxLines = if (expanded.value) 5 else 1

    // State to track if the edit dialog should be shown
    val showDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded.value = !expanded.value }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    ExpandableText(
                        text = birthday.name,
                        maxLines = maxLines,
                        modifier = Modifier.width(100.dp)
                    )
                    if (birthday.description.isNotEmpty()) {
                        ExpandableText(
                            text = birthday.description,
                            maxLines = maxLines,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
                ExpandableText(
                    text = birthday.category,
                    maxLines = maxLines,
                    modifier = Modifier.width(100.dp)
                )
                Text(text = SimpleDateFormat("dd/MM").format(birthday.birthday))
            }

            AnimatedVisibility(visible = expanded.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { viewModel.deleteBirthday(birthday) }) {
                        Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(onClick = { showDialog.value = true }) {
                        Text(text = "Edit")
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
    }
    if (showDialog.value) {
        EditBirthdayDialog(
            birthday = birthday,
            onDismiss = { showDialog.value = false },
            onEdit = { updatedBirthday ->
                viewModel.editBirthday(updatedBirthday)
                showDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableText(text: String, maxLines: Int, modifier: Modifier = Modifier) {
    AnimatedContent(targetState = maxLines, label = "") { targetLines ->
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = targetLines,
            modifier = modifier
        )
    }
}