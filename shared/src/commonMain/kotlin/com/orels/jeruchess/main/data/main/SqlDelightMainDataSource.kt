package com.orels.jeruchess.main.data.main

import com.orels.jeruchess.core.util.CommonFlow
import com.orels.jeruchess.core.util.toCommonFlow
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.main.domain.model.ClubData
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.map

class SqlDelightMainDataSource(
    db: JeruChessDatabase
): MainDataSource {

    private val queries = db.jeruchessQueries

    override suspend fun addClub(clubData: ClubData) = queries
        .transaction {
            queries.insertClub(
                address = clubData.address,
                phone = clubData.phone,
                email = clubData.email,
                website = clubData.website,
                facebook = clubData.facebook,
                instagram = clubData.instagram,
                twitter = clubData.twitter
            )
        }

    override suspend fun updateClub(clubData: ClubData) = queries
        .transaction {
            queries.updateClub(
                address = clubData.address,
                phone = clubData.phone,
                email = clubData.email,
                website = clubData.website,
                facebook = clubData.facebook,
                instagram = clubData.instagram,
                twitter = clubData.twitter
            )
        }
    override suspend fun deleteClub(clubData: ClubData) = queries
        .transaction {
            queries.deleteClub(clubData.address)
        }

    override fun getClub(clubData: ClubData): CommonFlow<ClubData?> = queries
        .getClub()
        .asFlow()
        .mapToOneOrNull()
        .map { clubEntity -> clubEntity?.toClubData() }
        .toCommonFlow()
}