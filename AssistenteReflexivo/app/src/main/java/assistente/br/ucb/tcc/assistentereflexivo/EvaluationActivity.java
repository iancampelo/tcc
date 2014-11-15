package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class EvaluationActivity extends Activity {

    private EditText inpTimePre, inpTimeElapsed, inpActProd;
    private static Act act = null;
    private ImageButton btnNextEval;
    private Spinner spinEval;
    private static Context mContext;
    private boolean doubleBackToExitPressedOnce;

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



//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, getString(R.string.msg_back_again), Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        mContext = getApplicationContext();
        load();
    }

    private void load() {
        act = (Act)getApplicationContext();
        spinEval = (Spinner) findViewById(R.id.spinEvalAct);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.predict_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEval.setAdapter(adapter);
        inpTimePre = (EditText)findViewById(R.id.inpTimeExe);
        inpTimePre.setText(getTimeAct());
        inpTimeElapsed = (EditText)findViewById(R.id.inpTimePos);
        inpTimeElapsed.setText(getTimeActElapsed());
        inpActProd = (EditText)findViewById(R.id.inpActEval);
        inpActProd.setText(act.getNome());
        btnNextEval=(ImageButton)findViewById(R.id.btnNextEval);
        btnNextEval.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //act.setPredicao(spinEval.getSelectedItem().toString());

                finish();
                Intent intent = new Intent(view.getContext(), PostReflectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private String getTimeAct() {
        return Integer.toString(act.getTempoEstimado().getHours())+":"+Integer.toString(act.getTempoEstimado().getMinutes())+
                ":"+Integer.toString(act.getTempoEstimado().getSeconds());
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
            Intent settings = new Intent(EvaluationActivity.this,SettingsActivity.class);
            EvaluationActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(EvaluationActivity.this, LoginActivity.class);
            EvaluationActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
