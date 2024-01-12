package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.data.DessertUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class DessertViewModel: ViewModel() {

    //2. Start by adding a _uiState property of the MutableStateFlow class
    private val _uiState = MutableStateFlow(DessertUiState())
    //3. Add a backing property to uiState named _uiState of type StateFlow<DessertUiState>.
    // _uiState is accessible and editable only within the DessertViewModel
    // asStateFlow() makes this mutable state flow a read-only state flow.
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    //4. Move the logic for declaring the variables from the MainActivity and replace with
    // the ones from State UI
    fun onDessertClicked() {
        _uiState.update { cupcakeUiState ->
            val dessertsSold = cupcakeUiState.dessertsSold + 1
            val nextDessertIndex = determineDessertToShow(dessertsSold)
            cupcakeUiState.copy(
                currentDessertIndex = nextDessertIndex,
                revenue = cupcakeUiState.revenue + cupcakeUiState.currentDessertPrice,
                dessertsSold = dessertsSold,
                currentDessertImageId = dessertList[nextDessertIndex].imageId,
                currentDessertPrice = dessertList[nextDessertIndex].price
            )
        }
    }



    //4. Move the function for determining which dessert to show from the MainActivity to the viewModel
    /**
     * Determine which dessert to show.
     */
    fun determineDessertToShow(
//        desserts: List<Dessert>,
        dessertsSold: Int
    ): //Dessert
    Int {
//        var dessertToShow = desserts.first()
        var dessertIndex = 0
//        for (dessert in desserts) {
//            if (dessertsSold >= dessert.startProductionAmount) {
//                dessertToShow = dessert
        for (index in dessertList.indices) {
            if (dessertsSold >= dessertList[index].startProductionAmount) {
                dessertIndex = index
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }
//        return dessertToShow
        return dessertIndex
    }

}