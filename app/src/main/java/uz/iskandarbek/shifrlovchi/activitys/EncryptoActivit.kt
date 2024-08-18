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
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class EncryptoActivit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypto)

        val editTextEncryptInput = findViewById<EditText>(R.id.editTextEncryptInput)
        val buttonEncrypt = findViewById<Button>(R.id.buttonEncrypt)
        val textViewEncryptOutput = findViewById<TextView>(R.id.textViewEncryptOutput)
        val copy = findViewById<ImageView>(R.id.copy)

        var secretKey = getSecretKey()
        if (secretKey == null) {
            secretKey = generateSecretKey()
            saveSecretKey(secretKey)
        }

        buttonEncrypt.setOnClickListener {
            if (editTextEncryptInput.text.isNotBlank()) {
                val textToEncrypt = editTextEncryptInput.text.toString()
                val encryptedText = encrypt(textToEncrypt, secretKey)
                textViewEncryptOutput.text = encryptedText
            } else {
                Toast.makeText(this, "Hech narsa kiritilgani yo'q", Toast.LENGTH_SHORT).show()
            }
        }

        copy.setOnClickListener {
            val textToCopy = textViewEncryptOutput.text.toString()
            if (textToCopy.isNotEmpty()) {
                copyToClipboard(textToCopy)
                Toast.makeText(this, "Shifrlangan matn nusxalandi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nusxalash uchun hech narsa yo'q", Toast.LENGTH_SHORT).show()
            }
        }
        editTextEncryptInput.addTextChangedListener {
            if (editTextEncryptInput.text.isBlank()) {
                textViewEncryptOutput.text = "Shifrlangan matn"
            }
        }
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        return keyGenerator.generateKey()
    }

    private fun encrypt(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Shifrlangan matn", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun saveSecretKey(secretKey: SecretKey) {
        val sharedPreferences = getSharedPreferences("SecretKeyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
        editor.putString("secretKey", encodedKey)
        editor.apply()
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
