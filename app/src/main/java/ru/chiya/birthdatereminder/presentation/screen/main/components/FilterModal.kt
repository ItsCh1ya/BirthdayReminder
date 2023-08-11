package ru.chiya.birthdatereminder.presentation.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import ru.chiya.birthdatereminder.R
import ru.chiya.birthdatereminder.presentation.screen.main.MainViewModel
import ru.chiya.birthdatereminder.domain.model.DatePeriod
import ru.chiya.birthdatereminder.domain.model.Filter
import java.time.format.DateTimeFormatter


@Composable
fun FilterModal(showDialog: MutableState<Boolean>, viewModel: MainViewModel) {
    Dialog(onDismissRequest = { showDialog.value = false }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                ModalTop()
                Spacer(modifier = Modifier.height(16.dp))
                InputFields(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                Categories(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                SelectDatePeriod(viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                Buttons(viewModel, showDialog)
            }
        }
    }
}

@Composable
private fun Buttons(viewModel: MainViewModel, showDialog: MutableState<Boolean>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.filter = Filter(null, null, mutableListOf(), null)
            viewModel.filterBirthdays()
            showDialog.value = false
        }) {
            Text(text = "Clear")
        }
        TextButton(onClick = {
            viewModel.filterBirthdays()
            showDialog.value = false
        }) {
            Text(text = "Search")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDatePeriod(viewModel: MainViewModel) {
    val calendarDialogVisible = remember { mutableStateOf<Boolean>(false) }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    Column(Modifier.fillMaxWidth()) {
        Text(text = "Period:", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(onClick = { calendarDialogVisible.value = true }) {
            if (viewModel.filter.datePeriod != null){
                val datePeriod = viewModel.filter.datePeriod!!
                Text(text = "${datePeriod.from.format(formatter)} - ${datePeriod.to.format(formatter)}")
            } else {
                Text(text = "Select date")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Filled.DateRange, "daterange")
        }
    }
    if (calendarDialogVisible.value) {
        CalendarDialog(
            state = rememberUseCaseState(
                visible = true,
                onCloseRequest = {
                    calendarDialogVisible.value = false
                }
            ),
            config = CalendarConfig(
                yearSelection = false, monthSelection = true, style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Period { date1, date2 ->
                viewModel.filter.datePeriod = DatePeriod(date1, date2)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Categories(viewModel: MainViewModel) {
    viewModel.fetchCategories()
    Column(Modifier.fillMaxWidth()) {
        Text(text = "Categories:", style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(viewModel.categoriesList.value!!) {
                val textChipRememberOneState = remember { mutableStateOf(viewModel.filter.category.contains(it)) }
                FilterChip(selected = textChipRememberOneState.value, onClick = {
                    textChipRememberOneState.value = !textChipRememberOneState.value
                    viewModel.filter.category.apply {
                        if (textChipRememberOneState.value) {
                            add(it)
                        } else {
                            remove(it)
                        }
                    }
                }, label = {
                    Text(text = it.ifEmpty { "No category" })
                }, modifier = Modifier.padding(end = 5.dp))
            }
        }
    }
}

@Composable
private fun InputFields(
    viewModel: MainViewModel
) {
    val nameFilter = remember { mutableStateOf(viewModel.filter.name.orEmpty())}
    val descriptionFilter = remember { mutableStateOf(viewModel.filter.description.orEmpty()) }
    OutlinedTextField(
        value = nameFilter.value,
        onValueChange = {
            nameFilter.value = it
            viewModel.filter.name = it.ifEmpty { null }
        },
        label = { Text(text = "Name") })
    OutlinedTextField(
        value = descriptionFilter.value,
        onValueChange = {
            descriptionFilter.value = it
            viewModel.filter.description = it.ifEmpty { null }
        },
        label = { Text(text = "Description") })
}


@Composable
private fun ModalTop() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            painterResource(id = R.drawable.baseline_filter_alt_24),
            contentDescription = "filter",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Filter",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}