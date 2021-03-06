package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

    private ImageView gaugeBlue, gaugeRed, gaugeGreen;
    private ImageButton btnAddActivity, btnStatsActivity;
    private TextView txtMain,txtAvgAct;
    private static Act act = null;
    private static User user= null;
    private static Context mContext = null;
    private UserTask mAuthTask = null;
    private KmaTask mKmaTask = null;
    private View mProgressView, mScrollView;
    private static Float kmaMedio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        act = (Act)mContext;
        user = (User)mContext;
        setContentView(R.layout.activity_main);
        load();
        setGauge();
        btnAddActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreateActivity.class);
                startActivity(intent);
                finish();

            }
        });
        btnStatsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StatsActivity.class);
                startActivity(intent);
                finish();

            }
        });

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
    private void load() {
        btnAddActivity = (ImageButton) findViewById(R.id.btnNewAct);
        btnStatsActivity = (ImageButton) findViewById(R.id.btnStats);
        mProgressView = findViewById(R.id.main_progress);
        mScrollView = findViewById(R.id.ScrlViewMain);
        txtMain = (TextView)findViewById(R.id.txtAvgMain);
        txtAvgAct = (TextView)findViewById(R.id.txtKMAStatus);
        gaugeBlue = (ImageView)findViewById(R.id.gaugeBlue);
        gaugeRed = (ImageView)findViewById(R.id.gaugeRed);
        gaugeGreen = (ImageView)findViewById(R.id.gaugeGreen);
        kmaMedio = -123125123123f;
    }
    private void setGauge() {
        Util.showProgress(true,mContext,mScrollView,mProgressView);
        if(user.getUserId()==0){
            mAuthTask = new UserTask();
            mAuthTask.execute((Void) null);
        }

        if(kmaMedio<-12312f){
            try {
                mKmaTask = new KmaTask();
                mKmaTask.execute((Void)null).get();
            } catch (Exception e) {
                Util.error("MAIN_AVG_KMA_GET",e.getMessage(),mContext);
            }
        }


        /*

[-1, -0.25]
Baixo KMA
Estudante não estimou corretamente seu conhecimento na maioria das situações
[-0.25, 0.5]
Médio KMA
O estudante, por vezes, estimou corretamente, mas pouco freqüentes foram as estimativas parcialmente erradas ou completamente erradas.
[0.5, 1]
￼
Alto KMA

         */
        if(kmaMedio>=-12312f){
            if(isBetween(kmaMedio,0.5F,1F)){
                gaugeGreen.setVisibility(View.INVISIBLE);
                gaugeRed.setVisibility(View.VISIBLE);
                gaugeBlue.setVisibility(View.INVISIBLE);
                txtMain.setText(getResources().getString(R.string.high));
            }
            else if(isBetween(kmaMedio,-0.25F,0.5F)){
                gaugeGreen.setVisibility(View.VISIBLE);
                gaugeRed.setVisibility(View.INVISIBLE);
                gaugeBlue.setVisibility(View.INVISIBLE);
                txtMain.setText(getResources().getString(R.string.middle));

            }
            else{
                gaugeGreen.setVisibility(View.INVISIBLE);
                gaugeRed.setVisibility(View.INVISIBLE);
                gaugeBlue.setVisibility(View.VISIBLE);
                txtMain.setText(getResources().getString(R.string.low));

            }
            txtAvgAct.setText(getString(R.string.main_average_value));
            btnStatsActivity.setVisibility(View.VISIBLE);
        }
        Util.showProgress(false,mContext,mScrollView,mProgressView);
    }
    public static boolean isBetween(Float x, Float lower, Float upper) {
        return (x>=lower && x <= upper);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(logout);
            finish();
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
            MainActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }
    public class UserTask extends AsyncTask<Void, Void, Boolean> {
        private User usuario;
        private IntegrateWS client = null;
        UserTask() {}

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
                            if (usuario.getUserId()!=0) {
                                user.setUserId(usuario.getUserId());
                                success = true;
                            } else
                                success = false;
                        }
                    }
                }
                else
                    success = false;
            }catch (Exception e) {
                Util.error("ERROR_CONNECTION", e.getMessage(),mContext);
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }
    }
    public class KmaTask extends AsyncTask<Void, Void, Boolean> {
        private IntegrateWS client = null;
        KmaTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = false;
            try {
                if (user.getUsername() != null) {
                    client = new IntegrateWS(Util.getUrl(R.string.url_ws_get_avg_kma,mContext));
                    client.AddHeader("Accept", "application/json");
                    client.AddHeader("Content-type", "application/json");
                    client.AddParam("content", user.toJson());

                    client.Execute(RequestMethod.POST);
                    String a = client.getResponse();
                    client.getErrorMessage();
                    if (client.getResponseCode() == 200) {
                        if (a != null) {
                            kmaMedio = Float.parseFloat(a);
                            success = true;
                        }
                    }
                }
                else
                    success = false;
            }catch (Exception e) {
                Util.error("ERROR_CONNECTION", e.getMessage(),mContext);
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
            mKmaTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }
    }
}
