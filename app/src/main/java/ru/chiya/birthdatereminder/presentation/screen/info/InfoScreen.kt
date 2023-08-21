package ru.chiya.birthdatereminder.presentation.screen.info

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun InfoScreen(
    viewModel: InfoViewModel = hiltViewModel(), id: Int
) {

    val birthdayEntity by viewModel.birthdayEntity.observeAsState()

    LaunchedEffect(id) {
        viewModel.getBirthday(id)
    }
    val activity = LocalContext.current as? Activity

    // Display the details of the birthdayEntity
    if (birthdayEntity != null) {
        // Render UI using birthdayEntity data
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                RowItem(birthdayEntity!!.name, "Name")
                RowItem(birthdayEntity!!.description, "Description")
                RowItem(
                    SimpleDateFormat(
                        "d MMM yyyy", Locale.getDefault()
                    ).format(birthdayEntity!!.birthday), "Birthday date"
                )
                RowItem(birthdayEntity!!.category, "Category")
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { activity?.finish() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(text = "Close")
                }
            }
        }
    } else {
        // Handle the case when the birthday entity is not found
        Text(text = "Birthday not found")
    }
}

@Composable
private fun RowItem(value: String, key: String) {
    Text(text = key, style = MaterialTheme.typography.bodyMedium)
    Text(text = value, style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))
}