package ru.chiya.birthdatereminder.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.chiya.birthdatereminder.presentation.screen.main.components.BirthdayCard
import ru.chiya.birthdatereminder.presentation.screen.main.components.TopBar

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    val birthdaysList by viewModel.birthdaysList.observeAsState(emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.fetchBirthdays()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        },
        topBar = { TopBar(viewModel) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (birthdaysList.isNotEmpty()) {
                LazyColumn {
                    items(birthdaysList){
                        BirthdayCard(it, viewModel)
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                ) {
                    Text(text = "404", style = MaterialTheme.typography.headlineLarge)
                    Text(text = "Nothing found or the database is empty")
                }
            }
        }
    }
}

