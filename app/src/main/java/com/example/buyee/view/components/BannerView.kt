package com.example.buyee.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier) {

    var bannerList by remember { mutableStateOf<List<String>>(emptyList()) }


    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners")
            .get()
            .addOnSuccessListener { doc ->
                val list = doc.get("urls") as? List<String>
                bannerList = list ?: emptyList()
            }
    }


    if (bannerList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        return
    }

    val pageState = rememberPagerState(
        initialPage = 0,
        pageCount = { bannerList.size }
    )

    Column(modifier = modifier) {

        HorizontalPager(
            state = pageState,
            pageSpacing = 16.dp
        ) { page ->

            AsyncImage(
                model = bannerList[page],
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (bannerList.size > 1) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DotsIndicator(
                    dotCount = bannerList.size,
                    type = ShiftIndicatorType(
                        DotGraphic(
                            color = MaterialTheme.colorScheme.primary,
                            size = 6.dp
                        )
                    ),
                    pagerState = pageState
                )
            }
        }
    }
}