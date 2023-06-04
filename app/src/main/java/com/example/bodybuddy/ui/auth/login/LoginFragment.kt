package com.example.bodybuddy.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentLoginBinding
import com.example.bodybuddy.ui.LoadingFragmentOverlay
import com.example.bodybuddy.ui.auth.AuthViewModelFactory
import com.example.bodybuddy.ui.auth.login.reset.EmailResetOverlayFragment
import com.example.bodybuddy.ui.auth.validator.ResultListener
import com.example.bodybuddy.util.afterTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "CALLED")

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Get Google Sign-In account
                val account = task.getResult(ApiException::class.java)

                // Authenticate with Firebase using the Google credential
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Google Sign-In successful, handle the authenticated user
                            Toast.makeText(
                                requireContext(),
                                "SUCCESS LOGIN",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Do further processing with the authenticated user

                            requireActivity().finish()

                            findNavController().navigate(
                                R.id.action_loginFragment_to_userProfileInput
                            )

                            Log.d(TAG, "success")
                        } else {
                            Log.e(TAG, "ERROR not success")
                        }
                    }
            } catch (e: ApiException) {
                Log.e(TAG, "ERROR not success ${e.message}")
            }
        }
    }

    private fun googleLogin() {
        try {
            // Configure Google Sign-In
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("573988797765-8746qj4p4tcvstmp0a3idt8m6cblf1tq.apps.googleusercontent.com")
                .requestEmail()
                .build()

            // Create a GoogleSignInClient
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

            // Launch the Google Sign-In intent
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } catch (e: Throwable) {
            // Handle the exception
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoogleLogin.setOnClickListener {
            googleLogin()
        }

        binding.textViewForgotPassword.setOnClickListener {
            val email = "khalid.rizki22@gmail.com" // Replace with the user's email address

            val dialog = EmailResetOverlayFragment()

            dialog.show(parentFragmentManager, "reset_password_overlay")
        }

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
            val dialog = LoadingFragmentOverlay()
            dialog.show(parentFragmentManager, "loading_login_overlay")

            val handler = Handler()
            handler.postDelayed({
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

                    requireActivity().finish()

                    findNavController().navigate(
                        R.id.action_loginFragment_to_userProfileInput
                    )
                }

                dialog.dismiss()
            }, 2000) // Delay in milliseconds (2 seconds = 2000 milliseconds)

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
        private const val TAG = "LoginFragment"
        const val LOGIN_RESULT_OK = 200
        const val LOGIN_RESULT_ONGOING = 220
        const val RC_SIGN_IN = 9001
    }
}