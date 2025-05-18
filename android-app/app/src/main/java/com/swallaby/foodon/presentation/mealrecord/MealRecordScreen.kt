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
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimatable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.dropShadow
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil
import com.swallaby.foodon.core.util.ImageCropManager
import com.swallaby.foodon.core.util.ImageMetadataUtil
import com.swallaby.foodon.core.util.rememberThrottledFunction
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordEvent
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordUiState
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordViewModel
import org.threeten.bp.LocalDateTime
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
                    event.mealInfo.imageUri?.let {
                        val metadata = ImageMetadataUtil.getMetadataFromUri(context, it)
                        Log.d("MealRecordScreen", "metadata: $metadata")

                        when (val time = metadata?.getFormattedCaptureTime()) {
                            null -> {
                                Log.d("MealRecordScreen", "time is null")
                                editViewModel.updateMealTime(DateUtil.formatTimeToHHmm(LocalDateTime.now()))
                            }

                            else -> {
                                Log.d("MealRecordScreen", "time: $time")
                                editViewModel.updateMealTime(time.split(" ")[1])
                            }
                        }
                    }


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

    Scaffold { innerPadding ->
        Box(
            modifier = modifier.fillMaxSize()
//                .padding(innerPadding)
        ) {
            WithPermission(
                modifier = modifier, permission = Manifest.permission.CAMERA
            ) {
                CameraAppScreen(
                    modifier = modifier,
                    uiState = uiState,
                    onBackClick = onBackClick,
                    uploadMealImage = { uri, context ->
//                        ImageConverter.convertUriToWebP(
//                            context = context, imageUri = uri, quality = 10
//                        )
                        recordViewModel.uploadMealImage(
                            uri, context
                        )
                    },
                    onSearchClick = onSearchClick,
                    onCaptureClick = recordViewModel::capture,
                    onClearImageUploadFailMessage = recordViewModel::clearImageUploadFailMessage,
                    innerPadding = innerPadding
                )
            }
        }

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
    onClearImageUploadFailMessage: () -> Unit = {},
    onCaptureClick: () -> Unit = {},
    innerPadding: PaddingValues = PaddingValues(0.dp),
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
            onCaptureClick()
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


    val throttledCapture = rememberThrottledFunction(300) {
        onCaptureClick()
        Log.d("CAMERASCREEN", "currentTime = ${System.currentTimeMillis()}")
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
            context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        imageCaptureUseCase.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        Log.d("CAMERA", "Saved image to gallery: $uri")
                        selectedImageUri = uri // 찍은 사진의 URI 저장
                        uploadMealImage(uri, context)
                    }
                }
