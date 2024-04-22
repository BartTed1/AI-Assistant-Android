package xyz.teodorowicz.assistant.services

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
import xyz.teodorowicz.assistant.R
import xyz.teodorowicz.assistant.models.User
import xyz.teodorowicz.assistant.views.LoginActivity
import xyz.teodorowicz.assistant.views.MainActivity

class AuthService(private val activity: ComponentActivity) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var signInLauncher: ActivityResultLauncher<Intent>
    val sharedPreferencesService = SharedPreferencesService(activity)

    fun initSignInLauncher() {
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
                val token = firebaseUser.getIdToken(true).await().token
                activity.runOnUiThread { Log.i("AuthService:Token", idToken ?: "") }
                val user = User.fromFirebaseUser(firebaseUser)
                val userService = UserService(user, activity)
                val registered = withContext(Dispatchers.IO) { userService.register(activity, token).await() }

                if (registered is Exception)  activity.runOnUiThread { throw registered }
                else if (registered is Boolean && registered) {
                    sharedPreferencesService.saveString("token", token ?: "")
                    sharedPreferencesService.saveString("uid", user.uid)
                    sharedPreferencesService.saveInt("tokenIssuedAt", ((System.currentTimeMillis() - 10) / 1000).toInt())
                    sharedPreferencesService.saveString("userFirstName", user.firstName)
                    sharedPreferencesService.saveString("userLastName", user.lastName)
                    sharedPreferencesService.saveString("userEmail", user.email)
                    sharedPreferencesService.saveString("userPhotoUrl", user.photoUrl)
                    sharedPreferencesService.saveString("refreshToken", idToken)

                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                } else {
                    activity.runOnUiThread { Toast.makeText(activity, "Nie udało się zarejestrować użytkownika", Toast.LENGTH_LONG).show() }
                }
            } else {
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            if (e is FirebaseNetworkException) {
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                return
            }
            e.printStackTrace()
        }
    }

    @Deprecated("Use getToken() instead")
    private suspend fun getGoogleTokenFromIdToken(): String? {
        val refreshToken = sharedPreferencesService.getString("refreshToken")
        if (refreshToken == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            return null
        }
        val credential = GoogleAuthProvider.getCredential(refreshToken, null)
        try {
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            if (firebaseUser == null) {
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                return null
            }
            val token = firebaseUser.getIdToken(true).await().token
            sharedPreferencesService.saveString("token", token ?: "")
            sharedPreferencesService.saveInt("tokenIssuedAt", ((System.currentTimeMillis() - 10) / 1000).toInt())
            return token
        } catch (e: Exception) {
            if (e is FirebaseNetworkException) {
                Toast.makeText(activity, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                return null
            }
            e.printStackTrace()
            return null
        }
    }

    private suspend fun refreshToken(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            return null
        }
        val token = currentUser.getIdToken(true).await().token
        sharedPreferencesService.saveString("token", token ?: "")
        sharedPreferencesService.saveInt("tokenIssuedAt", ((System.currentTimeMillis() - 10) / 1000).toInt())
        return token
    }

    suspend fun getToken(): String? {
        val tokenIssuedAt = sharedPreferencesService.getInt("tokenIssuedAt")
        if (tokenIssuedAt + 3600 > (System.currentTimeMillis() / 1000)) return sharedPreferencesService.getString("token")
        return refreshToken()

    }
}