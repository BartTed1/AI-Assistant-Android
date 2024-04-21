package xyz.teodorowicz.ai.services

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import xyz.teodorowicz.ai.R
import xyz.teodorowicz.ai.models.User
import xyz.teodorowicz.ai.views.LoginActivity
import xyz.teodorowicz.ai.views.MainActivity

class AuthService(private val activity: ComponentActivity) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var signInLauncher: ActivityResultLauncher<Intent>
    var isSignInLauncherInitialized = false

    init {
        initSignInLauncher()
    }

    private fun initSignInLauncher() {
        signInLauncher = activity
            .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                activity.lifecycleScope.launch {
                    handleGoogleLoginResult(result)
                }
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.google_auth_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private suspend fun handleGoogleLoginResult(result: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!)
            } else {
                Log.e("AuthService", "Google sign in failed")
            }
        } catch (e: ApiException) {
            Log.e("AuthService", "Google sign in failed", e)
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        try {
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                Log.i("AuthService", "signInWithCredential:success")
                Log.i("AuthService", "User: ${firebaseUser.displayName}")

                val user = User.fromFirebaseUser(firebaseUser)
                val registered = withContext(Dispatchers.IO) { user.register(activity).await() }

                if (registered is Exception)  activity.runOnUiThread { throw registered }
                else if (registered is Boolean && registered) {
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                } else {
                    activity.runOnUiThread {
                        Toast.makeText(activity, "Nie udało się zarejestrować użytkownika", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Log.w("FirstRunActivity", "signInWithCredential:failure")
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("FirstRunActivity", "signInWithCredential:failure", e)
            if (e is FirebaseNetworkException) {
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
            }
        }
    }
}