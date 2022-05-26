package com.shihs.tripmood.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentLoginBinding
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.util.UserManager

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val serverClientId = getString(R.string.server_client_id)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val googleSignInBtn = binding.signInButton

        googleSignInBtn.apply {
            this.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            this.setSize(SignInButton.SIZE_WIDE)
        }

        viewModel.navigateToLoginSuccess.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    MobileNavigationDirections.actionGlobalNavigationHome()
                )
                viewModel.navigateToLoginSuccessEnd()
            }
        }

        binding.johnBtn.setOnClickListener {
            UserManager.userUID = "8787878787"
            UserManager.userName = "皮卡皮卡!"
            UserManager.userPhotoUrl = "https://browsecat.net/sites/default/files/the-professor-money-heist-wallpapers-87259-579206-1832331.png"
            findNavController().navigate(MobileNavigationDirections.actionGlobalNavigationHome())
        }

        Log.d("SS", "userToken ${UserManager.userUID}")

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity() as MainActivity).hideToolBar()
        (requireActivity() as MainActivity).hideBottomNavBar()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideToolBar()
        (requireActivity() as MainActivity).hideBottomNavBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showToolBar()
        (requireActivity() as MainActivity).showBottomNavBar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            val user = User(
                email = account.email,
                name = account.displayName,
                image = account.photoUrl.toString(),
                uid = account.id ?: ""
            )
            viewModel.checkUserExist(user)
        } catch (e: ApiException) {
            Log.w("SS", "Sign-in failed", e)
        }
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
}
