package com.pedrogomez.hnmob

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.db.HitTable

class DataHelper {

    companion object{
        val HITTABLE = HitTable(
            "objectId",
            "author",
            1000,
            "story_title",
            "story_url",
            "title",
            "url"
        )
        val HITSRESPONSE = HitsListResponse(
            listOf(
                HitResponse(
                    "objectId",
                    "author",
                    1000,
                    1000,
                    1000,
                    "1000",
                    "story_title",
                    "story_url",
                    "story_url",
                    "story_url",
                    "story_url",
                    "story_url",
                    "title",
                    "url"
                ),
                HitResponse(
                    "objectId1",
                    "author1",
                    10001,
                    10001,
                    10001,
                    "10001",
                    "story_title1",
                    "story_url1",
                    "story_url1",
                    "story_url1",
                    "story_url1",
                    "story_url1",
                    "title1",
                    "url1"
                )
            )
        )
        val HITSLIST = listOf(
            HITTABLE
        )
    }

}