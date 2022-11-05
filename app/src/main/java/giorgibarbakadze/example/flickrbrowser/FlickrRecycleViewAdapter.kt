package giorgibarbakadze.example.flickrbrowser


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

private const val TAG = "RecyclerViewAdapter"

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var thumbNail: ImageView = view.findViewById(R.id.thubmnail)
    var title: TextView = view.findViewById(R.id.title)
}
class FlickrRecycleViewAdapter(private var photoList : List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //called by layout manager when it needs new vies
        Log.d(TAG, "onCreateViewHolder: new View requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //called by layout manager when t wants new data in an existing view
        Log.d(TAG, ".onBindViewHolder called")
        val photoItem = photoList[position]
//        Log.d(TAG, ".onBindViewHolder: ${photoItem.title} -> $position")
        Picasso.get().load(photoItem.image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbNail)

        holder.title.text = photoItem.title

    }

    override fun getItemCount(): Int {
//        Log.d(TAG, ".getItemCount: called")
        return if(photoList.isNotEmpty()) photoList.size else 0
    }

    fun loadNewData(newPhotos: List<Photo>){
        Log.d(TAG, ".loadNewData called")
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo?{
        return if(photoList.isNotEmpty()) photoList[position] else null
    }

}