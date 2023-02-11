package com.orels.jeruchess.main.domain.data

import com.orels.jeruchess.main.domain.model.ClubData

interface MainClient {
    suspend fun addClub(clubData: ClubData)
    suspend fun updateClub(clubData: ClubData)
    suspend fun deleteClub(clubData: ClubData)
    fun getClub() : ClubData?
}