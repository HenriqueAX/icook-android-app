package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// Activity que gerencia login, cadastro e autenticação com Google
class SignInActivity : AppCompatActivity() {

<<<<<<< HEAD
    private lateinit var dbHelper: DatabaseHelper
=======
    private lateinit var auth: FirebaseAuth // Gerenciador de autenticação Firebase
    private lateinit var dbHelper: DatabaseHelper // Gerenciador do banco de dados local
>>>>>>> 815b497d3c1ac7c152725e0360087e48a26fe477

    // Componentes da interface do usuário
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var errorName: TextView
    private lateinit var errorEmail: TextView
    private lateinit var errorPassword: TextView
    private lateinit var loginButtonsContainer: LinearLayout
    private lateinit var registerButtonsContainer: LinearLayout
    private lateinit var googleButtonContainer: LinearLayout
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var buttonSubmitRegister: Button
    private lateinit var buttonBack: Button
    private lateinit var signInButton: Button
    private lateinit var textInfo: TextView

    private var isRegisterMode = false // Controla se está no modo de cadastro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

<<<<<<< HEAD
        dbHelper = DatabaseHelper(this)

        // Inicializa os campos de entrada e botões
=======
        // Inicializa FirebaseAuth e o banco de dados local
        auth = FirebaseAuth.getInstance()
        dbHelper = DatabaseHelper(this)

        // Liga os componentes da interface às variáveis
>>>>>>> 815b497d3c1ac7c152725e0360087e48a26fe477
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        errorName = findViewById(R.id.errorName)
        errorEmail = findViewById(R.id.errorEmail)
        errorPassword = findViewById(R.id.errorPassword)
        loginButtonsContainer = findViewById(R.id.loginButtonsContainer)
        registerButtonsContainer = findViewById(R.id.registerButtonsContainer)
        googleButtonContainer = findViewById(R.id.googleButtonContainer)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonSubmitRegister = findViewById(R.id.buttonSubmitRegister)
        buttonBack = findViewById(R.id.buttonBack)
        signInButton = findViewById(R.id.signInButton)
        textInfo = findViewById(R.id.textInfo)

        // Define o comportamento dos botões
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

    // Alterna para o modo de cadastro
    private fun switchToRegisterMode() {
        isRegisterMode = true
        editTextName.visibility = View.VISIBLE
        loginButtonsContainer.visibility = View.GONE
        registerButtonsContainer.visibility = View.VISIBLE
<<<<<<< HEAD
        googleButtonContainer.visibility = View.GONE
=======
        googleButtonContainer.visibility = View.GONE // Oculta o botão do Google no cadastro
>>>>>>> 815b497d3c1ac7c152725e0360087e48a26fe477
        textInfo.text = "Preencha os dados para cadastrar-se"
    }

    // Alterna para o modo de login
    private fun switchToLoginMode() {
        isRegisterMode = false
        editTextName.visibility = View.GONE
        loginButtonsContainer.visibility = View.VISIBLE
        registerButtonsContainer.visibility = View.GONE
<<<<<<< HEAD
        googleButtonContainer.visibility = View.VISIBLE
=======
        googleButtonContainer.visibility = View.VISIBLE // Mostra o botão do Google no login
>>>>>>> 815b497d3c1ac7c152725e0360087e48a26fe477
        textInfo.text = "Faça login ou cadastre-se"
    }

    // Realiza login local com e-mail e senha
    private fun performLogin() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showValidationError(editTextEmail, errorEmail, "Preencha todos os campos")
            showValidationError(editTextPassword, errorPassword, "Preencha todos os campos")
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

    // Realiza o cadastro de um novo usuário
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
            switchToLoginMode()
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário. E-mail já registrado.", Toast.LENGTH_SHORT).show()
        }
    }

<<<<<<< HEAD
    private fun showValidationError(field: EditText, errorView: TextView, message: String) {
        field.setBackgroundResource(R.drawable.error_border) // Destaque em vermelho
        errorView.text = message
        errorView.visibility = View.VISIBLE
    }

    private fun hideValidationError(field: EditText, errorView: TextView) {
        field.setBackgroundResource(0) // Remove o destaque
        errorView.visibility = View.GONE
    }

    private fun signInWithGoogle() {
        // Configuração para login com Google
=======
    // Realiza o login com Google
    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Gerencia o resultado do login com Google
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

    // Autenticação com Google via Firebase
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
>>>>>>> 815b497d3c1ac7c152725e0360087e48a26fe477
    }

    // Redireciona para a MainActivity após login bem-sucedido
    private fun navigateToMainActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
        finish()
    }
}
