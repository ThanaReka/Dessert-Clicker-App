package com.example.dessertclicker.data

import androidx.annotation.DrawableRes
import com.example.dessertclicker.data.Datasource.dessertList

// 1. create a model class for state UI to hold all the variables
data class DessertUiState(
    val currentDessertIndex: Int = 0,
    val dessertsSold: Int = 0,
    val revenue: Int = 0,
    val currentDessertPrice: Int = dessertList[currentDessertIndex].price,
    @DrawableRes val currentDessertImageId: Int = dessertList[currentDessertIndex].imageId
)
