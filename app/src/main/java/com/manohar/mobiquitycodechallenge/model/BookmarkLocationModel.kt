package com.manohar.mobiquitycodechallenge.model

import java.io.Serializable

class BookmarkLocationModel(
    val locationName: String,
    val locationAddress: String,
    val locationLatitude: String,
    val locationLongitude: String
) : Serializable
