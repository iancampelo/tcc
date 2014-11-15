package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class FamilyActivity extends Activity {
    public EditText inpActFam,inpProblem,inpObjv;
    private static Act act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        load();
    }


    private void load() {
        act = (Act)getApplicationContext();

        inpActFam = (EditText) findViewById(R.id.inpNameFam);
        inpProblem = (EditText)findViewById(R.id.inpProblem);
        inpObjv = (EditText)findViewById(R.id.inpActGoal);
        inpActFam.setHint(act.getNome());
        ImageButton btn = (ImageButton) findViewById(R.id.btnNextFam);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setObjetivo(inpObjv.getText().toString());
                act.setComprensao(inpProblem.getText().toString());
                Intent intent = new Intent(view.getContext(), ProductionActivity.class);
                startActivity(intent);
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
                        System.exit(0);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
    private boolean checkFields() {
        inpObjv.setError(null);
        inpProblem.setError(null);
        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(inpProblem.getText().toString().trim())){
            inpProblem.setError(getString(R.string.error_field_required));
            focus=inpProblem;
            focus.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(inpObjv.getText().toString().trim())){
            inpObjv.setError(getString(R.string.error_field_required));
            focus=inpObjv;
            focus.requestFocus();
            valid = false;
        }
        return valid;
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
            Intent settings = new Intent(FamilyActivity.this,SettingsActivity.class);
            FamilyActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(FamilyActivity.this, LoginActivity.class);
            FamilyActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
