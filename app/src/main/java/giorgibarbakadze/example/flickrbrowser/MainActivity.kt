package giorgibarbakadze.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*


private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), GetRawData.OnDownloadCompete,
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener{
    private val flickrRecycleViewAdapter = FlickrRecycleViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)


        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickrRecycleViewAdapter

        val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne", "formula,drift", "en-us", true )
        val getRawData = GetRawData(this)
        getRawData.execute(url)
        Log.d(TAG, "onCreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: Starts")
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick starts")
        val photo = flickrRecycleViewAdapter.getPhoto(position)
        if (photo != null){
            val intent = Intent(this, PhotoDetails::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean): String{
      Log.d(TAG, ".createUri starts")

        return Uri.parse(baseURL).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if(matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if(status == DownloadStatus.OK){

            val getFlickerJsonData = GetFlickrJsonData(this)
            getFlickerJsonData.execute(data)

        } else {
            Log.d(TAG, "onDownloadComplete failed with status $status. Error message is: $data")
        }
    }


    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable: called")

        flickrRecycleViewAdapter.loadNewData(data)
        Log.d(TAG, ".onDataAvailable ends.")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, "onError: error message -> ${exception.message}")
    }
}