package com.douglasbruce.tasky.features.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.Green
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White

@Composable
internal fun TaskRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TaskScreen(
        onBackClick = onBackClick,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "10 October 2023")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = White,
                        ),
                    ) {
                        Icon(imageVector = TaskyIcons.Close, contentDescription = null)
                    }
                },
                actions = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    )
                )
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        Modifier
                            .padding(end = 12.dp)
                            .width(20.dp)
                            .height(20.dp)
                            .background(
                                color = Green,
                                shape = RoundedCornerShape(size = 2.dp),
                            ),
                    )
                    Text(
                        text = stringResource(R.string.task),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 19.2.sp,
                            fontWeight = FontWeight(600),
                            color = DarkGray,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = TaskyIcons.CircleOutlined,
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.new_task),
                        style = TextStyle(
                            fontSize = 26.sp,
                            lineHeight = 25.sp,
                            fontWeight = FontWeight(700),
                            color = Black,
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskPreview() {
    TaskyTheme {
        TaskScreen(
            onBackClick = {},
        )
    }
}