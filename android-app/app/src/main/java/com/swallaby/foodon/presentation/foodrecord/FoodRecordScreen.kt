package com.swallaby.foodon.presentation.foodrecord

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.dropShadow
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FoodRecordScreen(modifier: Modifier = Modifier) {
    WithPermission(
        modifier = modifier, permission = Manifest.permission.CAMERA
    ) {
        CameraAppScreen(modifier = modifier)
    }
}

// 재사용 가능한 이미지 프리뷰 컴포넌트
@Composable
fun ImagePreview(
    imageUri: Uri?,
    onClose: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // 닫기 버튼 추가
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(36.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_close),
                contentDescription = "Close Preview",
                tint = Color.White
            )
        }
    }
}

// 재사용 가능한 하단 아이콘 버튼 컴포넌트
@Composable
fun ActionButton(
    iconResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BG100, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = text
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text,
            style = NotoTypography.NotoMedium13.copy(color = G750)
        )
    }
}

// 캡처 버튼 컴포넌트
@Composable
fun CaptureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .border(
                4.dp, Color(0xff417FF7), CircleShape
            )
            .dropShadow(
                shape = CircleShape,
                color = Color(0xFF2073E4).copy(alpha = .32f),
                blur = 8.dp,
                offsetY = 0.dp
            )
            .background(
                color = MainWhite, shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}

@Composable
fun CameraAppScreen(modifier: Modifier = Modifier) {
    // 찍은 사진의 URI를 저장할 변수
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 선택된 이미지 URI를 저장할 변수
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 갤러리 런처 설정
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Log.d("GALLERY", "Selected image: $it")
        }
    }

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var zoomLevel by remember { mutableFloatStateOf(0.0f) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }
    val imageCaptureUseCase = remember {
        ImageCapture.Builder().setFlashMode(flashMode).build()
    }

    val localContext = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 버튼 행
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.icon_close), contentDescription = "close")
            }
            IconButton(onClick = {
                // 플래시 모드 토글 (OFF -> AUTO -> ON -> OFF)
                flashMode = when (flashMode) {
                    ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_AUTO
                    ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_ON
                    else -> ImageCapture.FLASH_MODE_OFF
                }
                // 플래시 모드 변경 적용
                imageCaptureUseCase.flashMode = flashMode
            }) {
                Icon(painter = painterResource(R.drawable.icon_flash), contentDescription = "flash")
            }
        }

        // 카메라 프리뷰 또는 선택된/찍은 사진 표시
        when {
            capturedImageUri != null -> {
                ImagePreview(
                    imageUri = capturedImageUri,
                    onClose = { capturedImageUri = null },
                    contentDescription = "Captured Image"
                )
            }
            selectedImageUri != null -> {
                ImagePreview(
                    imageUri = selectedImageUri,
                    onClose = { selectedImageUri = null },
                    contentDescription = "Selected Image"
                )
            }
            else -> {
                CameraPreview(
                    lensFacing = lensFacing,
                    zoomLevel = zoomLevel,
                    imageCaptureUseCase = imageCaptureUseCase
                )
            }
        }

        // 하단 액션 버튼 행
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 앨범 선택 버튼
            Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
                ActionButton(
                    iconResId = R.drawable.icon_gallery,
                    text = stringResource(R.string.select_gallery),
                    onClick = { galleryLauncher.launch("image/*") }
                )
            }

            // 촬영 버튼
            CaptureButton(
                onClick = {
                    // 현재 시간을 파일명에 포함시켜 겹치지 않게 함
                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val filename = "FOODON_$timestamp.jpg"

                    // 갤러리에 저장될 파일 생성
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/FoodOn")
                        }
                    }

                    val outputOptions = ImageCapture.OutputFileOptions.Builder(
                        localContext.contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    ).build()

                    imageCaptureUseCase.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(localContext),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                outputFileResults.savedUri?.let { uri ->
                                    Log.d("CAMERA", "Saved image to gallery: $uri")
                                    capturedImageUri = uri // 찍은 사진의 URI 저장
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("CAMERA", "Error saving image", exception)
                                // 에러 메시지 표시
                                Toast.makeText(
                                    localContext,
                                    "사진 저장 실패: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            )

            // 음식 검색 버튼
            Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
                ActionButton(
                    iconResId = R.drawable.icon_search,
                    text = stringResource(R.string.food_search),
                    onClick = { /* 음식 검색 기능 구현 */ }
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int,
    zoomLevel: Float,
    imageCaptureUseCase: ImageCapture
) {
    val previewUseCase = remember { androidx.camera.core.Preview.Builder().build() }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    val localContext = LocalContext.current

    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                localContext as LifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
            )
            cameraControl = camera.cameraControl
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = ProcessCameraProvider.awaitInstance(localContext)
        rebindCameraProvider()
    }

    LaunchedEffect(lensFacing) {
        rebindCameraProvider()
    }

    LaunchedEffect(zoomLevel) {
        cameraControl?.setLinearZoom(zoomLevel)
    }

    // 컴포넌트가 화면에서 사라질 때 카메라 리소스 해제
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            cameraProvider = null
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RectangleShape), // 1:1 비율에 맞게 clip
        factory = { context ->
            PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
                previewUseCase.surfaceProvider = it.surfaceProvider
                rebindCameraProvider()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraAppScreen()
}