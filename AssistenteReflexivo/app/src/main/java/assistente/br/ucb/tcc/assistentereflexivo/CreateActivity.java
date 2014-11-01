package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.HeaderGroup;
import org.json.*;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class CreateActivity extends Activity implements AdapterView.OnItemSelectedListener, NumberPicker.OnValueChangeListener{
    private static Act act = null;
    private static User user = null;
    private static Context mContext = null;
    private View mProgressView;
    private View mScrlViewCreate;
    public EditText inpName;
    public Spinner spinner;
    public NumberPicker npHrs;
    public NumberPicker npMin;
    public NumberPicker npSec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mContext = getApplicationContext();
        user = (User)mContext;
        act = (Act)mContext;
        load();
    }
    public void load() {
        mProgressView = findViewById(R.id.create_progress);
        mScrlViewCreate = findViewById(R.id.ScrlViewCreate);

        ImageButton btnNextCreate = (ImageButton) findViewById(R.id.btnNextCreate);

        inpName = (EditText) findViewById(R.id.inpName);
        spinner = (Spinner) findViewById(R.id.spinPrediction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.predict_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //HOURS
        npHrs = (NumberPicker) findViewById(R.id.numHrs);
        npHrs.setMaxValue(23);
        npHrs.setMinValue(0);
        npHrs.setOnValueChangedListener(this);

        //MINUTES
        npMin = (NumberPicker) findViewById(R.id.numMin);
        npMin.setMaxValue(59);
        npMin.setMinValue(0);
        npMin.setOnValueChangedListener(this);

        //SECS
        npSec = (NumberPicker) findViewById(R.id.numSecs);
        npSec.setMaxValue(59);
        npSec.setMinValue(0);
        npSec.setOnValueChangedListener(this);

        btnNextCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setNome(inpName.getText().toString());
                act.setPredicao(spinner.getSelectedItem().toString());
                Timestamp time = new Timestamp(0);
                time.setSeconds(npSec.getValue());
                time.setMinutes(npMin.getValue());
                time.setHours(npHrs.getValue());
                act.setTempoEstimado(time);
                showProgress(true);
                invokeWS();
                Toast myToast = Toast.makeText(mContext, time.toString(), Toast.LENGTH_SHORT);
                myToast.show();

                Intent intent = new Intent(view.getContext(), PreReflectionActivity.class);
                startActivity(intent);
            }
        });
    }
    public void invokeWS(){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        try{
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Accept", "application/json");
            client.addHeader("Content-type", "application/json");
            RequestParams rp = new RequestParams();
            rp.add("content",user.toJson());
            URL url = new URL("http://192.168.0.19:8080/webservice/usuario/cadastrarUsuario");
            HeaderGroup hg = new HeaderGroup();
            hg.addHeader(new BasicHeader("Accept", "application/json"));
            hg.addHeader(new BasicHeader("Content-type", "application/json"));
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(user.toJson().getBytes(StandardCharsets.UTF_8)));
            client.post(mContext,url.toString(),hg.getAllHeaders(),basicHttpEntity,"application/json", new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    String response = new String(bytes);

                    // Hide Progress Dialog
                    showProgress(false);
                    try {
                        // JSON Object
                        JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        User newUser = new User();
                        newUser = gson.fromJson(response,User.class);

                        Toast.makeText(mContext,newUser.toString(), Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(mContext, "Error Occured [Server's JSON response " +
                                "might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }catch (Exception e){
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                // When the response returned by REST has Http response code other than '200'

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    // Hide Progress Dialog
//                    prgDialog.hide();
                    showProgress(false);
                    // When Http response code is '404'
                    if (statusCode == 404) {
                        Toast.makeText(mContext, "Requested resource not found", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(mContext, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Toast.makeText(mContext, "Unexpected Error occcured! [Most common Error: Device might not be connected " +
                                "to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            showProgress(false);
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ERRO_ASYNC_HTTP",e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mScrlViewCreate.setVisibility(show ? View.GONE : View.VISIBLE);
            mScrlViewCreate.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mScrlViewCreate.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mScrlViewCreate.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean checkFields() {
        inpName.setError(null);
        String name = inpName.getText().toString();

        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(name)){
            inpName.setError(getString(R.string.error_field_required));
            focus=inpName;
            focus.requestFocus();
            valid = false;
        }
        if((npMin.getValue()==0)&&(npHrs.getValue()==0)&&(npSec.getValue()==0)){
            Toast myToast = Toast.makeText(this, R.string.error_numberpicker, Toast.LENGTH_SHORT);
            myToast.show();
            valid = false;
        }
        return valid;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_logout){
            //TODO Implementar um Logout real, que não volte para a Activity anterior
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(CreateActivity.this,SettingsActivity.class);
            CreateActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedPrediction = (String) parent.getItemAtPosition(position);
        Log.println(1,"selected prediction",selectedPrediction);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){

            case R.id.numSecs:
                int ValSecs = newVal;
                break;
            case R.id.numHrs:
                int ValHrs = newVal;
                break;
            case R.id.numMin:
                int ValMin = newVal;
                break;
        }
    }
}
