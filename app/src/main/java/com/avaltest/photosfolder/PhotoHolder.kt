package `com`.avaltest.photosfolder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.util.Log
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by chevil on 21/05/19.
 */


// this class extends the classical view holder
// to update the contents dynamically

class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ThumbnailView: ImageView = itemView.findViewById<ImageView>(R.id.thumbnail)
    private val TitleView: TextView = itemView.findViewById<TextView>(R.id.title)
 
    // update the view
    // with lazt loading from Picasso
    fun update(url: String, title : String) {
        TitleView.setText( title )
        // load one image and discard progress dialog
        // when one image at least is loaded
        Picasso.get().load(url).into(ThumbnailView, object : com.squareup.picasso.Callback {

                        override fun onSuccess() {
                          MainActivity.loadingDialog.dismiss()
                        }

                        override fun onError(e: Exception) {
                           Log.e( Constants.LOGTAG, "Couldn't load image : " + title )
                        }
                    })
    }
}
