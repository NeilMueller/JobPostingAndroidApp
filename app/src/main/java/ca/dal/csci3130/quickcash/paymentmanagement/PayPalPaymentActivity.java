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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.BuildConfig;
import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAOAdapter;

public class PayPalPaymentActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private EditText enterAmtET;
    private TextView paymentStatusTV;
    private DAO dao;
    private String jobId;
    private Double totalPay;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobId = extras.getString("jobId");
            totalPay = extras.getDouble("totalPay");
        }

        dao = new JobDAOAdapter(new JobDAO());

        enterAmtET = findViewById(R.id.enterAmtET);
        if (totalPay != null && totalPay != 0) {
            enterAmtET.setText(String.valueOf(totalPay));
        }

        Button payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        Button cancelPayment = findViewById(R.id.btnCancel);

        configPayPal();
        initActivityLauncher();

        payNowBtn.setOnClickListener(v -> processPayment());
        cancelPayment.setOnClickListener(v -> moveToEmployerHomeActivity());
    }

    /**
     * Configure paypal with client id provided
     */
    private void configPayPal() {
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    /**
     * Handle what happens when a user makes a payment and comes back to the app.
     */
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
                                closeJobStatus(jobId);
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

    /**
     * Process Payment made by a user
     */
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

    /**
     * Close job when payment is done
     */
    private void closeJobStatus(String jobID) {
        DatabaseReference ref = dao.getDatabaseReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(jobID)) {
                        DatabaseReference jobRef = snapshot1.getRef();
                        Map<String, Object> jobUpdate = new HashMap<>();
                        jobUpdate.put("jobStatusOpen", false);
                        jobRef.updateChildren(jobUpdate);
                        Toast.makeText(getApplicationContext(), "Job Closed, Moving to Payment", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - closeJobStatus (EmployerJobListingActivity)", error.getMessage());
            }
        });
    }

    /**
     * Move to employer home
     */
    private void moveToEmployerHomeActivity() {
        Intent intent = new Intent(PayPalPaymentActivity.this, EmployerHomeActivity.class);
        startActivity(intent);
    }

    /**
     * Check if the amount is empty
     *
     * @param amount
     * @return true or false
     */
    protected boolean isEmpty(String amount) {
        if (amount.isEmpty()) {
            createToast(R.string.toast_missing_component);
            return true;
        }
        return false;
    }

    /**
     * Create a toast
     *
     * @param toastMissingComponent
     */
    private void createToast(int toastMissingComponent) {
        Toast.makeText(this, getText(toastMissingComponent), Toast.LENGTH_LONG).show();
    }
}
