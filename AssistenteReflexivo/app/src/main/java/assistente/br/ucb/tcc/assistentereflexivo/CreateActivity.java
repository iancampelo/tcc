package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;


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
                //call request
                ConnectTask connectTask = new ConnectTask(mContext);
                connectTask.execute((Void) null);

            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

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


    public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private final Context mContext;
        private final User user;
        private String response;

        public String getResponse() {
            return response;
        }

        private void setResponse(String response) {
            this.response = response;
        }

        ConnectTask(Context context) {
            mContext = context;
            user = (User)context;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            IntegrateWS client = new IntegrateWS("http://192.168.1.4:8080/webservice/usuario/consultarUsuario");
            client.AddHeader("Accept", "application/json");
            client.AddHeader("Content-type", "application/json");
            client.AddParam("content", user.toJson());

            try {
                client.Execute(RequestMethod.POST);
                Looper.prepare();
                setResponse(client.getResponse());
                return true;
            } catch (Exception e) {
                Log.e("ERROR_CONNECTION", e.getMessage());
                setResponse("ERROR_"+e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try{

                showProgress(false);
                if(success) {
                    if(getResponse()!= null){
                        if (getResponse().isEmpty()) {
                            inpName.setError(getString(R.string.error_incorrect_password));
                            inpName.requestFocus();
                        }
                        GsonBuilder builder = new GsonBuilder();
                        builder.setPrettyPrinting().serializeNulls();
                        Gson gson = builder.create();
//                        setResponse(getResponse().replace("\n",""));
                        User ser = gson.fromJson(getResponse(),User.class);

                        Intent intent = new Intent(mContext, PreReflectionActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    inpName.setError(getString(R.string.error_incorrect_password));
                    inpName.requestFocus();
                }
            }catch (Exception e){
                inpName.setError(getString(R.string.error_incorrect_password));
                inpName.requestFocus();
            }

        }

    }

}

