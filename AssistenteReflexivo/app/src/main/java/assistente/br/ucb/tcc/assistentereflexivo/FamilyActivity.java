package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class FamilyActivity extends Activity {
    public static String nameAct=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        load();
    }
//TODO implement Logout
    private void load() {
        EditText txtPreName = (EditText) findViewById(R.id.inpNameFam);
        txtPreName.setHint(getIntent().getExtras().getString("nameAct"));

        ImageButton btn = (ImageButton) findViewById(R.id.btnNextFam);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EvaluationActivity.class);
                nameAct = ((EditText)findViewById(R.id.inpNameFam)).getText().toString();
                intent.putExtra("nameAct",nameAct);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.family, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
