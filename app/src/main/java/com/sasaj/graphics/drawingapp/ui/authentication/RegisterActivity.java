package com.sasaj.graphics.drawingapp.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper;
import com.sasaj.graphics.drawingapp.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private AutoCompleteTextView username;
    private AutoCompleteTextView email;
    private EditText password;
    private Button signupButton;

    private final CognitoUserAttributes attributes = new CognitoUserAttributes();

    private final SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            Log.i(TAG, "onSuccess - is confirmed: " + signUpConfirmationState);
            if (!signUpConfirmationState) {
                Log.i(TAG, "onSuccess: not confirmed, token sent to: " + cognitoUserCodeDeliveryDetails.getDestination());
                Intent intent =  new Intent(RegisterActivity.this, VerifyActivity.class);
                startActivity(intent);
            } else {
                Log.i(TAG, "onSuccess - confirmed: ");
                Intent intent =  new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e(TAG, "Signup Failure: ", exception);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.sign_up_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attributes.addAttribute("email", email.getText().toString());

                CognitoHelper cognitoHelper = new CognitoHelper(RegisterActivity.this);
                cognitoHelper.getUserPool().signUpInBackground(username.getText().toString(),
                                                                  password.getText().toString(),
                                                                  attributes,
                                                                  null,
                                                                  signUpHandler);

            }
        });
    }
}
