package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.douglasbruce.tasky.core.model.VisitorFilterType

@Composable
fun VisitorFilters(
    onClick: (VisitorFilterType) -> Unit,
    selectedFilterType: VisitorFilterType,
    modifier: Modifier = Modifier
) {
    val filterTypes = VisitorFilterType.values()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(filterTypes) {
            VisitorFilter(
                text = it.text.asString(),
                selected = selectedFilterType == it,
                onClick = { onClick(it) }
            )
        }
    }
}

@Preview
@Composable
fun VisitorFiltersPreview() {
    VisitorFilters(
        onClick = {},
        selectedFilterType = VisitorFilterType.ALL
    )
}