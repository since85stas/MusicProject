package stas.batura.musicproject.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import stas.batura.musicproject.R
import stas.batura.musicproject.ui.playlist.PlaylistFragment

class DeleteAlertDialog : DialogFragment () {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Важное сообщение!")
                .setMessage("Покормите кота!")
                .setIcon(R.drawable.image396168)
                .setPositiveButton("Ok") {
                        dialog, id ->  (targetFragment as PlaylistFragment).deletePlaylistOk()
                }
                .setNegativeButton("Cancel") {
                    dialog, which -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}