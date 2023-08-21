package ru.chiya.birthdatereminder.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ru.chiya.birthdatereminder.presentation.screen.info.InfoScreen
import ru.chiya.birthdatereminder.presentation.ui.theme.BirthdateReminderTheme

@AndroidEntryPoint
class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BirthdateReminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val notificationData = intent?.extras
                    val id = notificationData?.getInt("id")
                    id?.let {
                        InfoScreen(id = it)
                    } ?: run {
                        Text(text = "Something went wrong: Id is incorrect")
                    }
                }
            }
        }
    }
}
