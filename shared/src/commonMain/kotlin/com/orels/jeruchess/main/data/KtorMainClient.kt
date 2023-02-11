package com.orels.jeruchess.main.data

import com.orels.jeruchess.main.domain.data.MainClient
import com.orels.jeruchess.main.domain.model.ClubData

class KtorMainClient : MainClient {

    override suspend fun addClub(clubData: ClubData) = Unit
    override suspend fun updateClub(clubData: ClubData) = Unit
    override suspend fun deleteClub(clubData: ClubData) = Unit

    override fun getClub(): ClubData =
        ClubData(
            address = "מועדון ירושחמט",
            phone = "",
            email = "",
            website = "http://www.jeruchess.com/",
            facebook = "https://www.facebook.com/jeruchess.chess",
            instagram = "",
            twitter = ""
        )


}