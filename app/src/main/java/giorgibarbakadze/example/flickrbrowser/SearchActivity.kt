package giorgibarbakadze.example.flickrbrowser

import android.os.Bundle
import android.util.Log

private const val TAG = "SearchActivity"
class SearchActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activateToolbar(true)
        Log.d(TAG, ".onCreate starts")
    }


}