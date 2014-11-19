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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        gaugeBlue = (ImageView)findViewById(R.id.imageViewBlue);
        gaugeGreen = (ImageView)findViewById(R.id.imageViewGreen);
        gaugeRed = (ImageView)findViewById(R.id.imageViewRed);
        txtStatus = (TextView)findViewById(R.id.textView3);
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
                        input.setText(act.getAnotacoes() + "\n");
                }
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
                saveAct();

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
        //TODO fazer calculo do KMB da atividade desenvolvida e do kma
        //if(Act.media != 0)
        //switch(act.media)
        //case 1:
        gaugeGreen.setVisibility(View.INVISIBLE);
        gaugeRed.setVisibility(View.INVISIBLE);
        gaugeBlue.setVisibility(View.VISIBLE);
        txtStatus.setText(getString(R.string.optimistic));

    }

    private String getTimeActElapsed() {
        return Integer.toString(act.getTempoGasto().getHours())+":"+Integer.toString(act.getTempoGasto().getMinutes())+
                ":"+Integer.toString(act.getTempoGasto().getSeconds());
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
                client = new IntegrateWS(Util.getUrl(R.string.url_ws_alter_act,mContext));
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
                Intent myIntent = new Intent(PostReflectionActivity.this, ProductionActivity.class);
                PostReflectionActivity.this.startActivity(myIntent);
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
