package com.azueromarket.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.azueromarket.databinding.ActivityLoginBinding
import com.azueromarket.model.TipoUsuario
import com.azueromarket.ui.home.HomeClienteActivity
import com.azueromarket.ui.home.HomeEmprendedorActivity
import com.azueromarket.ui.register.RegisterActivity
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        if (session.isLoggedIn()) { navigateToHome(session.getTipoUsuario()); return }

        binding.btnLogin.setOnClickListener { performLogin() }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            Snackbar.make(binding.root, "Contáctanos vía WhatsApp: +507 6000-0000", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val pass  = binding.etPassword.text.toString()

        binding.tilEmail.error    = null
        binding.tilPassword.error = null

        if (email.isEmpty()) { binding.tilEmail.error    = "Ingresa tu correo"; return }
        if (pass.isEmpty())  { binding.tilPassword.error = "Ingresa tu contraseña"; return }

        setLoading(true)

        binding.root.postDelayed({
            val usuario = MockDataSource.autenticar(email, pass)
            setLoading(false)
            if (usuario != null) {
                session.saveSession(usuario.id, usuario.nombre, usuario.email,
                    usuario.tipoUsuario, usuario.telefono, usuario.ubicacion)
                navigateToHome(usuario.tipoUsuario)
            } else {
                Snackbar.make(binding.root, "Correo o contraseña incorrectos", Snackbar.LENGTH_LONG).show()
            }
        }, 700)
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
    }

    private fun navigateToHome(tipo: TipoUsuario) {
        val intent = when (tipo) {
            TipoUsuario.EMPRENDEDOR -> Intent(this, HomeEmprendedorActivity::class.java)
            TipoUsuario.CLIENTE     -> Intent(this, HomeClienteActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
