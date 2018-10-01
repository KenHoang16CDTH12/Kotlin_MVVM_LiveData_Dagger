package n.com.myapplication.widget.DialogManager

import android.content.Context
import n.com.myapplication.extension.isNull
import n.com.myapplication.extension.notNull
import java.lang.ref.WeakReference

class DialogManagerImpl(context: Context) : DialogManager {

  private var context: WeakReference<Context> = WeakReference(context)

  private var progressDialog: ProgressDialog? = null

  override fun showProgressDialog() {
    progressDialog.isNull {
      progressDialog = ProgressDialog(context.get())
    }
    progressDialog?.show()
  }

  override fun hideProgressDialog() {
    progressDialog.isNull {
      progressDialog = ProgressDialog(context.get())
    }
    progressDialog?.dismiss()
  }

  override fun onRelease() {
    progressDialog.notNull {
      progressDialog = null
    }
  }

}