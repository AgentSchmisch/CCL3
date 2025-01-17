package at.florianschmid.fridgeventory.ui.add

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import at.florianschmid.fridgeventory.CameraActivity
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.Item
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import at.florianschmid.fridgeventory.ui.theme.Typography


@Composable
fun ItemAddScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemAddViewModel = hiltViewModel(),
    onSave: () -> Unit,
) {
    val item = viewModel.addUiState.item
    ItemAddForm(item, modifier, onValueChange = { itemsChanged ->
        viewModel.updateItem(itemsChanged)
    }, onSaveButtonClicked={
        viewModel.saveItem()
        onSave()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAddForm(
    item: Item,
    modifier: Modifier = Modifier,
    onValueChange: (Item) -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


    // Registering the activity result for camera
    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data?.getStringExtra("image_uri")

            if (imageUri != null) {
                // Update the image path in the ViewModel
                item.image_path = imageUri
            }
        }
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
    ){
        Column (Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Row(
            horizontalArrangement = Arrangement.Center, // Centers the items in the Row
            modifier = Modifier.fillMaxWidth() // Ensures the Row fills the available width
        ) {
            Text("Add a new item to your fridge", style=Typography.titleLarge)
        }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = item.name,
                    label = { Text("Name") },
                    onValueChange = { newText ->
                        onValueChange(item.copy(name = newText))
                    })
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = item.additional,
                    label = { Text("Description") },
                    onValueChange = { newText ->
                        onValueChange(item.copy(additional = newText))
                    })
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
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
                CameraButton(resultLauncher)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
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
}

@Composable
fun CameraButton(resultLauncher: ActivityResultLauncher<Intent>) {
    val context = LocalContext.current
    IconButton(onClick = {startCamera(context, resultLauncher)}) {
        Icon(
            painter = painterResource(R.drawable.baseline_camera_alt_24),
            contentDescription = "Take Picture"
        )
    }
}

fun startCamera(context: Context, resultLauncher: ActivityResultLauncher<Intent>){
        val intent = Intent(context, CameraActivity::class.java)

        resultLauncher.launch(intent)
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}



@Composable
@Preview
fun ItemAddScreenPreview() {
    ItemAddForm(Item(0, "", LocalDate.now().atStartOfDay(), 5,"",""))
}