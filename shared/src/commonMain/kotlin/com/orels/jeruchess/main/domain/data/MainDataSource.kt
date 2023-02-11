package com.orels.jeruchess.main.domain.data

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.main.domain.model.ClubData

interface MainDataSource {
    suspend fun addClub(clubData: ClubData)
    suspend fun updateClub(clubData: ClubData)
    suspend fun deleteClub(clubData: ClubData)
    fun getClub(clubData: ClubData): CommonFlow<ClubData?>
}