//                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                                outputFileResults.savedUri?.let { uri ->
//                                    Log.d("CAMERASCREEN", "Saved image to gallery: $uri")
//                                    selectedImageUri = uri // 찍은 사진의 URI 저장
//
//                                    // WebP로 변환
//                                    val webpFile = ImageConverter.convertUriToWebP(
//                                        context, uri, 85
//                                    ) // 품질 85로 설정
//
//                                    // WebP 파일을 사용하여 업로드
//                                    webpFile?.let { file ->
//                                        val webpUri = Uri.fromFile(file)
//                                        Log.d("CAMERASCREEN", "Converted to WebP: $webpUri")
//                                        uploadMealImage(webpUri, context) // WebP URI로 업로드
//                                    } ?: run {
//                                        Log.d("CAMERASCREEN", "Conversion to WebP failed")
//                                        // 변환 실패 시 원본 URI 사용
//                                        uploadMealImage(uri, context)
//                                    }
//                                }
//                            }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CAMERASCREEN", "Error saving image", exception)
                    // 에러 메시지 표시
                    Toast.makeText(
                        context, "사진 저장 실패: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // 상단 버튼 행
        Row(
            modifier = Modifier
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
                Icon(
                    painter = painterResource(
                        when (flashMode) {
                            ImageCapture.FLASH_MODE_OFF -> R.drawable.icon_flash_off
                            ImageCapture.FLASH_MODE_AUTO -> R.drawable.icon_flash_auto
                            else -> R.drawable.icon_flash_on
                        }
                    ), contentDescription = "flash"
                )
            }
        }

        // 카메라 프리뷰 또는 선택된/찍은 사진 표시
        when {
            selectedImageUri != null -> {
                ImagePreview(
                    imageUri = selectedImageUri, onClose = {
                        selectedImageUri = null
                        onClearImageUploadFailMessage()
                    }, contentDescription = "Selected Image"
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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            uiState.imageUploadFailMessage?.let {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(it),
                        style = NotoTypography.NotoBold16.copy(color = Color.Red),
                    )
                }
            }


            // 하단 액션 버튼 행
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 앨범 선택 버튼
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    ActionButton(
                        iconResId = R.drawable.icon_gallery,
                        text = stringResource(R.string.select_gallery),
                        onClick = { galleryLauncher.launch("image/*") })
                }

                // 촬영 버튼
                CaptureButton(
                    onClick = throttledCapture

                )

                // 음식 검색 버튼
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    ActionButton(
                        iconResId = R.drawable.icon_search,
                        text = stringResource(R.string.food_search),
                        onClick = onSearchClick
                    )
                }
            }
        }

    }
    when (uiState.mealRecordState) {
        is ResultState.Loading -> {
            val retrySignal = rememberLottieRetrySignal()
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.food_loading),
                onRetry = { failCount, exception ->
                    retrySignal.awaitRetry()
                    // Continue retrying. Return false to stop trying.
                    true
                })

            // 로딩 중 UI 표시
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = .5f))
                    .pointerInteropFilter { event ->
                        true
                    }, contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                )
            }
        }

        is ResultState.Success -> {

        }

        is ResultState.Error -> {

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
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    // remember로 상태 유지
    val previewUseCase = remember {
        androidx.camera.core.Preview.Builder().build()
    }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    // PreviewView를 remember로 생성
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    fun bindCameraUseCases() {
        cameraProvider?.let { provider ->
            try {
                // 기존 바인딩 해제
                provider.unbindAll()

                // 카메라 설정
                val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

                // 프리뷰와 이미지 캡처 사용 사례 바인딩
                val camera = provider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                )

                // 카메라 컨트롤 저장
                cameraControl = camera.cameraControl
            } catch (e: Exception) {
                Log.e("CameraPreview", "카메라 사용 사례 바인딩 실패", e)
            }
        }
    }

    // 카메라 프로바이더 초기화
    LaunchedEffect(Unit) {
        try {
            val provider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider = provider
            previewUseCase.surfaceProvider = previewView.surfaceProvider
            bindCameraUseCases()
        } catch (e: Exception) {
            Log.e("CameraPreview", "카메라 프로바이더 초기화 실패", e)
        }
    }

    // 렌즈 방향이 변경될 때 카메라 리바인딩
    LaunchedEffect(lensFacing) {
        bindCameraUseCases()
    }

    // 줌 레벨 변경 시 적용
    LaunchedEffect(zoomLevel) {
        cameraControl?.setLinearZoom(zoomLevel)
    }

    // 핵심: 컴포넌트 수명 주기와 리소스 해제 관리 개선
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    // 앱이 백그라운드로 갈 때 리소스 해제
                    previewUseCase.surfaceProvider = null
                    cameraProvider?.unbindAll()
                }

                Lifecycle.Event.ON_RESUME -> {
                    // 앱이 다시 포그라운드로 올 때 카메라 재설정
                    if (cameraProvider != null) {
                        previewUseCase.surfaceProvider = previewView.surfaceProvider
                        bindCameraUseCases()
                    }
                }

                else -> {}
            }
        }

        // 생명주기 옵저버 등록
        lifecycleOwner.lifecycle.addObserver(observer)

        // 컴포넌트가 제거될 때 확실히 리소스 해제
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // 중요: 명시적으로 서페이스 제공자 해제
            previewUseCase.surfaceProvider = null
            cameraProvider?.unbindAll()
            cameraProvider = null
            cameraControl = null
        }
    }



    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RectangleShape), // 1:1 비율에 맞게 clip
        factory = { previewView },
    )
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