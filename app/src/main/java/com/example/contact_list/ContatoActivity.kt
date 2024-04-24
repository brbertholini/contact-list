package com.example.contact_list

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.Contato
import org.json.JSONArray
import android.widget.Toast
import org.json.JSONObject

class ContatoActivity : AppCompatActivity() {

    val listaContatos = ArrayList<Contato>()

    override fun onCreate(bundle : Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.contato_activity)

        val txtNome = findViewById<EditText>(R.id.editNome)
        val txtEmail = findViewById<EditText>(R.id.editEmail)
        val txtTelefone = findViewById<EditText>(R.id.editTelefone)
        val btnSalvar = findViewById<Button>(R.id.buttonGravar)
        val btnPesquisar = findViewById<Button>(R.id.buttonPesquisar)

        carregarPrefs()

        btnSalvar.setOnClickListener {
            val nome = txtNome.text.toString()
            val email = txtEmail.text.toString()
            val telefone = txtTelefone.text.toString()

            val contato = Contato(nome, email, telefone)

            print(contato)
            listaContatos.add(contato)

            salvarPrefs()
        }


        btnPesquisar.setOnClickListener {
            val contatoProcurado = txtNome.text.toString()

            val contatoEncontrado = listaContatos.find { contato ->
                contato.nome.contains(contatoProcurado, ignoreCase = true)
            }

            if (contatoEncontrado != null) {
                txtNome.setText(contatoEncontrado.nome)
                txtEmail.setText(contatoEncontrado.email)
                txtTelefone.setText(contatoEncontrado.telefone)
            } else {
                txtNome.setText("")
                txtEmail.setText("")
                txtTelefone.setText("")
                Toast.makeText(this, "Contato não existe", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun salvarPrefs() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("contatos_prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val jsonArray = JSONArray()

        for (contato in listaContatos) {
            val jsonObject = JSONObject()
            jsonObject.put("nome", contato.nome)
            jsonObject.put("email", contato.email)
            jsonObject.put("telefone", contato.telefone)
            jsonArray.put(jsonObject)
        }

        editor.putString("contatos", jsonArray.toString())
        editor.apply()

    }

    private fun carregarPrefs() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("contatos_prefs", Context.MODE_PRIVATE)
        val contatosString = sharedPreferences.getString("contatos", "")

        if (contatosString != null && contatosString.isNotEmpty()) {
            val jsonArray = JSONArray(contatosString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val nome = jsonObject.getString("nome")
                val email = jsonObject.getString("email")
                val telefone = jsonObject.getString("telefone")
                val contato = Contato(nome, email, telefone)
                listaContatos.add(contato)
            }
        }
    }
}