package com.example.weatherthing.scenarios.intro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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
fun SignUpScreen(viewModel: StartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavHostController, weatherCode: Int) {
    val (nickname, setNickname) = remember {
        mutableStateOf("")
    }
    var selectedAge by remember {
        mutableStateOf(20)
    }

    val genderOptions = mapOf("남" to 1, "여" to 0)
    var expanded by remember { mutableStateOf(false) }
    var expandedAge by remember { mutableStateOf(false) }
    var gender by remember {
        mutableStateOf("남")
    }
    val genderKey by remember { mutableStateOf(genderOptions[gender]) }

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
            TextField(value = nickname, onValueChange = setNickname)
            Text(
                text = "성별",
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
                    value = gender,
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
                    genderOptions.keys.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }
            Text(
                text = "나이",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            ExposedDropdownMenuBox(
                expanded = expandedAge,
                onExpandedChange = {
                    expandedAge = !expandedAge
                }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedAge.toString(),
                    onValueChange = { },
                    trailingIcon = {
                        Icons.Filled.ArrowDownward
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedAge,
                    onDismissRequest = {
                        expandedAge = false
                    }
                ) {
                    (20..40).forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedAge = option
                                expandedAge = false
                            }
                        ) {
                            Text(text = option.toString())
                        }
                    }
                }
            }
            Button(onClick = {
                if (nickname != "") {
                    viewModel.sign(nickname, selectedAge, gender = genderKey ?: 0, weatherCode)
                }
            }) {}
        }
    }
}
