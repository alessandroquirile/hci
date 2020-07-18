package com.quiriletelese.troppadvisorproject.cognito;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;

import static android.content.ContentValues.TAG;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class CognitoSettings {
    private CognitoUserPool cognitoUserPool;
    private CognitoUserAttributes cognitoUserAttributes;
    private Context context;
    private String userPassword;

    public CognitoSettings(Context context) {
        this.context = context;
        String poolID = "us-east-1_qAD0nAvIx";
        String clientID = "dtdj2futs2n9p72lg1gk0t2dt";
        String clientSecret = "m813avdndibpeosk27uq4pfcg1fuko429ktn34hbd63tb9fp33a";
        Regions awsRegion = Regions.US_EAST_1;
        cognitoUserPool = new CognitoUserPool(context, poolID, clientID, clientSecret, awsRegion);
        cognitoUserAttributes = new CognitoUserAttributes();
    }

    public void signUpInBackground(String userId, String password) {
        cognitoUserPool.signUpInBackground(userId, password, this.cognitoUserAttributes, null, signUpCallback);
    }

    SignUpHandler signUpCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful
            Log.d(TAG, "Sign-up success");
            Toast.makeText(context, "Sign-up success, codice inviato a " + cognitoUserCodeDeliveryDetails.getDestination(), Toast.LENGTH_LONG).show();
            // Check if this user (cognitoUser) needs to be confirmed
            if (!userConfirmed) {

            } else {
                Toast.makeText(context, "Errore: l'utente era già stato confermato", Toast.LENGTH_LONG).show();
                // The user has already been confirmed
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(context, "Sign-up failed " + exception.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + exception);
        }
    };

    public void confirmUser(String userId, String code) {
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code, false, confirmationCallback);
    }

    // Callback handler for confirmSignUp API
    GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            // User was successfully confirmed
            Toast.makeText(context, "User Confirmed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            // User confirmation failed. Check exception for the cause.
        }
    };

    public void addAttribute(String key, String value) {
        cognitoUserAttributes.addAttribute(key, value);
    }

    public void userLogin(String userId, String password) {
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);
        userPassword = password;
        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    // Callback handler for the sign-in process
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            if (!userSession.isValid())
                Toast.makeText(context, "Rifai il login", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Login valido", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "Sign in success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            // Allow the sign-in to continue
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-in failed, check exception for the cause
            Toast.makeText(context, "Sign in Failure " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void tokenIsValid() {
        CognitoUser cognitoUser = cognitoUserPool.getCurrentUser();
        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    public CognitoUserPool getCognitoUserPool() {
        return cognitoUserPool;
    }
}