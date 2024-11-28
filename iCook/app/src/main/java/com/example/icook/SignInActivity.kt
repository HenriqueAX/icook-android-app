package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var errorName: TextView
    private lateinit var errorEmail: TextView
    private lateinit var errorPassword: TextView
    private lateinit var loginErrorMessage: TextView
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

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        dbHelper = DatabaseHelper(this)
        auth = FirebaseAuth.getInstance()

        // Configuração para o login com Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializa os campos de entrada e botões
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        errorName = findViewById(R.id.errorName)
        errorEmail = findViewById(R.id.errorEmail)
        errorPassword = findViewById(R.id.errorPassword)
        loginErrorMessage = findViewById(R.id.loginErrorMessage)
        loginButtonsContainer = findViewById(R.id.loginButtonsContainer)
        registerButtonsContainer = findViewById(R.id.registerButtonsContainer)
        googleButtonContainer = findViewById(R.id.googleButtonContainer)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonSubmitRegister = findViewById(R.id.buttonSubmitRegister)
        buttonBack = findViewById(R.id.buttonBack)
        signInButton = findViewById(R.id.signInButton)
        textInfo = findViewById(R.id.textInfo)

        // Configura os botões
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

        // Adiciona TextWatchers para os campos de entrada
        setupTextWatchers()
    }

    private fun switchToRegisterMode() {
        isRegisterMode = true
        editTextName.visibility = View.VISIBLE
        loginErrorMessage.visibility = View.GONE // Oculta mensagem de erro do login
        loginButtonsContainer.visibility = View.GONE
        registerButtonsContainer.visibility = View.VISIBLE
        googleButtonContainer.visibility = View.GONE
        textInfo.text = "Preencha os dados para cadastrar-se"
    }

    private fun switchToLoginMode() {
        isRegisterMode = false
        editTextName.visibility = View.GONE
        loginButtonsContainer.visibility = View.VISIBLE
        registerButtonsContainer.visibility = View.GONE
        googleButtonContainer.visibility = View.VISIBLE
        textInfo.text = "Faça login ou cadastre-se"

        // Limpa apenas mensagens de erro, mas mantém os campos preenchidos, caso necessário
        hideValidationError(editTextName, errorName)
        hideValidationError(editTextEmail, errorEmail)
        hideValidationError(editTextPassword, errorPassword)
        loginErrorMessage.visibility = View.GONE
    }


    private fun performLogin() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            loginErrorMessage.text = "E-mail ou senha inválidos"
            loginErrorMessage.visibility = View.VISIBLE
            return
        }

        val user = dbHelper.getUserByEmail(email)
        if (user != null && user.password == password) {
            loginErrorMessage.visibility = View.GONE
            Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
            navigateToMainActivity(user.name)
        } else {
            loginErrorMessage.text = "E-mail ou senha incorretos"
            loginErrorMessage.visibility = View.VISIBLE
        }
    }

    private fun performRegistration() {
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        var isValid = true

        if (name.isEmpty()) {
            showValidationError(editTextName, errorName, "O nome não pode estar vazio")
            isValid = false
        } else {
            hideValidationError(editTextName, errorName)
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showValidationError(editTextEmail, errorEmail, "E-mail inválido")
            isValid = false
        } else {
            hideValidationError(editTextEmail, errorEmail)
        }

        if (password.length < 5) {
            showValidationError(editTextPassword, errorPassword, "A senha deve ter no mínimo 5 caracteres")
            isValid = false
        } else {
            hideValidationError(editTextPassword, errorPassword)
        }

        if (!isValid) return

        val newUser = User(0, name, email, password)
        val isAdded = dbHelper.addUser(newUser)

        if (isAdded) {
            Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

            // Retorna para o modo de login com os dados preenchidos
            switchToLoginMode()
            editTextEmail.setText(email) // Preenche o campo de e-mail
            editTextPassword.setText(password) // Preenche o campo de senha
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário. E-mail já registrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showValidationError(field: EditText, errorView: TextView, message: String) {
        val redColor = ContextCompat.getColor(this, android.R.color.holo_red_dark)
        field.backgroundTintList = ColorStateList.valueOf(redColor) // Define o contorno vermelho
        errorView.text = message
        errorView.visibility = View.VISIBLE
    }

    private fun hideValidationError(field: EditText, errorView: TextView) {
        val defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray)
        field.backgroundTintList = ColorStateList.valueOf(defaultColor) // Restaura a cor padrão
        errorView.visibility = View.GONE
    }

    private fun setupTextWatchers() {
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) hideValidationError(editTextName, errorName)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    hideValidationError(editTextEmail, errorEmail)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && s.length >= 5) {
                    hideValidationError(editTextPassword, errorPassword)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun clearFields() {
        // Limpa campos de texto e mensagens de erro
        editTextName.text.clear()
        editTextEmail.text.clear()
        editTextPassword.text.clear()
        hideValidationError(editTextName, errorName)
        hideValidationError(editTextEmail, errorEmail)
        hideValidationError(editTextPassword, errorPassword)
        loginErrorMessage.visibility = View.GONE
    }

    private fun signInWithGoogle() {
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
                Toast.makeText(this, "Falha no login com Google: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Falha na autenticação com Google", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
        finish()
    }
}
