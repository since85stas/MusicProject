package stas.batura.musicproject.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.databinding.TextDialogFragmentBinding

class TextDialog (  private val mainModel: MainAcivityViewModel) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bindings: TextDialogFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.text_dialog_fragment,
            container,
            false
            )

        bindings.mainModel = mainModel

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}