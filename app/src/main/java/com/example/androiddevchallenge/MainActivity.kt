/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val pagerState = PagerState()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "puppyList") {
            composable("puppyList") { puppyGrid(navController, pagerState) }
            composable(
                "puppyDetail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                puppyDetailList(
                    pagerState, backStackEntry.arguments?.getInt("id") ?: 0
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun puppyGrid(navController: NavHostController, pagerState: PagerState) {
    val list = PuppyRepo.list
    LazyVerticalGrid(cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 1.dp, end = 1.dp),
        content = {
            items(list) {
                puppyGridItem(it) {
                    navController.navigate("puppyDetail/${it.id}") {
                        popUpTo("puppyList") {
                            pagerState.hasConsumeJump = false
                        }
                    }
                }
            }
        })
}

@Composable
fun puppyGridItem(puppy: Puppy, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .height(240.dp)
            .fillMaxWidth()
            .padding(1.dp, 0.dp, 1.dp, 2.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(puppy.imgRes),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "No.${puppy.id}",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start)
            )
            Text(
                text = "❤️:${puppy.viewCount}", modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End)
            )
        }
    }
}

@Composable
fun puppyDetailList(pagerState: PagerState, id: Int) {
    val list = PuppyRepo.list
    pagerState.maxPage = (list.size - 1).coerceAtLeast(0)
    Pager(state = pagerState) {
        if (!pagerState.hasConsumeJump) {
            pagerState.currentPage = (id - 1).coerceAtLeast(0)
            pagerState.hasConsumeJump = true
        }
        puppyDetail(list[page])
    }
}

@Composable
fun puppyDetail(puppy: Puppy) {
    val typography = MaterialTheme.typography
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(puppy.imgRes),
            contentDescription = null,
            modifier = Modifier
                .height(480.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))

        Text(
            "This is the puppy detail of No.${puppy.id}",
            style = typography.h6
        )
        Text(
            "❤️:${puppy.viewCount}",
            style = typography.body2
        )
        Text(
            "Telephone:...",
            style = typography.body2
        )
        Text(
            "Address:...",
            style = typography.body2
        )
    }
}

@ExperimentalFoundationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalFoundationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
