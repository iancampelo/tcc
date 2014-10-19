package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class ProductionActivity extends Activity {
    public EditText inpTimePre, inpTimeElapsed, inpActProd;
    private static Act act = null;
    public ImageButton btnPauseTime, btnPlayTime, btnNextProd,btnAddNote;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
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

                if(!checkFields()){
                    return;
                }
            }


        });

        btnAddNote = (ImageButton) findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //add note, make
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //close dialog
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(R.string.confirm_registry).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();

            }
        });

    }

    private boolean checkFields() {
        return false;

    }

    private Runnable updateTimerMethod = new Runnable() {

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
            //TODO Implementar um Logout real, que não volte para a Activity anterior
            //TODO Implementar pegar as horas com o NumberPicker
            //TODO usar imagem do botão, assim como está no Wireframe
            Intent logout = new Intent(ProductionActivity.this, LoginActivity.class);
            ProductionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
