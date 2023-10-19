package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskyCenterAlignedTopAppBar(
    title: String,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String?,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight(600),
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                )
            }
        },
        actions = actions,
        colors = colors,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TaskyCenterAlignedTopAppBar() {
    TaskyCenterAlignedTopAppBar(
        title = stringResource(id = android.R.string.untitled),
        navigationIcon = TaskyIcons.Close,
        navigationIconContentDescription = "Navigation icon",
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = TaskyIcons.EditOutlined,
                    contentDescription = "Action icon",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = White,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = White
        ),
    )
}