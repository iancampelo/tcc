package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class CreateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_logout){
            //TODO Implementar um Logout real, que não volte para a Activity anterior
            //TODO Implementar pegar as horas com o NumberPicker
            //TODO usar imagem do botão, assim como está no Wireframe
            Intent logout = new Intent(CreateActivity.this, LoginActivity.class);
            CreateActivity.this.startActivity(logout);
            finish();
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(CreateActivity.this,SettingsActivity.class);
            CreateActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }
}
