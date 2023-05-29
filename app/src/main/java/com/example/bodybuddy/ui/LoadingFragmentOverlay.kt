package com.example.bodybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentLoadingOverlayBinding
import com.example.bodybuddy.databinding.FragmentLoginBinding
import com.example.bodybuddy.ui.auth.login.LoginViewModel
import com.example.bodybuddy.ui.auth.validator.ResultListener

class LoadingFragmentOverlay : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var resultListener: ResultListener? = null

    private var _binding: FragmentLoadingOverlayBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        private const val TAG = "LoadingFragmentOverlay"
    }
}