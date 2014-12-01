package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;

import java.sql.Time;


public class ProductionActivity extends Activity {
    public EditText inpTimePre, inpTimeElapsed, inpActProd;
    private static Act act = null;
    public ImageButton btnPlayPauseTime, btnNextProd,btnAddNote;
    private String note;
    private Chronometer timeElapsed;
    private long startTime = 0L;
    private static boolean playClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);
        load();
    }

    private void load() {
        act = (Act)getApplicationContext();
        inpTimePre      = (EditText)findViewById(R.id.inpTimeExe);
        inpTimeElapsed  = (EditText)findViewById(R.id.inpTimePos);
        inpActProd      = (EditText)findViewById(R.id.inpActProd);
        inpActProd.setHint(act.getNome());
        act.getTempoEstimado().getTime();
        inpTimePre.setText(getTimeAct());
        timeElapsed  = (Chronometer) findViewById(R.id.chrono);
        timeElapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        btnPlayPauseTime = (ImageButton) findViewById(R.id.btnPlayPauseTime);
        btnPlayPauseTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!playClick) {
                    timeElapsed.setBase(SystemClock.elapsedRealtime()+startTime);
                    timeElapsed.start();
                    playClick=true;
                    btnPlayPauseTime.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
                }else{
                    startTime = timeElapsed.getBase() - SystemClock.elapsedRealtime();
                    timeElapsed.stop();
                    playClick=false;
                    btnPlayPauseTime.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                }
            }
        });
        btnNextProd = (ImageButton)findViewById(R.id.btnNextProd);
        btnNextProd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                timeElapsed.stop();
                act.setTempoGasto(getTimeFromField());
                act.setAnotacoes(note);
                finish();
                Intent intent = new Intent(view.getContext(), EvaluationActivity.class);
                startActivity(intent);
            }
        });
        btnAddNote = (ImageButton) findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getResources().getString(R.string.notes));

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                if(note!= null)
                    input.setText(note);
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

    }

    private String getTimeAct() {
        return Integer.toString(act.getTempoEstimado().getHours())+":"+Integer.toString(act.getTempoEstimado().getMinutes())+
                ":"+Integer.toString(act.getTempoEstimado().getSeconds());
    }

    private Time getTimeFromField(){
        String[] times = timeElapsed.getText().toString().split(":");
        Time time = new Time(0);
        time.setSeconds(Integer.parseInt(times[2]));
        time.setMinutes(Integer.parseInt(times[1]));
        time.setHours(Integer.parseInt(times[0]));
        return time;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(ProductionActivity.this,SettingsActivity.class);
            ProductionActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(ProductionActivity.this, LoginActivity.class);
            ProductionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
