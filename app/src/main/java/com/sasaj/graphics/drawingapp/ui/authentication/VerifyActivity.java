package com.sasaj.graphics.drawingapp.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper;
import com.sasaj.graphics.drawingapp.ui.main.MainActivity;

public class VerifyActivity extends AppCompatActivity {

    private static final String TAG = VerifyActivity.class.getSimpleName();
    private AutoCompleteTextView code;
    private AutoCompleteTextView username;
    private Button verify;

    private GenericHandler genericHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.i(TAG, "Verification success!");
            Intent intent =  new Intent(VerifyActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e(TAG, "Verification Failure", exception);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        code = findViewById(R.id.verification_code);
        username = findViewById(R.id.username);
        verify = findViewById(R.id.verify_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CognitoHelper cognitoHelper = new CognitoHelper(VerifyActivity.this);
               CognitoUser user = cognitoHelper.getUserPool().getUser(username.getText().toString());
               user.confirmSignUpInBackground(code.getText().toString(), false, genericHandler);
            }
        });

    }
}
