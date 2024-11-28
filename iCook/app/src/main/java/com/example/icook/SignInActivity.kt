package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButtonsContainer: LinearLayout
    private lateinit var registerButtonsContainer: LinearLayout
    private lateinit var googleButtonContainer: LinearLayout
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var buttonSubmitRegister: Button
    private lateinit var buttonBack: Button
    private lateinit var signInButton: Button
    private lateinit var textInfo: TextView

    private var isRegisterMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        dbHelper = DatabaseHelper(this)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButtonsContainer = findViewById(R.id.loginButtonsContainer)
        registerButtonsContainer = findViewById(R.id.registerButtonsContainer)
        googleButtonContainer = findViewById(R.id.googleButtonContainer)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonSubmitRegister = findViewById(R.id.buttonSubmitRegister)
        buttonBack = findViewById(R.id.buttonBack)
        signInButton = findViewById(R.id.signInButton)
        textInfo = findViewById(R.id.textInfo)

        buttonLogin.setOnClickListener {
            if (isRegisterMode) {
                switchToLoginMode()
            } else {
                performLogin()
            }
        }

        buttonRegister.setOnClickListener {
            switchToRegisterMode()
        }

        buttonSubmitRegister.setOnClickListener {
            performRegistration()
        }

        buttonBack.setOnClickListener {
            switchToLoginMode()
        }

        signInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun switchToRegisterMode() {
        isRegisterMode = true
        editTextName.visibility = View.VISIBLE
        loginButtonsContainer.visibility = View.GONE
        registerButtonsContainer.visibility = View.VISIBLE
        googleButtonContainer.visibility = View.GONE // Oculta o botão do Google no modo de cadastro
        textInfo.text = "Preencha os dados para cadastrar-se"
    }

    private fun switchToLoginMode() {
        isRegisterMode = false
        editTextName.visibility = View.GONE
        loginButtonsContainer.visibility = View.VISIBLE
        registerButtonsContainer.visibility = View.GONE
        googleButtonContainer.visibility = View.VISIBLE // Mostra o botão do Google no modo de login
        textInfo.text = "Faça login ou cadastre-se"
    }

    private fun performLogin() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val user = dbHelper.getUserByEmail(email)
        if (user != null && user.password == password) {
            Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
            navigateToMainActivity(user.name)
        } else {
            Toast.makeText(this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performRegistration() {
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(0, name, email, password)
        val isAdded = dbHelper.addUser(newUser)

        if (isAdded) {
            Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
            switchToLoginMode()
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário. E-mail já registrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login com Google bem-sucedido", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity(auth.currentUser?.displayName ?: "Usuário")
                } else {
                    Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
