package com.example.scorekeeper.components

import android.widget.Toast
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

class Dropdown<T>(
    private val defaultText: String,
    private val label: String,
    private val items: List<T>,
    private val onItemClicked: (index: Int, item: T) -> Unit,
    private val getItemText: (item: T) -> String = { item: T -> item.toString() },
    private val toastString: (item: T) -> String = { item -> getItemText(item) }
) {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Render() {
        val expanded = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableStateOf(0) }
        val context = LocalContext.current

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = {
                expanded.value = !expanded.value
            },
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = defaultText,
                onValueChange = {},
                label = { Text(text = label) },
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(onClick = {
                        selectedIndex.value = index
                        expanded.value = !expanded.value

                        Toast.makeText(
                            context,
                            toastString(item),
                            Toast.LENGTH_SHORT
                        ).show()

                        onItemClicked(index, item)
                    }) {
                        Text(getItemText(item))
                    }
                }
            }
        }
    }
}