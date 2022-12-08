package com.example.weatherthing.scenarios.intro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherthing.viewModel.StartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreen(viewModel: StartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavHostController) {
    val (name, setName) = remember {
        mutableStateOf("")
    }
    val genderOptions = mapOf<Int, String>(1 to "남", 0 to "여")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(genderOptions[1]) }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                title = { Text(text = "회원가입") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxWidth()) {
            Text(
                text = "당신의 정보를 입력해주세요",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "닉네임",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(value = name, onValueChange = setName)
            Text(
                text = "나이",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText!!,
                    onValueChange = { },
                    trailingIcon = {
                        Icons.Filled.ArrowDownward
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    genderOptions.values.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            Button(onClick = { viewModel.sign() }) {
            }
        }
    }
}
