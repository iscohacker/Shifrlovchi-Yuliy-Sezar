package uz.iskandarbek.shifrlovchi.activitys

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import uz.iskandarbek.shifrlovchi.R

class EncryptActivity : AppCompatActivity() {

    private val uzbekAlphabet = listOf(
        "a",
        "b",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "x",
        "y",
        "z",
        "oʻ",
        "gʻ",
        "sh",
        "ch",
        "A",
        "B",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "X",
        "Y",
        "Z",
        "Oʻ",
        "Gʻ",
        "Sh",
        "Ch"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypto)

        val editTextEncryptInput = findViewById<EditText>(R.id.editTextEncryptInput)
        val buttonEncrypt = findViewById<Button>(R.id.buttonEncrypt)
        val textViewEncryptOutput = findViewById<TextView>(R.id.textViewEncryptOutput)
        val copy = findViewById<ImageView>(R.id.copy)

        buttonEncrypt.setOnClickListener {
            if (editTextEncryptInput.text.isNotBlank()) {
                val textToEncrypt = editTextEncryptInput.text.toString()
                val encryptedText = caesarCipherEncrypt(textToEncrypt, 3)
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

    private fun caesarCipherEncrypt(input: String, shift: Int): String {
        val result = StringBuilder()
        val tokens = tokenize(input)
        for (token in tokens) {
            val index = uzbekAlphabet.indexOf(token)
            if (index != -1) {
                val newIndex = (index + shift) % uzbekAlphabet.size
                result.append(uzbekAlphabet[newIndex])
            } else {
                result.append(token)
            }
        }
        return result.toString()
    }

    private fun tokenize(input: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < input.length) {
            if (i + 2 <= input.length && uzbekAlphabet.contains(input.substring(i, i + 2))) {
                tokens.add(input.substring(i, i + 2))
                i += 2
            } else {
                tokens.add(input.substring(i, i + 1))
                i++
            }
        }
        return tokens
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Shifrlangan matn", text)
        clipboard.setPrimaryClip(clip)
    }
}
