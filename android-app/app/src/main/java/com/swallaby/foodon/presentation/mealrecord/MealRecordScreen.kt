package com.swallaby.foodon.presentation.mealrecord

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.dropShadow
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.ImageCropManager
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordEvent
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordUiState
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MealRecordScreen(
    modifier: Modifier = Modifier,
    recordViewModel: MealRecordViewModel,
    editViewModel: MealEditViewModel,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNavigateToMealDetail: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by recordViewModel.uiState.collectAsStateWithLifecycle()
    val cropManager = ImageCropManager(context)

    // 이벤트 수집
    LaunchedEffect(Unit) {
        recordViewModel.events.collect { event ->
            when (event) {
                is MealRecordEvent.NavigateToDetail -> {
                    // 이벤트에서 데이터 꺼내서 사용
                    editViewModel.initMeal(event.mealInfo)

                    val positions = event.mealInfo.mealItems.mapNotNull { mealItem ->
                        mealItem.positions.firstOrNull()
                    }

                    Log.d("MealRecordScreen", "image URI: ${event.mealInfo.imageUri.toString()}")
                    // 이미지 로드 및 크롭
                    cropManager.loadAndCropImage(
                        event.mealInfo.imageUri.toString(),
//                        "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740",
                        positions
                    ) {
                        // 모든 크롭 이미지가 준비됨
                        onNavigateToMealDetail()
                    }
                }

                is MealRecordEvent.ShowErrorMessage -> {
                    // 에러 시 UI 표시
                    Toast.makeText(
                        context, event.errorMessageRes, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    WithPermission(
        modifier = modifier, permission = Manifest.permission.CAMERA
    ) {
        CameraAppScreen(
            modifier = modifier,
            uiState = uiState,
            onBackClick = onBackClick,
            uploadMealImage = recordViewModel::uploadMealImage,
            onSearchClick = onSearchClick,
        )
    }
}

// 재사용 가능한 이미지 프리뷰 컴포넌트
@Composable
fun ImagePreview(
    imageUri: Uri?,
    onClose: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
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
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Box(
            modifier = modifier
                .size(40.dp)
                .background(BG100, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconResId), contentDescription = text
            )
        }
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text, style = NotoTypography.NotoMedium13.copy(color = G750)
        )
    }
}

// 캡처 버튼 컴포넌트
@Composable
fun CaptureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CameraAppScreen(
    modifier: Modifier = Modifier,
    uiState: MealRecordUiState,
    onBackClick: () -> Unit,
    uploadMealImage: (uri: Uri, context: Context) -> Unit,
    onSearchClick: () -> Unit,
) {
    val context = LocalContext.current

    // 선택된 이미지 URI를 저장할 변수
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 갤러리 런처 설정
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            uploadMealImage(it, context)
            Log.d("GALLERY", "Selected image: $it")
        }
    }

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var zoomLevel by remember { mutableFloatStateOf(0.0f) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }
    val imageCaptureUseCase = remember {
        ImageCapture.Builder().setFlashMode(flashMode).build()
    }

    when (uiState.mealRecordState) {
        is ResultState.Loading -> {
            // 로딩 중 UI 표시
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInteropFilter { event ->
                        true
                    }, contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ResultState.Success -> {
        }

        is ResultState.Error -> {

        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 버튼 행
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
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
                ActionButton(iconResId = R.drawable.icon_gallery,
                    text = stringResource(R.string.select_gallery),
                    onClick = { galleryLauncher.launch("image/*") })
            }

            // 촬영 버튼
            CaptureButton(onClick = {
                // 현재 시간을 파일명에 포함시켜 겹치지 않게 함
                val timestamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
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
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()

                imageCaptureUseCase.takePicture(outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri?.let { uri ->
                                Log.d("CAMERA", "Saved image to gallery: $uri")
                                selectedImageUri = uri // 찍은 사진의 URI 저장
                                uploadMealImage(uri, context)
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CAMERA", "Error saving image", exception)
                            // 에러 메시지 표시
                            Toast.makeText(
                                context, "사진 저장 실패: ${exception.message}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

            })

            // 음식 검색 버튼
            Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
                ActionButton(
                    iconResId = R.drawable.icon_search,
                    text = stringResource(R.string.food_search),
                    onClick = onSearchClick
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
    imageCaptureUseCase: ImageCapture,
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

    AndroidView(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RectangleShape), // 1:1 비율에 맞게 clip
        factory = { context ->
            PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
                previewUseCase.surfaceProvider = it.surfaceProvider
                rebindCameraProvider()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraAppScreen(
        uiState = MealRecordUiState(),
        onBackClick = {},
        uploadMealImage = { _, _ -> },
        onSearchClick = {},
    )
}