package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity{

    private final String EMAIL_VALIDATE = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPasswordView,mEmailView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, this);
            mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_VALIDATE);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context mContext;
        private User myUser,usuario;

        //TODO conectar e trazer o usuário
        //TODO check if isInternetOn()

        UserLoginTask(String email, String password, Context context) {
            mEmail = email;
            mPassword = password;
            mContext = context;
        }
//        protected Boolean doInBackground(Void... params) {
//            DBTools dbTools = null;
//            try {
//                dbTools = new DBTools(mContext);
//                myUser = dbTools.getUser(mEmail);
//
//                if (myUser.getUserId() > 0) {
//                    // Account exists, check password.
//                    if (myUser.getPassword().equals(mPassword))
//                        return true;
//                    else
//                        return false;
//                } else {
//                    myUser.setPassword(mPassword);
//                    return true;
//                }
//            } finally {
//                if (dbTools != null)
//                    dbTools.close();
//            }
//        }
        @Override
        protected Boolean doInBackground(Void... params) {
            setMyUser((User) mContext);

            Gson gson = new Gson();

            String paramUser = gson.toJson(getMyUser());

            RequestParams rp = new RequestParams();
            rp.add("content",paramUser);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getMyUser().URL_USER,rp,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    String response = bytes.toString();
                    showProgress(false);
                    try {
                        // JSON Object
                        JSONObject obj = new JSONObject(response);
                        // When the JSON response has status boolean value assigned with true
                        if(obj.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), "You are successfully logged in!" +
                                    "", Toast.LENGTH_LONG).show();
                            // Navigate to Home screen
                        }
                        // Else display error message
                        else{
                            //errorMsg.setText(obj.getString("error_msg"));
                            Toast.makeText(getApplicationContext(), obj.getString("error_msg" +
                                    ""), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response " +
                                "might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                    // When Http response code is '404'
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected " +
                                "to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });


            try {
//                if (getMyUser().getUserId() > 0) {
                if (getMyUser().getUsername().equals(mEmail)) {
                    // Account exists, check password.
                    if (getMyUser().getPassword().equals(mPassword)) {
                        //TODO Check connection
                        return true;
                    }
                    else
                        return false;
                } else {
                    getMyUser().setPassword(mPassword);
                    return true;
                }
            }
            catch (Exception e){
                Log.e("ERROR_USER",e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
//                if (getMyUser().getUserId() > 0) {
                if (getMyUser().getUsername().equals(mEmail)) {
                    finish();
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    DBTools dbTools = null;
                                    try {
                                        //TODO OnValueChange like, to User, when this happens call alter method
                                        finish();

                                        Toast myToast = Toast.makeText(mContext, getMyUser().toString(), Toast.LENGTH_SHORT);
                                        myToast.show();

                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.startActivity(myIntent);
                                        finish();
                                    } finally {
                                        if (dbTools != null)
                                            dbTools.close();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                    break;
                            }
                        }
                    };

//                    //LinearLayout lpView = new LinearLayout(this.mContext);
//                    LinearLayout linLayout = new LinearLayout(this.mContext);
//                    // specifying vertical orientation
//                    linLayout.setOrientation(LinearLayout.VERTICAL);
//                    // creating LayoutParams
//                    LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    // set LinearLayout as a root element of the screen
//                    setContentView(linLayout, linLayoutParam);
//
//                    LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                    EditText inputName = new EditText(this.mContext);
//                    inputName.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    inputName.setHint(getResources().getString(R.string.name));
//                    inputName.setLayoutParams(lpView);
//                    linLayout.addView(inputName);
//
//
//
//
//                    EditText inputIdade = new EditText(this.mContext);
//                    inputIdade.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    inputIdade.setHint(getResources().getString(R.string.birthday));
//                    inputIdade.setLayoutParams(lpView);
//                    linLayout.addView(inputIdade);
//
//                    EditText inputFuncao = new EditText(this.mContext);
//                    inputFuncao.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    inputFuncao.setHint(getResources().getString(R.string.function));
//                    inputFuncao.setLayoutParams(lpView);
//                    linLayout.addView(inputFuncao);

                    //TODO saber como add mais de um campo no AlertDialog
                    EditText inputFuncao = new EditText(this.mContext);
                    inputFuncao.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    inputFuncao.setHint(getResources().getString(R.string.function));


                    AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                    builder.setMessage(R.string.confirm_registry).setPositiveButton(R.string.yes, dialogClickListener)
                            .setNegativeButton(R.string.no, dialogClickListener).setView(inputFuncao).show();

                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        public User getMyUser() {
            return myUser;
        }

        public void setMyUser(User myUser) {
            this.myUser = myUser;
        }
    }
}



