package uz.iskandarbek.shifrlovchi.activitys

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import uz.iskandarbek.shifrlovchi.R
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class DeCryptoActivit : AppCompatActivity() {

    private lateinit var secretKey: SecretKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_de_crypto)

        val editTextDecryptInput = findViewById<EditText>(R.id.editTextDecryptInput)
        val buttonDecrypt = findViewById<Button>(R.id.buttonDecrypt)
        val textViewDecryptOutput = findViewById<TextView>(R.id.textViewDecryptOutput)
        val copy = findViewById<ImageView>(R.id.copy)

        secretKey = getSecretKey() ?: run {
            Toast.makeText(this, "Kalitni olishda xatolik yuz berdi", Toast.LENGTH_SHORT).show()
            return
        }

        buttonDecrypt.setOnClickListener {
            if (editTextDecryptInput.text.isNotBlank()) {
                val textToDecrypt = editTextDecryptInput.text.toString()
                val decryptedText = decrypt(textToDecrypt, secretKey)
                textViewDecryptOutput.text = decryptedText
            } else {
                Toast.makeText(this, "Hech narsa kiritilgani yo'q", Toast.LENGTH_SHORT).show()
            }
        }

        copy.setOnClickListener {
            val textToCopy = textViewDecryptOutput.text.toString()
            if (textToCopy.isNotEmpty()) {
                copyToClipboard(textToCopy)
                Toast.makeText(this, "Oddiy matn nusxalandi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nusxalash uchun hech narsa yo'q", Toast.LENGTH_SHORT).show()
            }
        }
        editTextDecryptInput.addTextChangedListener {
            if (editTextDecryptInput.text.isBlank()) {
                textViewDecryptOutput.text = "Oddiy matn"
            }
        }
    }

    private fun decrypt(encryptedData: String, secretKey: SecretKey): String {
        try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            return String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Shifrlangan matnda xatolik"
        }
    }


    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Oddiy matn", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun getSecretKey(): SecretKey? {
        val sharedPreferences = getSharedPreferences("SecretKeyPrefs", Context.MODE_PRIVATE)
        val encodedKey = sharedPreferences.getString("secretKey", null)
        return if (encodedKey != null) {
            val decodedKey = Base64.decode(encodedKey, Base64.DEFAULT)
            SecretKeySpec(decodedKey, "AES")
        } else {
            null
        }
    }

}
