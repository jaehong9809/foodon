package com.swallaby.foodon.presentation.mealrecord

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun PermissionRequiredScreen(
    modifier: Modifier = Modifier,
    permission: String,
    canShowPermissionDialog: Boolean,
    onBackClick: () -> Unit = {},
    onPermissionRequested: () -> Unit,
) {
    val context = LocalContext.current

    val permissionDenied =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonBackTopBar(onBackClick = onBackClick)
            if (permission == android.Manifest.permission.CAMERA) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("카메라 권한이 없습니다.", style = NotoTypography.NotoMedium20.copy(color = G900))
                    Spacer(modifier = modifier.height(4.dp))

                    // 사용자가 "다시 묻지 않음"을 선택한 경우
                    if (permissionDenied && !canShowPermissionDialog) {
                        Text(
                            "'설정' > 'foodOn' > '권한' > '카메라'에서\n'항상 확인'으로 직접 변경해주세요.",
                            style = NotoTypography.NotoMedium16.copy(color = G900),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "'설정' > 'foodOn' > '권한' > '카메라'에서\n'항상 확인'으로 변경해주세요.",
                            style = NotoTypography.NotoMedium16.copy(color = G900),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = modifier.height(12.dp))

                    if (permission == android.Manifest.permission.CAMERA) {
                        // 권한 요청 다이얼로그를 표시할 수 있거나 아직 권한을 요청하지 않은 경우에만 버튼 표시
                        if (canShowPermissionDialog || !permissionDenied) {
                            Button(onClick = { onPermissionRequested() }) {
                                Text("카메라 권한 요청")
                            }
                        } else if (permissionDenied && !canShowPermissionDialog) {
                            // 사용자가 "다시 묻지 않음"을 선택한 경우 설정으로 이동하는 버튼 표시
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                intent.data = uri
                                context.startActivity(intent)
                            }) {
                                Text("앱 설정으로 이동")
                            }
                        }
                    }
                }
            }


        }
    }

}

@Composable
fun WithPermission(
    modifier: Modifier = Modifier,
    permission: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? androidx.activity.ComponentActivity

    var permissionGranted by remember {
        mutableStateOf(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    }

    var canShowPermissionDialog by remember {
        mutableStateOf(activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
        } ?: false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted

        if (!granted) {
            canShowPermissionDialog = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
            } ?: false
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionGranted =
                    context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
                canShowPermissionDialog = activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                } ?: false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (!permissionGranted) {
        PermissionRequiredScreen(
            permission = permission,
            canShowPermissionDialog = canShowPermissionDialog,
            onBackClick = onBackClick,
            onPermissionRequested = {
                launcher.launch(permission)
            },
        )
    } else {
        content()
    }
}