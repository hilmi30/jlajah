package com.perumdajepara.jlajah.model

import hinl.kotlin.database.helper.Database
import hinl.kotlin.database.helper.Schema

@Database("bookmark")
class BookmarkModel (
    @Schema("id", true, true, true)
    var id: Int
)