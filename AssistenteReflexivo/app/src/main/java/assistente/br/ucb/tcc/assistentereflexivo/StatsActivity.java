package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class StatsActivity extends Activity {
    private TextView gradeKma, gradeKmb;
    private ImageView gaugeBlueKma, gaugeGreenKma, gaugeRedKma,gaugeBlueKmb,gaugeGreenKmb, gaugeRedKmb;
    private ImageButton btnBackAdd, btnShowActs;
    private static Context mContext;
    private static Act act;
    private static User user;
    private static ArrayList<Act> acts;
    private static View mScrollView,mProgressView;
    private ListActsTask mListTask;
    private KmaKmbTask mKmaKmbTask = null;
    private static Float indiceMedio = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mContext = getApplicationContext();
        act = (Act)mContext;
        user = (User)mContext;
        load();
        Util.setmContext(mContext);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void load() {
        mScrollView     = findViewById(R.id.ScrlViewStats);
        mProgressView   = findViewById(R.id.stats_progress);
        gradeKmb        = (TextView)findViewById(R.id.txtKmbStats);
        gradeKma        = (TextView)findViewById(R.id.txtKmaStats);
        gaugeBlueKmb    = (ImageView)findViewById(R.id.imgBlueKmb);
        gaugeRedKmb     = (ImageView)findViewById(R.id.imgRedKmb);
        gaugeGreenKmb   = (ImageView)findViewById(R.id.imgGreenKmb);
        gaugeBlueKma    = (ImageView)findViewById(R.id.imgBlueKma);
        gaugeGreenKma   = (ImageView)findViewById(R.id.imgGreenKma);
        gaugeRedKma     = (ImageView)findViewById(R.id.imgRedKma);
        btnBackAdd      = (ImageButton)findViewById(R.id.btnBackAdd);
        btnShowActs     = (ImageButton) findViewById(R.id.btnShowActs);
        indiceMedio     = -123125123123f;
        btnBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btnShowActs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActList();
            }
        });

        setGauge(gradeKma,true);
        setGauge(gradeKmb,false);

    }

    private void getActList() {
        Util.showProgress(true,mContext,mScrollView,mProgressView);
        mListTask = new ListActsTask();
        try {
            mListTask.execute((Void) null).get();
        } catch (Exception e) {
            Util.error("GET_ACTS",e.getMessage().toString(),mContext);
        }
        Util.showProgress(false,mContext,mScrollView,mProgressView);
    }

    private void setGauge(TextView txtStatus, boolean isKma) {
        if(isKma) {
            Util.showProgress(true,mContext,mScrollView,mProgressView);
            if(indiceMedio <-12312f){
                try {
                    mKmaKmbTask = new KmaKmbTask(R.string.url_ws_get_avg_kma);
                    mKmaKmbTask.execute((Void) null).get();
                } catch (Exception e) {
                    Util.error("MAIN_AVG_KMA_GET",e.getMessage(),mContext);
                }
            }
            if(indiceMedio >=-12312f){
                if(isBetween(indiceMedio,0.5F,1F)){
                    gaugeGreenKma.setVisibility(View.INVISIBLE);
                    gaugeRedKma.setVisibility(View.VISIBLE);
                    gaugeBlueKma.setVisibility(View.INVISIBLE);
                    txtStatus.setText(getResources().getString(R.string.optimistic));

                }
                else if(isBetween(indiceMedio,-0.25F,0.25F)){
                    gaugeGreenKma.setVisibility(View.VISIBLE);
                    gaugeRedKma.setVisibility(View.INVISIBLE);
                    gaugeBlueKma.setVisibility(View.INVISIBLE);
                    txtStatus.setText(getResources().getString(R.string.realistic));
                }
                else{
                    gaugeGreenKma.setVisibility(View.INVISIBLE);
                    gaugeRedKma.setVisibility(View.INVISIBLE);
                    gaugeBlueKma.setVisibility(View.VISIBLE);
                    txtStatus.setText(getResources().getString(R.string.pessimistic));
                }
            }

            Util.showProgress(false, mContext, mScrollView, mProgressView);
            indiceMedio     = -123125123123f;
        }else{
            if(indiceMedio<-12312f){
                try {
                    mKmaKmbTask = new KmaKmbTask(R.string.url_ws_get_avg_kmb);
                    mKmaKmbTask.execute((Void) null).get();
                } catch (Exception e) {
                    Util.error("MAIN_AVG_KMB_GET",e.getMessage(),mContext);
                }
            }
            if(indiceMedio >=-1.2312f){
                if(isBetween(indiceMedio,0.25f,1f)){
                    gaugeGreenKmb.setVisibility(View.INVISIBLE);
                    gaugeRedKmb.setVisibility(View.INVISIBLE);
                    gaugeBlueKmb.setVisibility(View.VISIBLE);
                    txtStatus.setText(getResources().getString(R.string.optimistic));
                }
                else if(isBetween(indiceMedio,-1f,-0.25f)){
                    gaugeGreenKmb.setVisibility(View.INVISIBLE);
                    gaugeRedKmb.setVisibility(View.VISIBLE);
                    gaugeBlueKmb.setVisibility(View.INVISIBLE);
                    txtStatus.setText(getResources().getString(R.string.pessimistic));
                }
                else if(indiceMedio==0){
                    gaugeGreenKmb.setVisibility(View.VISIBLE);
                    gaugeRedKmb.setVisibility(View.INVISIBLE);
                    gaugeBlueKmb.setVisibility(View.INVISIBLE);
                    txtStatus.setText(getResources().getString(R.string.realistic));
                }
                else {
                    gaugeGreenKmb.setVisibility(View.INVISIBLE);
                    gaugeRedKmb.setVisibility(View.INVISIBLE);
                    gaugeBlueKmb.setVisibility(View.VISIBLE);
                    txtStatus.setText(getResources().getString(R.string.random));
                }
            }
            Util.showProgress(false,mContext,mScrollView,mProgressView);
            indiceMedio     = -123125123123f;
        }
    }
    public static boolean isBetween(Float x, Float lower, Float upper) {
        return (x>=lower && x <= upper);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(StatsActivity.this,SettingsActivity.class);
            StatsActivity.this.startActivity(settings);
        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(StatsActivity.this, LoginActivity.class);
            StatsActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public class ListActsTask extends AsyncTask<Void, Void, Boolean> {

        private IntegrateWS client = null;
        ListActsTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            Looper.prepare();
            acts = new ArrayList<Act>();
            boolean success = false;
            try {
                if (user.getUsername() != null) {
                    client = new IntegrateWS(Util.getUrl(R.string.url_ws_list_act,mContext));
                    client.AddHeader("Accept", "application/json");
                    client.AddHeader("Content-type", "application/json");
                    client.AddParam("content", user.toJson());

                    client.Execute(RequestMethod.POST);
                    String a = client.getResponse();
                    client.getErrorMessage();
                    if (client.getResponseCode() == 200) {
                        if (a != null) {
                            acts = (Util.jsonToActList(a));
                            if(acts!=null)
                                success = true;
                        } else
                            success = false;
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
            if(success) {
                Intent intent = new Intent(mContext, ListActivity.class);
                intent.putExtra("acts",acts);
                startActivity(intent);
            }else{
                Util.error("ERROR_CONNECTION", getString(R.string.error),mContext);
            }
        }

        @Override
        protected void onCancelled() {
            mListTask.notifyAll();
            mListTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }
    }
    public class KmaKmbTask extends AsyncTask<Void, Void, Boolean> {
        private IntegrateWS client = null;
        private int url;
        KmaKmbTask(int _url) {
            url = _url;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = false;
            try {
                if (user.getUsername() != null) {
                    client = new IntegrateWS(Util.getUrl(url,mContext));
                    client.AddHeader("Accept", "application/json");
                    client.AddHeader("Content-type", "application/json");
                    client.AddParam("content", user.toJson());

                    client.Execute(RequestMethod.POST);
                    String a = client.getResponse();
                    client.getErrorMessage();
                    if (client.getResponseCode() == 200) {
                        if (a != null) {
                            indiceMedio = Float.parseFloat(a);
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
            mKmaKmbTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }
    }


}
