package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.OutlinedTextFieldStyle
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.DecimalVisualTransformation
import com.swallaby.foodon.core.util.NumberFormatPattern


@Composable
fun NutritionTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String = "g",
    formatPattern: NumberFormatPattern = NumberFormatPattern.DOUBLE_THOUSAND_COMMA,
    isLastField: Boolean = true,
) {
    // 숫자 키보드 설정
    val numberKeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
    )

    // VisualTransformation 생성
    val numberFormatTransformation = remember(formatPattern) {
        if (formatPattern == NumberFormatPattern.DOUBLE_THOUSAND_COMMA) DecimalVisualTransformation()
        else DecimalVisualTransformation()
//        NumberFormatTransformation(formatPattern)
    }
    OutLineTextField(value = value,
        modifier = Modifier
            .height(44.dp)
            .width(140.dp),
        onValueChange = onValueChange,
        keyboardOptions = numberKeyboardOptions,
        keyboardActions = KeyboardActions(),
        visualTransformation = numberFormatTransformation,
        placeholder = {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = "0",
                style = SpoqaTypography.SpoqaMedium16.copy(
                    color = G500, textAlign = TextAlign.Right
                )
            )
        },
        suffix = {
            Box(modifier = modifier.padding(start = 2.dp)) {
                Text(unit, style = SpoqaTypography.SpoqaMedium16.copy(color = G900))
            }
        })

}

@Composable
fun OutLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SpoqaTypography.SpoqaMedium16.copy(
        color = G900, textAlign = TextAlign.Right
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
) {
    BaseTextField(
        value = value,
        modifier = modifier,
        placeholder = placeholder,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        suffix = suffix,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OutlinedTextFieldStyle.focusedBackgroundColor,
            unfocusedContainerColor = OutlinedTextFieldStyle.unfocusedBackgroundColor,
            disabledContainerColor = OutlinedTextFieldStyle.disabledBackgroundColor,
            focusedIndicatorColor = OutlinedTextFieldStyle.focusedBorderColor,
            unfocusedIndicatorColor = OutlinedTextFieldStyle.unfocusedBorderColor,
            disabledIndicatorColor = OutlinedTextFieldStyle.disabledBorderColor,
            cursorColor = OutlinedTextFieldStyle.focusedCursorColor,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(6.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    // 내부 상태를 사용하여 TextFieldValue 관리
    val internalTextFieldValue = remember {
        mutableStateOf(TextFieldValue(text = value))
    }

    // 포커스 상태 추적
    var hasFocused by remember { mutableStateOf(false) }

    // 외부 값이 변경되었을 때 선택 위치 유지하며 업데이트
    LaunchedEffect(value) {
        if (value != internalTextFieldValue.value.text) {
            // 현재 선택 위치 유지
            val currentSelection = internalTextFieldValue.value.selection

            // 새 텍스트가 현재 텍스트보다 짧아진 경우 선택 위치 조정
            val newSelection = if (value.length < internalTextFieldValue.value.text.length) {
                // 텍스트가 지워졌을 때 커서 위치 조정
                val cursorPos = minOf(currentSelection.start, value.length)
                TextRange(cursorPos, cursorPos)
            } else {
                // 텍스트가 추가됐을 때 선택 위치 유지 또는 조정
                currentSelection
            }

            internalTextFieldValue.value = TextFieldValue(
                text = value, selection = newSelection
            )
        }
    }

    BasicTextField(value = internalTextFieldValue.value,
        modifier = modifier.onFocusChanged { focusState ->
            // 처음 포커스를 받았을 때만 커서 위치를 끝으로 설정
            if (focusState.isFocused && !hasFocused) {
                internalTextFieldValue.value = TextFieldValue(
                    text = internalTextFieldValue.value.text,
                    selection = TextRange(internalTextFieldValue.value.text.length)
                )
                hasFocused = true
            } else if (!focusState.isFocused) {
                // 포커스가 해제되었을 때 hasFocused를 false로 리셋
                hasFocused = false
            }
        },
        onValueChange = { newValue ->
            internalTextFieldValue.value = newValue
            onValueChange(newValue.text)
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(value = internalTextFieldValue.value.text,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                prefix = prefix,
                suffix = suffix,
                supportingText = supportingText,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                contentPadding = PaddingValues(horizontal = 16.dp),
                interactionSource = interactionSource,
                colors = colors,
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled, isError, interactionSource, colors, shape
                    )
                })
        })
}