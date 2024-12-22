package at.florianschmid.fridgeventory.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun TaskAddScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskAddViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSave: () -> Unit
) {
    val task = viewModel.addUiState.item
    TaskAddForm(task, modifier, onValueChange = { taskChanged ->
        viewModel.updateTask(taskChanged)
    }) {
        viewModel.saveTask()
        onSave()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddForm(
    item: Item,
    modifier: Modifier = Modifier,
    onValueChange: (Item) -> Unit = {},
    onSaveButtonClicked: () -> Unit = {}
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
    )
    {
        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                OutlinedTextField(
                    value = item.name,
                    label = { Text("Name") },
                    onValueChange = { newText ->
                        onValueChange(item.copy(name = newText))
                    })
            }
            Row {
                OutlinedTextField(
                    value = item.additional,
                    label = { Text("Description") },
                    onValueChange = { newText ->
                        onValueChange(item.copy(additional = newText))
                    })
            }
            Row {
                // the modal for datetime selection is shown here
                OutlinedTextField(
                    value = selectedDate,
                    label = { Text("Expiry Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    onValueChange = {  }
                )
                if (showDatePicker) {
                    Popup(
                        onDismissRequest = { showDatePicker = false },
                        alignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 64.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                        }
                    }
                }
            }

            Button(onClick = {
                // set the expiry date to the selected date
                onValueChange(
                    item.copy(
                        // set the date to the start of the day
                        expiry_date = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            .atStartOfDay()
                    )
                )
                onSaveButtonClicked()
            })
            {
                Text("Put in my fridge")
            }
        }
    }
}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


@Preview
@Composable
private fun ContactEditPreview() {
    TaskAddForm(Item(234, "", LocalDateTime.of(2024,12,9,0,0), 5,"")) { }
}