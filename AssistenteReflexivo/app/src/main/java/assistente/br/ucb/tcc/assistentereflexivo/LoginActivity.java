package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity{

    private final String EMAIL_VALIDATE = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private UserLoginTask mAuthTask = null;
    private EditText mPasswordView,mEmailView,inputName,inputFuncao,inputBirthday;
    private View mProgressView;
    private View mLoginFormView;
    private static Context mContext = null;
    private static Act act = null;
    private static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        user = (User)mContext;
        act = (Act)mContext;

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
        String email = mEmailView.getText().toString().toLowerCase();
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
            Util.showProgress(true,mContext,mLoginFormView,mProgressView);
            mAuthTask = new UserLoginTask(email, password);
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private User usuario;
        private IntegrateWS client = null;

        //TODO check if isInternetOn()

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            user = (User)mContext;
            user.setPassword(password);
            user.setUsername(email);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = false;

            try {

                if (user.getUsername() != null) {
                    client = new IntegrateWS(Util.getUrl(R.string.url_ws_get_user,mContext));
                    client.AddHeader("Accept", "application/json");
                    client.AddHeader("Content-type", "application/json");
                    client.AddParam("content", user.toJson());

                    client.Execute(RequestMethod.POST);
                    String a = client.getResponse();
                    client.getErrorMessage();
                    if (client.getResponseCode() == 200) {
                        if (a != null) {
                            usuario = Util.jsonToUser(a);
                            if(usuario==null)
                                return false;
                            if (user.getUsername().equals(usuario.getUsername())) {
                                if (user.getPassword().equals(usuario.getPassword())) {
                                    success = true;
                                } else
                                    success = false;
                            } else {
                                user.setUsername(mEmail);
                                user.setPassword(mPassword);
                                return true;
                            }
                        }
                    }
                    else
                        success = false;
                }
            }catch (Exception e) {
                Log.e("ERROR_CONNECTION", e.getMessage());
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            Util.showProgress(false,mContext,mLoginFormView,mProgressView);


            if (success) {
                finish();
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }else if(client.getResponseCode()==500) {
                try{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    try {
                                        //TODO OnValueChange like, to User, when this happens call alter method
                                        if(validateFields()){
                                            try{

                                                user.setBirthday(inputBirthday.getText().toString());
                                                user.setFuncao(inputFuncao.getText().toString());
                                                user.setName(inputName.getText().toString());

                                                client = new IntegrateWS(Util.getUrl(R.string.url_ws_create_user,mContext));
                                                client.AddHeader("Accept", "application/json");
                                                client.AddHeader("Content-type", "application/json");
                                                client.AddParam("content", user.toJson());
                                                client.Execute(RequestMethod.POST);
                                                client.getErrorMessage();
                                                if (client.getResponseCode() == 200) {
                                                    String res = client.getResponse();
                                                    if(res.contains("S")) {
                                                        Toast myToast = Toast.makeText(mContext, getString(R.string.msg_user_create), Toast.LENGTH_LONG);
                                                        myToast.show();
                                                    }
                                                }
                                            }catch (Exception e){
                                                String err = (e.getMessage()==null)?getString(R.string.error):e.getMessage();
                                                Log.e("ERROR_CREATE_USER_ACT", err);
                                                break;
                                            }
                                        }
                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.startActivity(myIntent);
                                        finish();
                                    } catch (Exception ex) {
                                        String err = (ex.getMessage()==null)?getString(R.string.error):ex.getMessage();
                                        Log.e("ERROR_CREATE_USER_ACT", err);
                                        Toast myToast = Toast.makeText(mContext, err, Toast.LENGTH_SHORT);
                                        myToast.show();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast myToast = Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT);
                                    myToast.show();
                                    mPasswordView.requestFocus();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    inputName = new EditText(mContext);
                    inputName.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    inputName.setHint(getResources().getString(R.string.name));
                    inputName.setTextColor(Color.BLACK);
                    inputName.setHint(getString(R.string.name));
                    layout.addView(inputName);

                    inputFuncao = new EditText(mContext);
                    inputFuncao.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    inputFuncao.setHint(getString(R.string.function));
                    inputFuncao.setTextColor(Color.BLACK);
                    layout.addView(inputFuncao);

                    inputBirthday = new EditText(mContext);
                    inputBirthday.setTextColor(Color.BLACK);
                    inputBirthday.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    inputBirthday.setHint(getResources().getString(R.string.name));
                    inputBirthday.setHint(getString(R.string.birthday));
                    layout.addView(inputBirthday);

                    builder.setMessage(R.string.confirm_registry);
                    builder.setPositiveButton(R.string.yes, dialogClickListener);
                    builder.setNegativeButton(R.string.no, dialogClickListener);
                    builder.setView(layout).show();
                }catch(RuntimeException re){
                    re.getMessage();
                    re.getCause();
                }
            }else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            Util.showProgress(false,mContext,mLoginFormView,mProgressView);
        }

        public Boolean validateFields(){
            Boolean success = false;
            if(inputBirthday.getText()!=null||!inputBirthday.getText().toString().trim().isEmpty()){
                success=true;
            }
            if(inputFuncao.getText()!=null||!inputFuncao.getText().toString().trim().isEmpty()){
                success=true;
            }
            if(inputName.getText()!=null||!inputName.getText().toString().trim().isEmpty()){
                success=true;
            }
            return success;
        }

    }
}



