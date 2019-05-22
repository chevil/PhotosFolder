package `com`.avaltest.photosfolder

import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.ObjectOutputStream
import java.io.ObjectInputStream

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.app.Dialog

// retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.OkHttpClient

// rxJava
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

// access views with their ids
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by chevil on 21/05/19.
 */

class MainActivity : AppCompatActivity() {

    companion object {
      lateinit var loadingDialog : Dialog
    }

    private var str:String = ""
    private lateinit var disposable : Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photosView.layoutManager = LinearLayoutManager(this)
        getPhotos()
    }

    fun getPhotos() {

      try {

        // show loading dialog 
        loadingDialog = ProgressDialog.progressDialog(this@MainActivity)
        loadingDialog.show()

        // build a specific client to debug requests
        var client : OkHttpClient = OkHttpClient.Builder()
                              .addInterceptor(HttpLoggingInterceptor().apply {
                                 level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                              })
                              .build();

        // build the retrofit service using a Gson converter
        // and a RxJava adapter to get result as an observable
        var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        // build the object to process the requests to the API
        var service = retrofit.create(PhotosService::class.java)

        // call the service on a new thread (not the UI thread )
        // but the result should be processed on the main UI thread
        // the result is then processed by handlePhotosList
        disposable = service.getPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlePhotosList, this::handleRxError)

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_retrofit ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't get photos list with retrofit", e )
      }

    }

    // This function handle the response from the retrofit call : a list of photos
    private fun handlePhotosList(photosList: List<Photo>) {
 
      try {
        // create an adapter with the received list
        photosView.adapter = PhotoAdapter( photosList );
        // free request data
        disposable.dispose()

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_display ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't show photos list", e )
        return
      }

      // save the photos list with serialization
      savePhotosList( photosList );
    }

    // This function is called when Rx returns an error,
    // so we use the cached data ( if any )
    private fun handleRxError(t: Throwable) {
      try {
        loadingDialog.dismiss()
        Toast.makeText(this, getString( R.string.error_live ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't get live data", t )
        var savedPhotos : List<Photo> = loadPhotosList()
        if ( savedPhotos.size >= 0 )
        {
           photosView.adapter = PhotoAdapter( savedPhotos );
        }
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't handle rx error", e )
      }
    }
 
    // This function save the photo list in a file
    private fun savePhotosList(photos: List<Photo>) {
     try {

        var directory : String = getApplicationContext().getFilesDir().toString();
        var outStream : FileOutputStream = FileOutputStream(directory + "/PhotosList.dat");
        var objectOutStream : ObjectOutputStream = ObjectOutputStream(outStream);
        objectOutStream.writeInt(photos.size); // Save size first
        Log.v( Constants.LOGTAG, "Saving " + photos.size + " photos" )
        for( p in photos)
        {
          objectOutStream.writeObject(p);
        }
        objectOutStream.close();
        Log.v( Constants.LOGTAG, "Saved photos list" )

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_save ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't save photos list", e )
      }
    }

    // This function restore the photo list from tha same file
    private fun loadPhotosList() : List<Photo> {
     var photos : List<Photo> = mutableListOf() 
     try {

        var directory : String = getApplicationContext().getFilesDir().toString();
        var inStream : FileInputStream = FileInputStream(directory + "/PhotosList.dat");
        var objectInStream : ObjectInputStream = ObjectInputStream(inStream);
        var count : Int = objectInStream.readInt(); // Get the number of photos
        Log.v( Constants.LOGTAG, "Loading " + count + " photos" )
        for (pi in count downTo 1)
        {
           val storedPhoto = objectInStream.readObject()
           when ( storedPhoto ) {
             is Photo -> photos += storedPhoto
             else -> Log.e( Constants.LOGTAG, "Corrupted data" );
           }
        }
        objectInStream.close();

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_load ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't load photos list", e )
      }
      return photos;
    }


}
