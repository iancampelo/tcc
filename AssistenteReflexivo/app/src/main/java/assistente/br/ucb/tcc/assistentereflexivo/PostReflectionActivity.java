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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


public class PostReflectionActivity extends Activity {
    private EditText inpActPost, inpTimeExe;
    private static Act act;
    private TextView txtStatus;
    private ImageView gaugeBlue, gaugeGreen, gaugeRed;
    private ImageButton btnAddNote, btnNextPost;
    private String note;
    private ActSaveTask mActSaveTask;
    private IntegrateWS client = null;
    private View mProgressView, mScrollView;
    private static Context mContext = null;
    private boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reflection);
        mContext = getApplicationContext();
        Intent a = getIntent();
        load();
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
        act = (Act)getApplicationContext();
        mProgressView = findViewById(R.id.post_progress);
        mScrollView = findViewById(R.id.ScrlViewPost);
        inpTimeExe = (EditText)findViewById(R.id.inpTimeExe);
        inpTimeExe.setText(getTimeActElapsed());
        inpActPost = (EditText)findViewById(R.id.inpActPost);
        inpActPost.setText(act.getNome());
        gaugeBlue = (ImageView)findViewById(R.id.imgBlueKmb);
        gaugeGreen = (ImageView)findViewById(R.id.imgGreenKmb);
        gaugeRed = (ImageView)findViewById(R.id.imgRedKmb);
        txtStatus = (TextView)findViewById(R.id.txtKMBStatus);
        btnAddNote = (ImageButton)findViewById(R.id.btnAddNotePost);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getResources().getString(R.string.notes));

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                if(act.getAnotacoes() != null) {
                    if (!act.getAnotacoes().isEmpty())
                        input.setText(act.getAnotacoes() + " ");
                }
                if(note!= null)
                    input.setText(note);
                builder.setView(input);
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note = input.getText().toString();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        btnNextPost = (ImageButton)findViewById(R.id.btnNextPost);
        btnNextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.setAnotacoes(note);
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
                if(saveAct()){
                    finish();
                    Intent myIntent = new Intent(PostReflectionActivity.this, StatsActivity.class);
                    PostReflectionActivity.this.startActivity(myIntent);
                }

            }
        });
        setGauge();
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void setGauge() {
        Util.showProgress(true, mContext, mScrollView, mProgressView);

        if(act.getKmb() >=-1d){
            if(isBetween(act.getKmb(),0.25d,1d)){
                gaugeGreen.setVisibility(View.INVISIBLE);
                gaugeRed.setVisibility(View.INVISIBLE);
                gaugeBlue.setVisibility(View.VISIBLE);
                txtStatus.setText(getResources().getString(R.string.optimistic));
            }
            else if(isBetween(act.getKmb(),-1d,-0.25d)){
                gaugeGreen.setVisibility(View.INVISIBLE);
                gaugeRed.setVisibility(View.VISIBLE);
                gaugeBlue.setVisibility(View.INVISIBLE);
                txtStatus.setText(getResources().getString(R.string.pessimistic));
            }
            else if(act.getKmb()==0){
                gaugeGreen.setVisibility(View.VISIBLE);
                gaugeRed.setVisibility(View.INVISIBLE);
                gaugeBlue.setVisibility(View.INVISIBLE);
                txtStatus.setText(getResources().getString(R.string.realistic));
            }
            else {
                gaugeGreen.setVisibility(View.INVISIBLE);
                gaugeRed.setVisibility(View.INVISIBLE);
                gaugeBlue.setVisibility(View.VISIBLE);
                txtStatus.setText(getResources().getString(R.string.random));
            }
        }
        Util.showProgress(false,mContext,mScrollView,mProgressView);
    }

    /*
    0
￼
0 Realista
O aprendiz faz uma estimativa exata de seu conhecimento, tendo uma alta KMA.

[0.25, 1]
Otimista
O aprendiz tende a estimar que pode resolver os problemas, mas ele não consegue, na maioria das situações

[-1, -0.25]
Pessimista
O aprendiz tende a estimativa de que não pode resolver os problemas, mas ele consegue
[-0.25, 0.25]
Randômico
     */

    public static boolean isBetween(Double x, Double lower, Double upper) {
        return (x>=lower && x <= upper);
    }
    private String getTimeActElapsed() {
        String a = Integer.toString(act.getTempoGasto().getHours())+":"+Integer.toString(act.getTempoGasto().getMinutes())+
                ":"+Integer.toString(act.getTempoGasto().getSeconds());
        return a;
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
            Intent settings = new Intent(PostReflectionActivity.this,SettingsActivity.class);
            PostReflectionActivity.this.startActivity(settings);
        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(PostReflectionActivity.this, LoginActivity.class);
            PostReflectionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveAct() {
        setGauge();
        success = false;
        Util.showProgress(true,mContext,mScrollView,mProgressView);
        mActSaveTask = new ActSaveTask();
        try {
            mActSaveTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Util.showProgress(false,mContext,mScrollView,mProgressView);
        return success;
    }

    public class ActSaveTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            success = false;
            try {
                client = new IntegrateWS(Util.getUrl(R.string.url_ws_alter_act,mContext));
                client.AddHeader("Accept", "application/json");
                client.AddHeader("Content-type", "application/json");
                client.AddParam("content", act.toJsonAct());
                client.Execute(RequestMethod.POST);
                if (client.getResponseCode() == 200) {
                    success = true;
                }
            }catch (Exception e) {
                Util.error("ERROR_CONNECTION", e.getMessage(),mContext);
                success = false;
            }
            return success;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mActSaveTask = null;
            Util.showProgress(false, mContext, mScrollView, mProgressView);
            if (!success) {
                Util.error("ERROR_ON_SAVE_ACT_POST",getResources().getString(R.string.error),mContext);
            }
        }
        @Override
        protected void onCancelled() {
            mActSaveTask = null;
            Util.showProgress(false,mContext,mScrollView,mProgressView);
        }
    }


}
