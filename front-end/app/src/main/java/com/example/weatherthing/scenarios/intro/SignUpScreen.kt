package com.example.weatherthing.scenarios.intro

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.weatherthing.R
import com.example.weatherthing.utils.App
import com.example.weatherthing.viewModel.StartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreen(
    viewModel: StartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController,
    weatherCode: Int
) {
    val weatherList: List<String> = listOf<String>("맑음", "비", "눈", "천둥번개")
    val (nickname, setNickname) = remember {
        mutableStateOf("")
    }
    var selectedAge by remember {
        mutableStateOf(20)
    }
    val weatherString: String by remember {
        mutableStateOf(weatherList[weatherCode])
    }

    val genderOptions = mapOf("남" to 1, "여" to 0)
    var expanded by remember { mutableStateOf(false) }
    var expandedAge by remember { mutableStateOf(false) }
    var gender by remember {
        mutableStateOf("남")
    }
    val genderKey by remember { mutableStateOf(genderOptions[gender]) }
    var imgUri: Uri? by remember {
        mutableStateOf(null)
    }

    val photoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imgUri = uri
        }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(modifier = Modifier.weight(1f),onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(15.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = "Profile",
                fontSize = 15.sp,
                modifier = Modifier.weight(6f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.weight(1f)) {
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(20.dp))
            Box(
                modifier = Modifier
                    .size(106.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_img_round),
                    contentDescription = "이미지 추가",
                    modifier = Modifier.size(106.dp)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp, 100.dp).clickable(
                            enabled = true,
                            onClickLabel = "Clickable image",
                            onClick = { photoLauncher.launch("image/*") }
                        ),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (imgUri != null) {
                        Image(
                            painter =
                            rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = imgUri)
                                    .build()
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = "상품 사진",
                            modifier = Modifier
                                .clickable(
                                    enabled = true,
                                    onClickLabel = "Clickable image",
                                    onClick = { photoLauncher.launch("image/*") }
                                )
                                .size(100.dp)
                                .clip(CircleShape)
                                .aspectRatio(1f)
                                .padding(end = 5.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.add_img_round),
                            contentDescription = "이미지 추가",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.add_img),
                        contentDescription = "이미지 추가",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nickname",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(7.dp),
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    value = nickname,
                    onValueChange = setNickname,
                    modifier = Modifier.padding(0.dp)
                        .height(50.dp).padding(0.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large.copy(CornerSize(20.dp)),
                    placeholder = {

                        Text(
                            text = "nickname",
                            style = TextStyle(
                                fontSize = 13.sp,
                                textDecoration = TextDecoration.None
                            ),
                            modifier = Modifier.padding(0.dp)
                        )
                    },
                    maxLines = 1,
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        backgroundColor = colorResource(id = R.color.backgray)
                    ),
                    textStyle = TextStyle(fontSize = 13.sp, textDecoration = TextDecoration.None)

                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Weather",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(7.dp),
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    value = weatherString,
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.padding(0.dp)
                        .height(50.dp).padding(0.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large.copy(CornerSize(20.dp)),
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = weatherString,
                            style = TextStyle(
                                fontSize = 13.sp,
                                textDecoration = TextDecoration.None
                            )
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        backgroundColor = colorResource(id = R.color.backgray)
                    ),
                    textStyle = TextStyle(fontSize = 13.sp, textDecoration = TextDecoration.None)

                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Gender",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(7.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
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
                    modifier = Modifier.padding(0.dp)
                        .height(50.dp).padding(0.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large.copy(CornerSize(20.dp)),
                    maxLines = 1,
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        backgroundColor = colorResource(id = R.color.backgray)
                    ),
                    textStyle = TextStyle(fontSize = 13.sp, textDecoration = TextDecoration.None)

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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Age",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(7.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
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
                    modifier = Modifier.padding(0.dp)
                        .height(50.dp).padding(0.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large.copy(CornerSize(20.dp)),
                    maxLines = 1,
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        backgroundColor = colorResource(id = R.color.backgray)
                    ),
                    textStyle = TextStyle(fontSize = 13.sp, textDecoration = TextDecoration.None)

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
                if (nickname != "" && imgUri != null) {
                    viewModel.sign(
                        nickname,
                        selectedAge,
                        gender = genderKey ?: 0,
                        weatherCode,
                        imgUri
                    )
                } else if (nickname == "") {
                    Toast.makeText(App.context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(App.context, "사진을 추가해주세요", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "가입하기")
            }
        }
    }
}
