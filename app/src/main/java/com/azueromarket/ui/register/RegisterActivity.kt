package com.azueromarket.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.azueromarket.databinding.ActivityRegisterBinding
import com.azueromarket.model.TipoUsuario
import com.azueromarket.ui.home.HomeClienteActivity
import com.azueromarket.ui.home.HomeEmprendedorActivity
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var session: SessionManager
    private var tipoSeleccionado = TipoUsuario.CLIENTE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Crear Cuenta"
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupUserTypeToggle()
        binding.btnCreateAccount.setOnClickListener { performRegister() }
    }

    private fun setupUserTypeToggle() {
        // Default: Cliente
        binding.btnCliente.isChecked = true
        updateInfoCard(TipoUsuario.CLIENTE)

        binding.toggleUserType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            tipoSeleccionado = when (checkedId) {
                binding.btnEmprendedor.id -> TipoUsuario.EMPRENDEDOR
                else                      -> TipoUsuario.CLIENTE
            }
            updateInfoCard(tipoSeleccionado)
        }
    }

    private fun updateInfoCard(tipo: TipoUsuario) {
        if (tipo == TipoUsuario.EMPRENDEDOR) {
            binding.tvInfoTipo.text = "🌾 Como Emprendedor podrás publicar tus productos y llegar a clientes en toda Panamá. Recibes el 100% del precio que fijes — los cargos se aplican al comprador."
            binding.layoutEmprendedorFields.visibility = View.VISIBLE
        } else {
            binding.tvInfoTipo.text = "🛒 Como Cliente podrás explorar productos auténticos de Azuero, chatear directamente con productores y hacer pedidos desde la app."
            binding.layoutEmprendedorFields.visibility = View.GONE
        }
    }

    private fun performRegister() {
        val nombre          = binding.etNombre.text.toString().trim()
        val email           = binding.etEmail.text.toString().trim()
        val pass            = binding.etPassword.text.toString()
        val confirmPass     = binding.etConfirmPassword.text.toString()
        val telefono        = binding.etTelefono.text.toString().trim()
        val ubicacion       = binding.etUbicacion.text.toString().trim()

        // Clear errors
        binding.tilNombre.error          = null
        binding.tilEmail.error           = null
        binding.tilPassword.error        = null
        binding.tilConfirmPassword.error = null
        binding.tilTelefono.error        = null

        var ok = true
        if (nombre.isEmpty())   { binding.tilNombre.error   = "Requerido"; ok = false }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Correo inválido"; ok = false }
        if (pass.length < 6)    { binding.tilPassword.error = "Mínimo 6 caracteres"; ok = false }
        if (pass != confirmPass) { binding.tilConfirmPassword.error = "Las contraseñas no coinciden"; ok = false }
        if (telefono.isEmpty()) { binding.tilTelefono.error = "Requerido"; ok = false }
        if (!ok) return

        setLoading(true)
        binding.root.postDelayed({
            val result = MockDataSource.registrarUsuario(nombre, email, pass,
                tipoSeleccionado, telefono, ubicacion)
            setLoading(false)
            result.onSuccess { usuario ->
                session.saveSession(usuario.id, usuario.nombre, usuario.email,
                    usuario.tipoUsuario, usuario.telefono, usuario.ubicacion)
                val intent = when (usuario.tipoUsuario) {
                    TipoUsuario.EMPRENDEDOR -> Intent(this, HomeEmprendedorActivity::class.java)
                    TipoUsuario.CLIENTE     -> Intent(this, HomeClienteActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent); finish()
            }
            result.onFailure { e ->
                Snackbar.make(binding.root, e.message ?: "Error al registrar", Snackbar.LENGTH_LONG).show()
            }
        }, 700)
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnCreateAccount.isEnabled = !loading
    }
}
