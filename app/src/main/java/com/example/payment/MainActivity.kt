package com.example.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.payment.ui.main.MainFragment
import sqip.CardEntry
import sqip.handleActivityResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            CardEntry.startCardEntryActivity(this,true,1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CardEntry.handleActivityResult(data,{
            Toast.makeText(this,
                it.getSuccessValue().nonce,
                Toast.LENGTH_SHORT)
                .show();
        })
    }
}