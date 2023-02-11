package com.orels.jeruchess.main.data

import com.orels.jeruchess.main.domain.model.ClubData
import database.ClubDataEntity

fun ClubDataEntity.toClubData() = ClubData(
    address = address,
    phone = phone,
    email = email,
    website = website,
    facebook = facebook,
    instagram = instagram,
    twitter = twitter
)