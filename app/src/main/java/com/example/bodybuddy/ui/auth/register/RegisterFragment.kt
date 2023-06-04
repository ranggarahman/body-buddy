package com.example.bodybuddy.ui.auth.register

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentRegisterBinding
import com.example.bodybuddy.ui.LoadingFragmentOverlay
import com.example.bodybuddy.ui.auth.AuthViewModelFactory
import com.example.bodybuddy.ui.auth.validator.ResultListener
import com.example.bodybuddy.util.afterTextChanged

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private lateinit var registerViewModel: RegisterViewModel
    private var resultListener: ResultListener? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val name = binding.inputUsername
        val email = binding.inputEmail
        val password = binding.inputPassword
        val register = binding.btnRegister

        binding.textAlreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        registerViewModel =
            ViewModelProvider(this, AuthViewModelFactory(requireContext()))[RegisterViewModel::class.java]

        registerViewModel.registerFormState.observe(viewLifecycleOwner, Observer {
            val registerState = it ?: return@Observer

            register.isEnabled = registerState.isDataValid

            if(registerState.emailError != null){
                email.error = getString(registerState.emailError)
            }
            if(registerState.nameError != null){
                name.error = getString(registerState.nameError)
            }
        })

        registerViewModel.registerResult.observe(viewLifecycleOwner, Observer { event ->
            val registerResult = event.getContentIfNotHandled() ?: return@Observer

            resultListener?.onResult(REGISTER_RESULT_OK)

            val dialog = LoadingFragmentOverlay()
            dialog.show(parentFragmentManager, "loading_register_overlay")

            val handler = Handler()

            handler.postDelayed({

                if(registerResult.failedRegister != null){
                    Toast.makeText(
                        activity,
                        registerResult.failedRegister.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if(registerResult.successRegister != null){
                    Toast.makeText(
                        activity,
                        getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }

                dialog.dismiss()
            }, 2000)
        })

        name.afterTextChanged {
            registerViewModel.registerDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId){
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            name.text.toString(),
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            register.setOnClickListener {

                resultListener?.onResult(REGISTER_RESULT_ONGOING)

                try {
                    registerViewModel.register(
                        name.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "$e")
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ResultListener) {
            resultListener = context
        }
    }

    companion object{
        private const val TAG = "RegisterFragment"
        const val REGISTER_RESULT_OK = 235
        const val REGISTER_RESULT_ONGOING = 233
    }
}