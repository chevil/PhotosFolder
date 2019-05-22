package `com`.avaltest.photosfolder

import java.io.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Created by chevil Team on 21/05/19.
 * JSON to object mapping
 */
class Photo : Serializable {

    @SerializedName("albumId")
    var albumId: Long = 0

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("title")
    var title: String = ""

    @SerializedName("url")
    var url: String = ""

    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String = ""

}
