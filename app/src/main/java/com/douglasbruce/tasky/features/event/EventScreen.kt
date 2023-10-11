package com.douglasbruce.tasky.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGreen
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White

@Composable
internal fun EventRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EventScreen(
        onBackClick = onBackClick,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "11 October 2023")
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
                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = White,
                        ),
                    ) {
                        Icon(imageVector = TaskyIcons.EditOutlined, contentDescription = null)
                    }
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(vertical = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Box(
                        Modifier
                            .padding(end = 16.dp)
                            .width(24.dp)
                            .height(24.dp)
                            .background(
                                color = LightGreen,
                                shape = RoundedCornerShape(size = 2.dp),
                            ),
                    )
                    Text(
                        text = stringResource(R.string.event),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 19.2.sp,
                            fontWeight = FontWeight(600),
                            color = DarkGray,
                        )
                    )
                }
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = TaskyIcons.CircleOutlined,
                            contentDescription = null,
                            tint = Black,
                        )
                    },
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.new_event),
                            style = TextStyle(
                                fontSize = 26.sp,
                                lineHeight = 25.sp,
                                fontWeight = FontWeight(700),
                                color = Black,
                            ),
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = TaskyIcons.ChevronRight,
                            contentDescription = null,
                            tint = Black,
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                Divider(color = LightBlue)
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.event_description),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight(400),
                                color = Black,
                            )
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = TaskyIcons.ChevronRight,
                            contentDescription = null,
                            tint = Black,
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = LightBlueVariant)
                        .clickable {  }
                ) {
                    Icon(
                        imageVector = TaskyIcons.Add,
                        contentDescription = null,
                        tint = Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.add_photos),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight(600),
                            color = Gray,
                        )
                    )
                }
                Divider(color = LightBlue)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }
                    ) {
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.from),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    ),
                                    modifier = Modifier.width(40.dp)
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = "08:00",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    )
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = TaskyIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = Black,
                                )
                            },
                            modifier = Modifier.clickable { }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Jul 21 2022",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    )
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = TaskyIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = Black,
                                )
                            },
                            modifier = Modifier.clickable { }
                        )
                    }
                }
                Divider(color = LightBlue)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }
                    ) {
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.to),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    ),
                                    modifier = Modifier.width(40.dp)
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = "08:00",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    ),
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = TaskyIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = Black,
                                )
                            },
                            modifier = Modifier.clickable { }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Jul 21 2022",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 15.sp,
                                        fontWeight = FontWeight(400),
                                        color = Black,
                                    )
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = TaskyIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = Black,
                                )
                            },
                            modifier = Modifier.clickable { }
                        )
                    }
                }
                Divider(color = LightBlue)
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = TaskyIcons.NotificationsOutlined,
                            contentDescription = null,
                            tint = Gray,
                            modifier = Modifier
                                .background(
                                    color = LightBlueVariant,
                                    shape = RoundedCornerShape(size = 5.dp)
                                )
                                .padding(4.dp)
                        )
                    },
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.thirty_minutes_before),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight(400),
                                color = Black,
                            )
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = TaskyIcons.ChevronRight,
                            contentDescription = null,
                            tint = Black,
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                Divider(color = LightBlue)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(R.string.delete_event),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight(600),
                            color = Gray,
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventPreview() {
    TaskyTheme {
        EventScreen(
            onBackClick = {},
        )
    }
}