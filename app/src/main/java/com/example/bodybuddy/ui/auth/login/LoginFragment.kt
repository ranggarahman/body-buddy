package com.example.bodybuddy.ui.auth.login

import android.content.Context
import android.os.Bundle
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
import com.example.bodybuddy.databinding.FragmentLoginBinding
import com.example.bodybuddy.ui.auth.AuthViewModelFactory
import com.example.bodybuddy.ui.auth.validator.ResultListener
import com.example.bodybuddy.util.afterTextChanged

class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private var resultListener: ResultListener? = null

    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = binding.inputUsername
        val password = binding.inputPassword
        val login = binding.btnLogin

        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(requireContext()))[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            //make sure observer handles once
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {event ->
            //make sure observer handles once
            val loginResult = event.getContentIfNotHandled() ?: return@Observer

            resultListener?.onResult(LOGIN_RESULT_OK)

            if (loginResult.failedLogin != null) {
                Toast.makeText(activity,
                    loginResult.failedLogin.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (loginResult.successLogin != null) {
                Toast.makeText(activity,
                    loginResult.successLogin.message,
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    R.id.action_loginFragment_to_userProfileInput
                )
                activity?.finish()
            }
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId){
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                resultListener?.onResult(LOGIN_RESULT_ONGOING)

                loginViewModel.login(
                    email.text.toString(),
                    password.text.toString()
                )
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
        const val LOGIN_RESULT_OK = 200
        const val LOGIN_RESULT_ONGOING = 220
    }
}