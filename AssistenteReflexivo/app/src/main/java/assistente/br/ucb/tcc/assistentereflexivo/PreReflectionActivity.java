package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class PreReflectionActivity extends Activity {
    private static Act act = null;
    private View mProgressView, mScrollView;
    private static Context mContext = null;
    public Spinner spinAtt;
    public EditText txtStrategy,txtResource,txtPreName;
    private ActSaveTask mActSaveTask;
    private IntegrateWS client = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        act = (Act)getApplicationContext();

        setContentView(R.layout.activity_pre_reflection);
        load();

    }

    private void load() {

        txtStrategy = (EditText)findViewById(R.id.inpStrategy);
        txtResource = (EditText)findViewById(R.id.inpResources);
        txtPreName = (EditText) findViewById(R.id.inpNamePre);
        txtPreName.setHint(act.getNome());
        mProgressView = findViewById(R.id.pre_progress);
        mScrollView = findViewById(R.id.ScrlViewPre);
        //Spinner Attention
        spinAtt = (Spinner) findViewById(R.id.spinAttentionDegree);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.attention_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAtt.setAdapter(adapter);

        ImageButton btn = (ImageButton) findViewById(R.id.btnNextPre);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setGrauAtencao(spinAtt.getSelectedItem().toString());
                act.setRecursos(txtResource.getText().toString());
                act.setEstrategia(txtStrategy.getText().toString());

                if(!saveAct())
                    return;



                Intent intent = new Intent(view.getContext(), FamilyActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean saveAct() {
        boolean success = false;

        mActSaveTask = new ActSaveTask();
        mActSaveTask.execute();

        return success;
    }

    private boolean checkFields() {
        txtStrategy.setError(null);
        txtResource.setError(null);
        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(txtStrategy.getText().toString().trim())){
            txtStrategy.setError(getString(R.string.error_field_required));
            focus=txtStrategy;
            focus.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(txtResource.getText().toString().trim())){
            txtResource.setError(getString(R.string.error_field_required));
            focus=txtStrategy;
            focus.requestFocus();
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(PreReflectionActivity.this,SettingsActivity.class);
            PreReflectionActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(PreReflectionActivity.this, LoginActivity.class);
            PreReflectionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public class ActSaveTask extends AsyncTask<Void, Void, Boolean> {

        //TODO check if isInternetOn()
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = false;

            try {
                client = new IntegrateWS(Util.getUrl(R.string.url_ws_save_act,mContext));
                client.AddHeader("Accept", "application/json");
                client.AddHeader("Content-type", "application/json");
                client.AddParam("content", act.toJsonAct());

                client.Execute(RequestMethod.POST);
                if (client.getResponseCode() == 200) {
                    success = true;
                }
            }catch (Exception e) {
                Log.e("ERROR_CONNECTION", e.getMessage());
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mActSaveTask = null;
            Util.showProgress(false, mContext, mScrollView, mProgressView);


            if (success) {
                finish();
                Intent myIntent = new Intent(PreReflectionActivity.this, MainActivity.class);
                PreReflectionActivity.this.startActivity(myIntent);
            }else {
                if (client.getResponseCode() == 500) {
                    try {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        try {
                                            //TODO OnValueChange like, to User, when this happens call alter method
                                            try {


                                                client = new IntegrateWS(Util.getUrl(R.string.url_ws_save_act, mContext));
                                                client.AddHeader("Accept", "application/json");
                                                client.AddHeader("Content-type", "application/json");
                                                client.AddParam("content", act.toJsonAct());
                                                client.Execute(RequestMethod.POST);
                                                client.getErrorMessage();
                                                if (client.getResponseCode() == 200) {
                                                    String res = client.getResponse();
                                                    if (res.contains("S")) {
                                                        Toast myToast = Toast.makeText(mContext, getString(R.string.msg_act_create), Toast.LENGTH_LONG);
                                                        myToast.show();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                String err = (e.getMessage() == null) ? getString(R.string.error) : e.getMessage();
                                                Log.e("ERROR_CREATE_USER_ACT", err);
                                                break;
                                            }
                                            Intent myIntent = new Intent(PreReflectionActivity.this, MainActivity.class);
                                            PreReflectionActivity.this.startActivity(myIntent);
                                            finish();
                                        } catch (Exception ex) {
                                            String err = (ex.getMessage() == null) ? getString(R.string.error) : ex.getMessage();
                                            Log.e("ERROR_CREATE_USER_ACT", err);
                                            Toast myToast = Toast.makeText(mContext, err, Toast.LENGTH_SHORT);
                                            myToast.show();
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        Toast myToast = Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT);
                                        myToast.show();
                                        break;
                                }
                            }
                        };
                    } catch (RuntimeException re) {
                        re.getMessage();
                        re.getCause();
                    }
                }
                else {
                    Toast myToast = Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mActSaveTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }

    }
}
