package com.remixtrainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    /**
     * Keep track of the registration task to ensure we can cancel it if requested.
     */
    private Task<AuthResult> mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private TextView mResetPasswordStatusMessage;

    private View mProgressView;
    private View mLoginFormView;

    private Button mLoginButton, mForgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.

        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mResetPasswordStatusMessage = findViewById(R.id.recovery_email_status_message);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            return id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL;
            });

        mLoginButton = findViewById(R.id.email_sign_in_button);
        mLoginButton.setOnClickListener(v -> { attemptLogin(); });

        mForgotPasswordButton = findViewById(R.id.forgot_password_button);
        mForgotPasswordButton.setOnClickListener(v -> {
                showProgress(true);

                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Ensure that the form is complete.
                if (TextUtils.isEmpty(email))
                {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener((task) -> {
                                if (task.isSuccessful())
                                {
                                    mResetPasswordStatusMessage.setVisibility(View.VISIBLE);
                                }
                                showProgress(false);
                            });
                }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to login the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors and status messages.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mResetPasswordStatusMessage.setVisibility(View.GONE);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Ensure that the form is complete.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, (task) -> {
                        DatabaseLoadCompleteListener loadCompleteListener = () -> {
                            startActivity(new Intent(LoginActivity.this, InitialParamsActivity.class));
                            finish();
                        };

                        showProgress(false);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful())
                        {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                        } else {
                            mDatabase.setLoadCompleteListenerAndUid(loadCompleteListener, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }

                        mAuthTask = null;
                    }).addOnCanceledListener(() -> {
                            showProgress(false);
                            mAuthTask = null;
                        });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        showProgress(false);
        mResetPasswordStatusMessage.setVisibility(View.GONE);
    }

    private boolean isEmailValid(String email)
    {
        return email.matches(getString(R.string.email_validator));
    }

    private boolean isPasswordValid(String password)
    {
        return password.matches(getString(R.string.password_validator));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

