package ru.chiya.birthdatereminder.presentation.screen.main.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.presentation.screen.add.isError
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


@Composable
fun EditBirthdayDialog(
    birthday: BirthdayEntity,
    onDismiss: () -> Unit,
    onEdit: (BirthdayEntity) -> Unit
) {
    // State for editable fields
    val name = remember { mutableStateOf(birthday.name) }
    val description = remember { mutableStateOf(birthday.description) }
    val category = remember { mutableStateOf(birthday.category) }
    val date = remember { mutableStateOf(birthday.birthday) }

    val nameValidationRegex = "[a-zA-Zа-яА-Я0-9 ]{1,25}".toRegex()
    val descriptionValidationRegex = ".{0,50}".toRegex()
    val categoryValidationRegex = "[a-zA-Zа-яА-Я0-9 ]{0,15}".toRegex()


    // Separate variables to store the error states
    val nameError = remember { mutableStateOf(false) }
    val descriptionError = remember { mutableStateOf(false) }
    val categoryError = remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                ModalTop()
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name") },
                    isError = nameError.value
                )
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text(text = "Description") },
                    isError = descriptionError.value
                )
                TextField(
                    value = category.value,
                    onValueChange = { category.value = it },
                    label = { Text(text = "Category") },
                    isError = categoryError.value
                )
                Spacer(modifier = Modifier.height(16.dp))
                SelectDatePeriod(date)
                Spacer(modifier = Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {                    // Set the error states based on validation
                        nameError.value = isError(name.value, nameValidationRegex)
                        descriptionError.value = description.value.isNotEmpty() && isError(
                            description.value,
                            descriptionValidationRegex
                        )
                        categoryError.value = category.value.isNotEmpty() && isError(
                            category.value,
                            categoryValidationRegex
                        )

                        // Check if any field is invalid
                        if (!nameError.value && !descriptionError.value && !categoryError.value) {
                            val updatedBirthday = birthday.copy(
                                name = name.value,
                                description = description.value,
                                category = category.value,
                                birthday = date.value
                            )
                            onEdit(updatedBirthday)
                        } else {
                            Toast.makeText(ctx, "Please verify your input", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDatePeriod(date: MutableState<Date>) {

    val calendarDialogVisible = remember { mutableStateOf(false) }
    Button(onClick = { calendarDialogVisible.value = true }) {
        Text(text = date.value.toString())
    }
    if (calendarDialogVisible.value) {
        CalendarDialog(state = rememberUseCaseState(visible = true, onCloseRequest = {
            calendarDialogVisible.value = false
        }), config = CalendarConfig(
            yearSelection = false, monthSelection = true, style = CalendarStyle.MONTH
        ), selection = CalendarSelection.Date {
            date.value = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant());
        })
    }
}

@Composable
private fun ModalTop() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            Icons.Filled.Edit,
            contentDescription = "edit",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Edit",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}