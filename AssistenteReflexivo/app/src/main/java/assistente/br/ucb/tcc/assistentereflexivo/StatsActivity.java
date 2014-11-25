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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mContext = getApplicationContext();
        act = (Act)mContext;
        user = (User)mContext;
        load();
        setGauge(gradeKma,true);
        setGauge(gradeKmb,false);
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
        gradeKma        = (TextView)findViewById(R.id.textView);
        gradeKmb        = (TextView)findViewById(R.id.textView3);
        gaugeBlueKma    = (ImageView)findViewById(R.id.imageViewBlue);
        gaugeRedKma     = (ImageView)findViewById(R.id.imageViewRed);
        gaugeGreenKma   = (ImageView)findViewById(R.id.imageViewGreen);
        gaugeBlueKmb    = (ImageView)findViewById(R.id.imageViewBlue2);
        gaugeGreenKmb   = (ImageView)findViewById(R.id.imageViewGreen2);
        gaugeRedKmb     = (ImageView)findViewById(R.id.imageViewRed2);
        btnBackAdd      = (ImageButton)findViewById(R.id.btnBackAdd);
        btnShowActs     = (ImageButton) findViewById(R.id.btnShowActs);

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
            //if(Act.mediaKma != NULL)
            //switch(act.media)
            //case 1:
            gaugeGreenKma.setVisibility(View.VISIBLE);
            gaugeRedKma.setVisibility(View.INVISIBLE);
            gaugeBlueKma.setVisibility(View.INVISIBLE);
            txtStatus.setText(getResources().getString(R.string.optimistic));
        }else{
            //if(Act.mediaKma != NULL)
            //switch(act.media)
            //case 1:
            gaugeGreenKmb.setVisibility(View.INVISIBLE);
            gaugeRedKmb.setVisibility(View.VISIBLE);
            gaugeBlueKmb.setVisibility(View.INVISIBLE);
            txtStatus.setText(getResources().getString(R.string.pessimistic));
        }

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
}
