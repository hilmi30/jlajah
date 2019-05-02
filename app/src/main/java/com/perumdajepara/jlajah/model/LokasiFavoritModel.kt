package com.perumdajepara.jlajah.model

import com.perumdajepara.jlajah.util.ConstantVariable
import hinl.kotlin.database.helper.Database
import hinl.kotlin.database.helper.Schema

@Database(ConstantVariable.favorit)
data class LokasiFavoritModel (
    @Schema(generatedId = true, field = "id", autoIncrease = true, nonNullable = true) val id: Int? = 0,
    @Schema(field = "location_name") var locationName: String?,
    @Schema(field = "alamat") var alamat: String?,
    @Schema(field = "icon") var icon: String?,
    @Schema(field = "id_lokasi") var idLokasi: Int?
)