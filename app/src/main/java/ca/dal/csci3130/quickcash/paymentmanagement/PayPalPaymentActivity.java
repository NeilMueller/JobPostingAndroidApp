package ca.dal.csci3130.quickcash.paymentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import ca.dal.csci3130.quickcash.BuildConfig;
import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class PayPalPaymentActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private EditText enterAmtET;
    private Button payNowBtn;
    private TextView paymentStatusTV;
    private Button cancelPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);
        init();
        configPayPal();
        initActivityLauncher();
        setListeners();

    }

    private void init() {
        enterAmtET = findViewById(R.id.enterAmtET);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        cancelPayment = findViewById(R.id.btnCancel);
    }

    private void configPayPal() {
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null) {
                            try {
                                // Get the payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                Log.i(TAG, paymentDetails);
                                // Extract json response and display it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);
                                String payID = payObj.getJSONObject("response").getString("id");
                                String state = payObj.getJSONObject("response").getString("state");
                                paymentStatusTV.setText(String.format("Payment %s%n with payment id is %s", state, payID));
                            } catch (JSONException e) {
                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.d(TAG, "Launcher Result Invalid");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.d(TAG, "Launcher Result Cancelled");
                    }
                });
    }

    private void setListeners() {

        payNowBtn.setOnClickListener(v -> processPayment());
        cancelPayment.setOnClickListener(v -> moveToEmployerHomeActivity());
    }

    private void processPayment() {
        final String amount = enterAmtET.getText().toString();
        if (!isEmpty(amount)) {
            final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                    amount), "CAD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);

            // Create Paypal Payment activity intent
            final Intent intent = new Intent(this, PaymentActivity.class);
            // Adding paypal configuration to the intent
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
            // Adding paypal payment to the intent
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            // Starting Activity Request launcher
            activityResultLauncher.launch(intent);
        }
    }

    private void moveToEmployerHomeActivity() {
        Intent intent = new Intent(PayPalPaymentActivity.this, EmployerHomeActivity.class);
        startActivity(intent);
    }

    protected boolean isEmpty(String amount) {
        if (amount.isEmpty()) {
            createToast(R.string.toast_missing_component);
            return true;
        }
        return false;
    }

    private void createToast(int toastMissingComponent) {
        Toast.makeText(getApplicationContext(), getText(toastMissingComponent), Toast.LENGTH_LONG).show();
    }

}
