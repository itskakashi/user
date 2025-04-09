package com.example.authenticationn.Presentation.Ui

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.authenticationn.Data.FireStoreDatabase.Models.User
import com.example.authenticationn.Data.FireStoreDatabase.managers.AnalyticsManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.BillingAndPaymentManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.CanesManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.OrderManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.UserManager
import com.example.authenticationn.Domain.FireBaseRepository
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController, viewModel: FireBaseViewModel, userId: String) {
    var isEditing by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<User?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedJarType by remember { mutableStateOf("Cold") }
    var selectedDeliveryTime by remember { mutableStateOf("Morning (8AM - 12PM)") }

    var isError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = userId) {
        isLoading = true
        viewModel.getUser(
            userId,
            onSuccess = {
                user = it
                isError = false
                isLoading=false
            },
            onFailure = {
                Log.d("getUser", "Error fetching user: ${it.message}")
//                isError = true
                isLoading=false
            }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Profile",
                            modifier = Modifier, textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Text(
                        if (isEditing) "Save" else "Edit",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                if (!isEditing) {
                                    isEditing = true
                                } else {
                                    if(user!=null){
                                        viewModel.updateUser(user!!,
                                            onSuccess = {
                                                isEditing = false
                                            },
                                            onFailure = {
                                                scope.launch {
                                                    Toast.makeText(context, "User Not Updated", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                    }


                                }
                            },
                        style = TextStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                }
            )
        },
        bottomBar = {
            bottomBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF9FAFB)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            if (isError) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("An error occurred. Please try again.")

                }
            }else{
                ProfileHeader(
                    isEditing = isEditing,
                    user = user,
                    isLoading = isLoading,
                    selectedImageUri = selectedImageUri,
                    bitmap = bitmap,
                    onUserChange = { user = it },
                    onImageChange = { selectedImageUri = it },
                    onBitmapChange = { bitmap = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                PreferencesSection(

                    selectedJarType = selectedJarType,
                    selectedDeliveryTime = selectedDeliveryTime,
                    onJarTypeChange = { selectedJarType = it },
                    onDeliveryTimeChange = { selectedDeliveryTime = it },
                    onClickSelectedJarType ={viewModel.updateUser(user?.copy( defaultJarSize =selectedJarType  ) ?: User() , onFailure = {}, onSuccess = {})} ,
                    onClickSelectedDeliveryTime = {viewModel.updateUser(user?.copy( preferredDeliveryTime =selectedDeliveryTime  ) ?: User() , onFailure = {}, onSuccess = {})}
                )
                Spacer(modifier = Modifier.height(24.dp))
                AccountSection(navController = navController)
                Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom
                LogOutButton(isEditing = isEditing) {
                    viewModel.logOut(onSuccess = {
                        navController.navigate(
                            route.signInScreen
                        )
                        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                    }, onFailure = {
                        Log.d("onLogout", "Error nologging user: ${it.message}")
                    })
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    isEditing: Boolean,
    user: User?,
    isLoading: Boolean,
    selectedImageUri: Uri?,
    bitmap: Bitmap?,
    onUserChange: (User) -> Unit,
    onImageChange: (Uri?) -> Unit,
    onBitmapChange: (Bitmap?) -> Unit
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageChange(uri)
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                onBitmapChange(MediaStore.Images.Media.getBitmap(context.contentResolver, uri))
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                onBitmapChange(ImageDecoder.decodeBitmap(source))
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.clickable {
            if (isEditing)
                launcher.launch("image/*")
        }) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id =R.drawable.logo ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // Show a loading indicator
            CircularProgressIndicator()
        } else {

            if (isEditing) {
                OutlinedTextField(
                    value = user?.name ?: "",
                    onValueChange = { if(user!=null) onUserChange(user.copy(name = it)) },
                    label = { Text("User Name") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {

                Text(
                    text = user?.name ?: "",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp,)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


            if (isEditing) {
                OutlinedTextField(
                    value = user?.email ?: "",
                    onValueChange = { if(user!=null) onUserChange(user.copy(email = it)) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                Text(
                    text = user?.email ?: "",
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isEditing) {
                OutlinedTextField(
                    value = user?.contactInfo ?: "",
                    onValueChange = { if(user!=null)onUserChange(user.copy(contactInfo = it)) },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                Text(
                    text = user?.contactInfo ?: "",
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isEditing) {
                OutlinedTextField(
                    value = user?.address ?: "",
                    onValueChange = { if(user!=null)onUserChange(user.copy(address = it)) },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                Text(
                    text = user?.address ?: "",
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }

    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    val user = User(
        userId = "123",
        userName = "Sarah Johnson",
        email = "sarah.johnson@email.com",
        contactInfo = "+1 (555) 123-4567",
        address = "123 Main Street, Apt 4B, New York, NY 10001"
    )
    ProfileHeader(
        isEditing = true,
        user = user,
        isLoading = false,
        selectedImageUri = null,
        bitmap = null,
        onUserChange = {},
        onImageChange = {},
        onBitmapChange = {}
    )
}

@Composable
fun PreferencesSection(
   onClickSelectedJarType: (String) -> Unit,
   onClickSelectedDeliveryTime: (String) -> Unit,
    selectedJarType: String,
    selectedDeliveryTime: String,
    onJarTypeChange: (String) -> Unit,
    onDeliveryTimeChange: (String) -> Unit
) {
    var showJarTypeDialog by remember { mutableStateOf(false) }
    var showDeliveryTimeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Preferences",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        PreferenceItem(
            icon = R.drawable.jar,
            label = "Default Jar Size",
            value = selectedJarType,
            onClick = {

                showJarTypeDialog = true

            }
        )

        if (showJarTypeDialog) {
            AlertDialog(
                onDismissRequest = { showJarTypeDialog = false },
                title = { Text(text = "Select Jar Type") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedJarType == "Cold",
                                onClick = { onJarTypeChange("Cold") },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MyColor.focussedIconColor
                                )
                            )
                            Text(text = "Cold")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedJarType == "Normal",
                                onClick = { onJarTypeChange("Normal") },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MyColor.focussedIconColor
                                )
                            )
                            Text(text = "Normal")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showJarTypeDialog = false }) {
                        Text("Confirm")
                        onClickSelectedJarType(selectedJarType)
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        PreferenceItem(
            icon = R.drawable.clockicon,
            label = "Preferred Delivery Time",
            value = selectedDeliveryTime,
            onClick = {

                showDeliveryTimeDialog = true

            }
        )
        if (showDeliveryTimeDialog) {
            AlertDialog(
                onDismissRequest = { showDeliveryTimeDialog = false },
                title = { Text("Select Delivery Time") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedDeliveryTime == "Morning (8AM - 12PM)",
                                onClick = { onDeliveryTimeChange("Morning (8AM - 12PM)") },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MyColor.focussedIconColor
                                )
                            )
                            Text(text = "Morning (8AM - 12PM)")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = selectedDeliveryTime == "Afternoon (12PM - 4PM)",
                                onClick = { onDeliveryTimeChange("Afternoon (12PM - 4PM)") },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MyColor.focussedIconColor
                                )
                            )
                            Text(text = "Afternoon (12PM - 4PM)")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedDeliveryTime == "Evening (4PM - 8PM)",
                                onClick = { onDeliveryTimeChange("Evening (4PM - 8PM)") },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MyColor.focussedIconColor
                                )
                            )
                            Text(text = "Evening (4PM - 8PM)")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDeliveryTimeDialog = false }) {
                        Text("Confirm")
                        onClickSelectedDeliveryTime(selectedDeliveryTime)
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))
        NotificationPreferenceItem(icon = R.drawable.notification, label = "Push Notifications")
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSection(navController: NavController) {
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Account",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        AccountItem(icon = R.drawable.lock, label = "Change Password", onClick = {
            showChangePasswordDialog = true
        })
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = {
                    showChangePasswordDialog = false
                    currentPassword = ""
                    newPassword = ""
                    confirmNewPassword = ""
                },
                title = { Text("Change Password") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmNewPassword,
                            onValueChange = { confirmNewPassword = it },
                            label = { Text("Confirm New Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newPassword == confirmNewPassword) {

                                showChangePasswordDialog = false
                                currentPassword = ""
                                newPassword = ""
                                confirmNewPassword = ""
                            } else {

                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showChangePasswordDialog = false
                        currentPassword = ""
                        newPassword = ""
                        confirmNewPassword = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        AccountItem(icon = R.drawable.security, label = "Privacy Policy", onClick = {
            // Handle Privacy Policy click
        })
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        AccountItem(icon = R.drawable.terms, label = "Terms of Service", onClick = {
            // Handle Terms of Service click
        })
    }
}

@Preview
@Composable
fun AccountSectionPreview() {
    // Create a dummy NavController for the preview
    AccountSection(navController = rememberNavController())
}

@Composable
fun LogOutButton(isEditing: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) MyColor.focussedIconColor else Color(0xFFEF4444))
    ) {
        Text(
            text = "Log Out",
            style = TextStyle(color = Color.White, fontSize = 16.sp)
        )
    }
}



@Composable
fun PreferenceItem(icon: Int, label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = TextStyle(fontSize = 16.sp))
        }
        Row {
            Text(text = value, style = TextStyle(fontSize = 16.sp), color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "More",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun NotificationPreferenceItem(icon: Int, label: String) {
    var isEnabled by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = TextStyle(fontSize = 16.sp))
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MyColor.focussedIconColor,
                checkedTrackColor = MyColor.focussedIconColor.copy(alpha = 0.6f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.6f)
            )
        )
    }
}


@Composable
fun AccountItem(icon: Int, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = TextStyle(fontSize = 16.sp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "More",
            tint = Color.Gray
        )
    }
}

@Preview
@Composable
fun UserProfileScreenPreview() {
    val repository=FireBaseRepository(OrderManager(),
        AnalyticsManager(), BillingAndPaymentManager(), UserManager(), CanesManager()
    )
    val vm=FireBaseViewModel(repository)
    // Create a dummy NavController for the preview
    UserProfileScreen(navController = rememberNavController(),viewModel =vm , userId = "lUuRjYQn5hYJb0P4gCjH")
}