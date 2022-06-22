package com.example.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import sqip.CardEntry
import sqip.handleActivityResult
import com.google.android.gms.wallet.TransactionInfo


import com.google.android.gms.wallet.AutoResolveHelper
import sqip.GooglePay.createPaymentDataRequest
import sqip.GooglePay.createIsReadyToPayRequest


class MainActivity : AppCompatActivity() {

    private val LOCATION_ID = "LZWJDZRCZZDPJ"
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 1
    private lateinit var paymentsClient: PaymentsClient
    private lateinit var googlePayButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        paymentsClient = Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        )
        googlePayButton = findViewById(R.id.pay_with_google_button)
        googlePayButton.setOnClickListener {
            googlePayButton.setEnabled(false)
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(
                    createPaymentDataRequest(
                        LOCATION_ID,
                        TransactionInfo.newBuilder().setTotalPriceStatus(
                            WalletConstants.TOTAL_PRICE_STATUS_NOT_CURRENTLY_KNOWN
                        ).setCurrencyCode("USD").build()
                    )
                ),
                it.getContext() as MainActivity,
                LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }

        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            CardEntry.startCardEntryActivity(this,true,1)
        }

        paymentsClient.isReadyToPay(createIsReadyToPayRequest())
            .addOnCompleteListener(
                this
            ) { task: Task<Boolean?> ->
                googlePayButton.isEnabled = task.isSuccessful
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