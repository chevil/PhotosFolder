package `com`.avaltest.photosfolder

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater

class ProgressDialog {
companion object {
    fun progressDialog(context: Context): Dialog{
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.progress, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        return dialog
    }
  }
}
