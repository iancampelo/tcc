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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;


public class ProductionActivity extends Activity {
    public EditText inpTimePre, inpTimeElapsed, inpActProd;
    private static Act act = null;
    public ImageButton btnPauseTime, btnPlayTime, btnNextProd,btnAddNote;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    private String note;
    private long startTime = 0L;
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
        btnPlayTime = (ImageButton) findViewById(R.id.btnPlayTime);
        btnPlayTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                myHandler.postDelayed(updateTimerMethod, 0);

            }

        });
        btnPauseTime = (ImageButton) findViewById(R.id.btnPauseTime);
        btnPauseTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeSwap += timeInMillies;
                myHandler.removeCallbacks(updateTimerMethod);
            }

        });

        btnNextProd = (ImageButton)findViewById(R.id.btnNextProd);
        btnNextProd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Pause time
                timeSwap += timeInMillies;
                myHandler.removeCallbacks(updateTimerMethod);
                act.setTempoGasto(getTimeFromField());
                act.setAnotacoes(note);
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

    private Runnable updateTimerMethod = new Runnable() {

        //TODO Add hours in the stopwatch
        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;
            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            inpTimeElapsed.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };

    private String getTimeAct() {
        return Integer.toString(act.getTempoEstimado().getHours())+":"+Integer.toString(act.getTempoEstimado().getMinutes())+
                ":"+Integer.toString(act.getTempoEstimado().getSeconds());
    }

    private Time getTimeFromField(){
        String[] times = inpTimeElapsed.getText().toString().split(":");
        Time time = new Time(0);
        time.setSeconds(Integer.parseInt(times[0]));
        time.setMinutes(Integer.parseInt(times[1]));
        time.setHours(Integer.parseInt(times[2]));
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
                        finish();
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
