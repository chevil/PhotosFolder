package `com`.avaltest.photosfolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder

/**
 * Created by chevil on 21/05/19.
 */

// this is the adapter for the list of photos
class PhotoAdapter( private val photos : List<Photo> ) : RecyclerView.Adapter<PhotoHolder>() {
 
    override fun getItemCount(): Int {
        return photos.size;
    }
 
    // update the view when necessary
    override public fun onBindViewHolder(holder: PhotoHolder, position: Int) : Unit {
        var thumbUrl = photos.get(position).thumbnailUrl
        var title = photos.get(position).title
        holder.update(thumbUrl, title)
    }
 
    override public fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        var photoItem = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoHolder(photoItem)
    }
}
