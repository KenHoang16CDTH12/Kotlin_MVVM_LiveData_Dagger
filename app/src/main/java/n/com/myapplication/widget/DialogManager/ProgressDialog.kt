package n.com.myapplication.widget.DialogManager

import android.app.Dialog
import android.content.Context
import android.view.Window
import n.com.myapplication.R

class ProgressDialog(context: Context?) : Dialog(context) {

  init {
    initView()
  }

  private fun initView() {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.setContentView(R.layout.layout_progress_dialog)
    this.window.setBackgroundDrawableResource(android.R.color.transparent)
    this.setCancelable(false)
    this.setCanceledOnTouchOutside(false)
  }

  override fun show() {
    if (super.isShowing()) return
    super.show()
  }

  override fun dismiss() {
    if (!super.isShowing()) return
    super.dismiss()
  }

  override fun onBackPressed() {
    super.onBackPressed()
    dismiss()
  }

}