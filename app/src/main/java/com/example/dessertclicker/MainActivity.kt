package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.ui.DessertViewModel
import com.example.dessertclicker.ui.theme.DessertClickerTheme


// declare a TAG constant in your file whose value will not change
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    // The onCreate() method is where you should do any one-time initializations for your activity.
    // onCreate() when the system creates the app.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The tag is a string that lets you more easily find your log messages in the Logcat.
        // The tag is typically the name of the class.
        Log.d(TAG, "onCreate Called")
        setContent {
            DessertClickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    DessertClickerApp(desserts = Datasource.dessertList)
                    DessertClickerApp()
                }
            }
        }
    }

    //  Unlike onCreate(), which is called only once to initialize your activity, onStart()
    //  can be called by the system many times in the lifecycle of your activity.
    //  onStart() makes the app visible on the screen, but the user is not yet
    //  able to interact with it.
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    // onResume() brings the app to the foreground, and the user is now able to interact with it.
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    // onPause() and onStop() are called when the app goes into the background
    // Whatever code runs in onPause() blocks other things from displaying,
    // so keep the code in onPause() lightweight.
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

/**
 * Determine which dessert to show.
 */
//fun determineDessertToShow(
//    desserts: List<Dessert>,
//    dessertsSold: Int
//): Dessert {
//    var dessertToShow = desserts.first()
//    for (dessert in desserts) {
//        if (dessertsSold >= dessert.startProductionAmount) {
//            dessertToShow = dessert
//        } else {
//            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
//            // you'll start producing more expensive desserts as determined by startProductionAmount
//            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
//            // than the amount sold.
//            break
//        }
//    }
//
//    return dessertToShow
//}

/**
 * Share desserts sold information using ACTION_SEND intent
 */

private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    try {
        ContextCompat.startActivity(intentContext, shareIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
private fun DessertClickerApp(
    // 6. pass the ViewModel instance to the UI–that is, from the DessertViewModel to the
    // DessertClickerApp() in the MainActivity.kt file
    viewModel: DessertViewModel = viewModel()
//    desserts: List<Dessert>
) {
    // 7.  use the ViewModel instance to access the uiState using collectAsState().
    val uiState by viewModel.uiState.collectAsState()

    //    var revenue by rememberSaveable { mutableStateOf(0) }
//    var dessertsSold by rememberSaveable { mutableStateOf(0) }
//    val currentDessertIndex by rememberSaveable { mutableStateOf(0) }
//    var currentDessertPrice by rememberSaveable {
//        mutableStateOf(desserts[currentDessertIndex].price)
//    }
//    var currentDessertImageId by rememberSaveable {
//        mutableStateOf(desserts[currentDessertIndex].imageId)
//    }

    DessertClickerApp(
        uiState = uiState,
        onDessertClicked = viewModel::onDessertClicked
    )
}

@Composable
private fun DessertClickerApp(
//    desserts: List<Dessert>
    // 8. Add uiState and onDessertClicked as parameters to the DessertClickerApp() composable function
    uiState: DessertUiState,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            val intentContext = LocalContext.current
            DessertClickerAppBar(
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext = intentContext,
//                        dessertsSold = dessertsSold,
//                        revenue = revenue
                        // 9.Pass the dessertsSold and revenue from the uiState instead of originals
                        dessertsSold = uiState.dessertsSold,
                        revenue = uiState.revenue
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { contentPadding ->
        DessertClickerScreen(
//            revenue = revenue,
//            dessertsSold = dessertsSold,
//            dessertImageId = currentDessertImageId,
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = onDessertClicked,
//            {

//                // Update the revenue
//                revenue += currentDessertPrice
//                dessertsSold++
//
//                // Show the next dessert
//                val dessertToShow = determineDessertToShow(desserts, dessertsSold)
//                currentDessertImageId = dessertToShow.imageId
//                currentDessertPrice = dessertToShow.price
//            },
            modifier = Modifier.padding(contentPadding)
        )
    }
}



@Composable
private fun DessertClickerAppBar(
    onShareButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_medium)),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.image_size))
                        .height(dimensionResource(R.dimen.image_size))
                        .align(Alignment.Center)
                        .clickable { onDessertClicked() },
                    contentScale = ContentScale.Crop,
                )
            }
            TransactionInfo(
                revenue = revenue,
                dessertsSold = dessertsSold,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}

@Composable
private fun TransactionInfo(
    revenue: Int,
    dessertsSold: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DessertsSoldInfo(
            dessertsSold = dessertsSold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
        RevenueInfo(
            revenue = revenue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
private fun RevenueInfo(revenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun DessertsSoldInfo(dessertsSold: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dessert_sold),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = dessertsSold.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
//        DessertClickerApp(listOf(Dessert(R.drawable.cupcake, 5, 0)))
        DessertClickerApp()
    }
}
