package com.example.icook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

// Activity responsável pelo processo de login com Google
class SignInActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001 // Código de requisição para o Sign-In
    }

    private lateinit var auth: FirebaseAuth // Instância do FirebaseAuth para autenticação

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in) // Define o layout da Activity

        // Inicializa a instância do FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Verifica se já existe um usuário logado
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuário já está logado, navega para a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a SignInActivity para evitar retorno com o botão "voltar"
        }

        // Referência ao botão de login com Google
        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            signIn() // Chama a função de login quando o botão é clicado
        }
    }

    // Função que inicia o processo de login com Google
    private fun signIn() {
        // Configura as opções de login do Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Solicita o ID Token
            .requestEmail() // Solicita o email do usuário
            .build()

        // Cria um cliente de GoogleSignIn com as opções configuradas
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent // Cria o Intent de login
        startActivityForResult(signInIntent, RC_SIGN_IN) // Inicia a Activity de login
    }

    // Método chamado quando a Activity de login retorna um resultado
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verifica se o resultado é do Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Tenta obter a conta do Google a partir do Intent
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!) // Autentica com o Firebase usando o ID Token
            } catch (e: ApiException) {
                // Exibe uma mensagem de erro se o login falhar
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função que autentica o usuário com o Firebase usando o ID Token do Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        // Cria uma credencial do GoogleAuthProvider com o ID Token
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        // Autentica com o Firebase usando a credencial
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticação bem-sucedida
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()

                    // Navega para a MainActivity após o login bem-sucedido
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Finaliza a SignInActivity para evitar retorno com o botão "voltar"
                } else {
                    // Autenticação falhou, exibe uma mensagem de erro
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
