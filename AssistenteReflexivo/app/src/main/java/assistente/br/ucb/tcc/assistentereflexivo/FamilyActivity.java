package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class FamilyActivity extends Activity {
    public EditText inpActFam,inpProblem,inpObjv;
    private static Act act;
    private ActSaveTask mActSaveTask;
    private IntegrateWS client = null;
    private View mProgressView, mScrollView;
    private static Context mContext = null;
    private boolean success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        mContext = getApplicationContext();
        load();
    }


    private void load() {
        act = (Act)mContext;
        mProgressView = findViewById(R.id.fam_progress);
        mScrollView = findViewById(R.id.ScrlViewFam);

        inpActFam = (EditText) findViewById(R.id.inpNameFam);
        inpProblem = (EditText)findViewById(R.id.inpProblem);
        inpObjv = (EditText)findViewById(R.id.inpActGoal);
        inpActFam.setHint(act.getNome());
        ImageButton btn = (ImageButton) findViewById(R.id.btnNextFam);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setObjetivo(inpObjv.getText().toString());
                act.setComprensao(inpProblem.getText().toString());


                if(!isOnline()){
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.offline))
                            .setCancelable(false)
                            .setMessage(getString(R.string.offline_msg))
                            .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }

                            })
                            .show();
                }
                else
                    saveAct();

            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.close_app))
                .setMessage(getString(R.string.close_confirm))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        System.exit(0);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
    private boolean checkFields() {
        inpObjv.setError(null);
        inpProblem.setError(null);
        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(inpProblem.getText().toString().trim())){
            inpProblem.setError(getString(R.string.error_field_required));
            focus=inpProblem;
            focus.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(inpObjv.getText().toString().trim())){
            inpObjv.setError(getString(R.string.error_field_required));
            focus=inpObjv;
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
            Intent settings = new Intent(FamilyActivity.this,SettingsActivity.class);
            FamilyActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(FamilyActivity.this, LoginActivity.class);
            FamilyActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean saveAct() {
        success = false;

        Util.showProgress(true,mContext,mScrollView,mProgressView);
        mActSaveTask = new ActSaveTask();
        mActSaveTask.execute();
        Util.showProgress(false,mContext,mScrollView,mProgressView);
        return success;
    }

    public class ActSaveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            success = false;

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
                Intent myIntent = new Intent(FamilyActivity.this, ProductionActivity.class);
                FamilyActivity.this.startActivity(myIntent);
            }else {
                Log.e("ERROR_CREATE_USER_ACT", getString(R.string.error));
                Toast myToast = Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT);
                myToast.show();
            }
        }
        @Override
        protected void onCancelled() {
            mActSaveTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }

    }

}
