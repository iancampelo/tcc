package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView txtMain = new TextView(this);
//        txtMain.findViewById(R.id.txtMain);
//        txtMain.setText("Welcome, Ian Campelo");
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
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(logout);
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
            MainActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }
}
