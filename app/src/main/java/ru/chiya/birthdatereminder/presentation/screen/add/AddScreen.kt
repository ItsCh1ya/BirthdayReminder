package ru.chiya.birthdatereminder.presentation.screen.add

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: AddViewModel = hiltViewModel(), navController: NavController
) {

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val date = remember { mutableStateOf<LocalDate?>(null) }

    val nameValidationRegex = "[a-zA-Zа-яА-Я0-9 ]{1,25}".toRegex()
    val descriptionValidationRegex = ".{0,50}".toRegex()
    val categoryValidationRegex = "[a-zA-Zа-яА-Я0-9 ]{0,15}".toRegex()


    // Separate variables to store the error states
    val nameError = remember { mutableStateOf(false) }
    val descriptionError = remember { mutableStateOf(false) }
    val categoryError = remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add birthday")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... Other parts of the Composable ...
            Column(modifier = Modifier.padding(horizontal = 64.dp)) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    isError = nameError.value // Use the stored error state
                )

                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    isError = descriptionError.value // Use the stored error state
                )

                TextField(
                    value = category.value,
                    onValueChange = { category.value = it },
                    label = { Text("Category") },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    isError = categoryError.value // Use the stored error state
                )
            }

            val calendarDialogVisible = remember { mutableStateOf(false) }
            if (calendarDialogVisible.value) {
                CalendarDialog(state = rememberUseCaseState(visible = true, onCloseRequest = {
                    calendarDialogVisible.value = false
                }), config = CalendarConfig(
                    yearSelection = false, monthSelection = true, style = CalendarStyle.MONTH
                ), selection = CalendarSelection.Date {
                    date.value = it
                })
            }
            OutlinedButton(
                onClick = { calendarDialogVisible.value = !calendarDialogVisible.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 84.dp)
            ) {
                Text(text = date.value?.toString() ?: "Select date")
            }
            Button(
                onClick = {
                    // Set the error states based on validation
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
                    if (!nameError.value && !descriptionError.value && !categoryError.value && date.value != null) {
                        // Save the reminder with the entered data
                        viewModel.addBirthday(
                            name.value, description.value, category.value, date.value!!
                        )
                        // Navigate back to the previous screen
                        navController.popBackStack()
                    } else {
                        Toast.makeText(ctx, "Please verify your input", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}

fun isError(value: String, validationRegex: Regex): Boolean {
    return value.isBlank() || !value.matches(validationRegex)
}