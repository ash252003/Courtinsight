package com.example.courtinsight.nav

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageHistory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.model.DrawerItem
import com.example.courtinsight.view.common.About
import com.example.courtinsight.view.common.ActExplanation
import com.example.courtinsight.view.common.Analytics
import com.example.courtinsight.view.common.EditProfile
import com.example.courtinsight.view.common.Feedback
import com.example.courtinsight.view.common.Home
import com.example.courtinsight.view.common.PostDescription
import com.example.courtinsight.view.common.Settings
import com.example.courtinsight.view.common.UserType
import com.example.courtinsight.view.court.ManagePost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNavigation(rootNavController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val userItem = listOf(
        DrawerItem("Home", "home", Icons.Default.Home),
        DrawerItem("Analytics", "analytics", Icons.Default.Analytics),
        DrawerItem("Act Explanation", "act_explanation", Icons.Default.Description),
        DrawerItem("Setting", "setting", Icons.Default.Settings)
    )
    val courtItem = listOf(
        DrawerItem("Home", "home", Icons.Default.Home),
        DrawerItem("Manage Post", "manage_post", Icons.Default.ManageHistory),
        DrawerItem("Analytics", "analytics", Icons.Default.Analytics),
        DrawerItem("Act Explanation", "act_explanation", Icons.Default.Description),
        DrawerItem("Setting", "setting", Icons.Default.Settings)
    )
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val userType = sharedPreferences.getString("user_type", null)
    val name = sharedPreferences.getString("name", null)
    val item = if (userType == "court") courtItem else userItem
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Column(modifier = Modifier.padding(16.dp,  top = 0.dp, bottom = 8.dp)){
                        Text(userType.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(name.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    item.forEach { item ->
                        NavigationDrawerItem(
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route){
                                    popUpTo("user_home")
                                    launchSingleTop = true
                                }
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = "Menu Item")
                            },
                        )
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CourtInsight") },
                    navigationIcon = {
                        IconButton(onClick =  {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                                else {
                                    drawerState.close()
                                }
                            }
                        }){
                            Icon(Icons.Default.Menu, contentDescription = "Menu"
                                , tint = colorResource(R.color.white))
                        }
                    },
                    colors = topAppBarColors(
                        containerColor = colorResource(R.color.app_main),
                        titleContentColor = colorResource(R.color.white)
                    )
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("home") {
                    Home(navController)
                }
                composable("act_explanation") {
                    ActExplanation()
                }
                composable("analytics") {
                    Analytics()
                }
                composable("manage_post") {
                    ManagePost(navController)
                }
                composable("setting") {
                    Settings(navController, rootNavController)
                }
                composable("feedback") {
                    Feedback(navController)
                }
                composable("about") {
                    About()
                }
                composable("post_description/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    if (id != null) {
                        PostDescription(id)
                    }
                }
                composable("edit_profile/{email}") {
                    val email = it.arguments?.getString("email")
                    if (email != null) {
                        EditProfile(email, navController)
                    }
                }
                composable("user_type") {
                    UserType(rootNavController)
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun UserNavigationPreview(){
    val navController = rememberNavController()
    UserNavigation(navController)
}