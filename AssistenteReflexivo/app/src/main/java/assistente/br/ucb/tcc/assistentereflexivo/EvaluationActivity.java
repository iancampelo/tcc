package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


public class EvaluationActivity extends Activity {

    private EditText inpTimePre, inpTimeElapsed, inpActProd;
    private static Act act = null;
    private ImageButton btnNextEval;
    private Spinner spinEval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        load();
    }

    private void load() {

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
                act.setPredicao(spinEval.getSelectedItem().toString());

                Intent intent = new Intent(view.getContext(), EvaluationActivity.class);
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
