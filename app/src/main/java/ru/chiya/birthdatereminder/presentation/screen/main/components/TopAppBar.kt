package ru.chiya.birthdatereminder.presentation.screen.main.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import ru.chiya.birthdatereminder.R
import ru.chiya.birthdatereminder.presentation.screen.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: MainViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text("Birthdays")
        },
        actions = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                    contentDescription = "Filter"
                )
            }
        }
    )

    if (showDialog.value) {
        FilterModal(showDialog, viewModel)
    }
}
