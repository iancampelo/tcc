package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


public class PreReflectionActivity extends Activity {
    private static Act act = null;
    public Spinner spinAtt;
    public EditText txtStrategy,txtResource,txtPreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_reflection);
        load();

    }

    private void load() {

        act = (Act)getApplicationContext();
        txtStrategy = (EditText)findViewById(R.id.inpStrategy);
        txtResource = (EditText)findViewById(R.id.inpResources);
        txtPreName = (EditText) findViewById(R.id.inpNamePre);
        Log.v("NOME ACT",act.getNome());
        txtPreName.setHint(act.getNome());
        //Spinner Attention
        spinAtt = (Spinner) findViewById(R.id.spinAttentionDegree);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.attention_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAtt.setAdapter(adapter);

        ImageButton btn = (ImageButton) findViewById(R.id.btnNextPre);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setGrauAtencao(spinAtt.getSelectedItem().toString());
                act.setRecursos(txtResource.getText().toString());
                act.setEstrategia(txtStrategy.getText().toString());
                Intent intent = new Intent(view.getContext(), FamilyActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean checkFields() {
        txtStrategy.setError(null);
        txtResource.setError(null);
        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(txtStrategy.getText().toString().trim())){
            txtStrategy.setError(getString(R.string.error_field_required));
            focus=txtStrategy;
            focus.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(txtResource.getText().toString().trim())){
            txtResource.setError(getString(R.string.error_field_required));
            focus=txtStrategy;
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
            Intent settings = new Intent(PreReflectionActivity.this,SettingsActivity.class);
            PreReflectionActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(PreReflectionActivity.this, LoginActivity.class);
            PreReflectionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}
