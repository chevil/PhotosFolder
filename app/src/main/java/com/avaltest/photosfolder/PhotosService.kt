package `com`.avaltest.photosfolder

import retrofit2.Call;
import retrofit2.http.GET;

// rxJava
import io.reactivex.Observable


/**
 * Created by chevil on 21/05/19.
 */

public interface PhotosService {

    @GET("photos")
    fun getPhotos() : Observable<List<Photo>>
}
