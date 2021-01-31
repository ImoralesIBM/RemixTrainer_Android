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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

/**
 * A Register screen that offers registration via email/password.
 */
public class RegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    /**
     * Keep track of the registration task to ensure we can cancel it if requested.
     */
    private Task<AuthResult> mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mFirstNameView, mLastNameView, mEmailView;
    private EditText mPasswordView;

    private View mProgressView;
    private View mRegistrationFormView;

    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Set up the registration form.

        mFirstNameView = findViewById(R.id.first_name);
        mLastNameView = findViewById(R.id.last_name);
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            return id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL;
        });

        mRegisterButton = findViewById(R.id.email_register_button);
        mRegisterButton.setOnClickListener(v -> { attemptRegistration(); });

        mRegistrationFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.registration_progress);
    }


    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual registration attempt is made.
     */
    private void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String first_name = mFirstNameView.getText().toString();
        String last_name = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Ensure that the form is complete.
        if (TextUtils.isEmpty(first_name)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(last_name))
        {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }
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

        // Check for a valid email address and password.
        if (!cancel)
        {
            if (!isEmailValid(email))
            {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
            if (!isPasswordValid(password))
            {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
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
            mAuthTask = mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, (task) -> {
                        showProgress(false);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful())
                        {
                            mEmailView.setError(getString(R.string.error_cannot_register));
                        } else {
                            DatabaseLoadCompleteListener loadCompleteListener = () -> {
                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(first_name + " " + last_name)
                                            .build());

                                    startActivity(new Intent(RegisterActivity.this, InitialParamsActivity.class));
                                    finish();
                                };

                            mDatabase.setLoadCompleteListenerAndUid(loadCompleteListener, FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                        }

                        showProgress(false);
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
    }

    private boolean isEmailValid(String email)
    {
        return email.matches(getString(R.string.email_validator));
    }

    private boolean isPasswordValid(String password)
    {
        return password.matches("(?=^.{8,255}$)((?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))^.*");
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

            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistrationFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

