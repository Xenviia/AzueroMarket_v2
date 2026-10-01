package com.azueromarket.ui.emprendedor

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import com.azueromarket.databinding.DialogAgregarProductoBinding
import com.azueromarket.model.CategoriaProducto
import com.azueromarket.model.Producto
import com.azueromarket.utils.MockDataSource

class AgregarProductoDialog(
    context: Context,
    private val emprendedorId: String,
    private val emprendedorNombre: String,
    private val emprendedorTel: String,
    private val onPublicado: () -> Unit
) : Dialog(context) {

    private lateinit var b: DialogAgregarProductoBinding

    companion object {
        fun show(
            ctx: Context,
            emprendedorId: String,
            emprendedorNombre: String,
            emprendedorTel: String,
            onPublicado: () -> Unit
        ) = AgregarProductoDialog(ctx, emprendedorId, emprendedorNombre, emprendedorTel, onPublicado).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        b = DialogAgregarProductoBinding.inflate(LayoutInflater.from(context))
        setContentView(b.root)
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.95).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setupSpinner()
        b.btnPublicar.setOnClickListener { publicar() }
        b.btnCancelar.setOnClickListener { dismiss() }
    }

    private fun setupSpinner() {
        val categorias = CategoriaProducto.values().map { "${it.emoji} ${it.nombreMostrar}" }
        b.spinnerCategoria.adapter = ArrayAdapter(context,
            android.R.layout.simple_spinner_item, categorias).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun publicar() {
        val nombre      = b.etNombre.text.toString().trim()
        val descripcion = b.etDescripcion.text.toString().trim()
        val precioStr   = b.etPrecio.text.toString().trim()
        val stockStr    = b.etStock.text.toString().trim()
        val ubicacion   = b.etUbicacion.text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) return

        val precio  = precioStr.toDoubleOrNull() ?: return
        val stock   = stockStr.toIntOrNull() ?: 0
        val catIdx  = b.spinnerCategoria.selectedItemPosition
        val cat     = CategoriaProducto.values()[catIdx]

        MockDataSource.productos.add(
            Producto(
                id                = "prod_${System.currentTimeMillis()}",
                nombre            = nombre,
                descripcion       = descripcion,
                precio            = precio,
                categoria         = cat,
                emprendedorId     = emprendedorId,
                emprendedorNombre = emprendedorNombre,
                emprendedorTelefono = emprendedorTel,
                ubicacion         = ubicacion.ifEmpty { "Azuero, Panamá" },
                stock             = stock,
                disponible        = true
            )
        )
        onPublicado()
        dismiss()
    }
}
