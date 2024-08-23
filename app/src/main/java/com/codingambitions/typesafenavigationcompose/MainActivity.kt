package com.codingambitions.typesafenavigationcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codingambitions.typesafenavigationcompose.ui.theme.TypeSafeNavigationComposeTheme
import kotlinx.serialization.Serializable

@Serializable
object List

@Serializable
data class Detail(val id: String)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TypeSafeNavigationComposeTheme {

                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                    navController = navController, startDestination = List) {
                    composable<List> {
                        HomeScreen(onNavigateToDetail = { id ->
                            navController.navigate(Detail(id))
                        })
                    }
                    composable<Detail> { backStackEntry ->
                        //val id = backStackEntry.arguments?.getString("id").toString()
                        val detail: Detail = backStackEntry.toRoute()
                        val id = detail.id
                        DetailScreen(id) {
                            navController.navigateUp()
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun HomeScreen(
    onNavigateToDetail: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {


        val list = List(10) { index ->
            User(
                // Generate consecutive increasing numbers as the user id
                id = index,
                name = "User $index",
                profileDesc = "Profile description for User $index",
            )
        }
        items(count = list.size) { index ->
            val item = list[index]
            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToDetail(item.id.toString())
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user1),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Text("User ${item?.id.toString()}", fontSize = 20.sp)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(id: String, goBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    Text("User Detail")
                })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "User Detail with id $id")
        }
    }


}


data class User(
    val id: Int,
    val name: String,
    val profileDesc: String,
)