package kz.kolesateam.confapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import kz.kolesateam.confapp.common.preferences.UserNameDataSource
import kz.kolesateam.confapp.common.UpcomingEventsRouter
import kz.kolesateam.confapp.common.AbstractTextWatcher
import org.koin.android.ext.android.inject

class HelloActivity : AppCompatActivity() {
    private val userNameDataSource: UserNameDataSource by inject()

    private lateinit var openHelloButton: Button
    private lateinit var editTextName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        bindViews()
        if (userNameDataSource.isSavedUserName()) {
            startActivity(UpcomingEventsRouter().createIntent(this))
            finish()
        }
    }

    private fun bindViews() {
        editTextName = findViewById(R.id.activity_hello_edit_text_name)
        editTextName.addTextChangedListener(object : AbstractTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                openHelloButton.isEnabled = text.toString().isNotBlank()
            }
        })
        openHelloButton = findViewById(R.id.activity_hello_continue_button)
        openHelloButton.setOnClickListener()
        {
            saveUserName(editTextName.text.toString().trim())
            startActivity(UpcomingEventsRouter().createIntent(this))
        }
    }

    private fun saveUserName(userName: String) {
        userNameDataSource.saveUserName(userName)
    }
}