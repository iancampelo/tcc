package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


public class PreReflectionActivity extends Activity {
    public static String nameAct=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_reflection);
        load();

    }

    private void load() {
        //TODO Find how to set the text into the edittext

        String txtName = getIntent().getExtras().getString("nameAct");
        final Act act = (Act)getIntent().getExtras().get("varAct");


        EditText txtPreName = (EditText) findViewById(R.id.inpNamePre);
        Log.v("NOME ACT",txtName);
        txtPreName.setHint(txtName);
        //Spinner Attention
        Spinner spinner = (Spinner) findViewById(R.id.spinAttentionDegree);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.attention_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        act.setGrauAtencao(spinner.getSelectedItem().toString());
        //act.setRecursos();
        //act.setEstrategia();
        ImageButton btn = (ImageButton) findViewById(R.id.btnNextPre);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FamilyActivity.class);
                nameAct = ((EditText)findViewById(R.id.inpNamePre)).getText().toString();
                intent.putExtra("nameAct",nameAct);
                intent.putExtra("varAct",act);
                startActivity(intent);
            }
        });

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
            //TODO Implementar um Logout real, que não volte para a Activity anterior
            //TODO Implementar pegar as horas com o NumberPicker
            //TODO usar imagem do botão, assim como está no Wireframe
            Intent logout = new Intent(PreReflectionActivity.this, LoginActivity.class);
            PreReflectionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}
