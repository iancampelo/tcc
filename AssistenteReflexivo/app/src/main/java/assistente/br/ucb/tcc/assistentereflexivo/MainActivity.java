package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//
//import com.ikovac.timepickerwithseconds.view.MyTimePickerDialog;
//import com.ikovac.timepickerwithseconds.view.TimePicker;
//
//import java.util.Calendar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtMain = (TextView)findViewById(R.id.txtAvgMain);
        txtMain.setText("PESSIMISTA");
        Button btnAddActivity = (Button) findViewById(R.id.btnNewAct);
        btnAddActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateActivity.class);
                startActivity(intent);
            }
        });

//        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
//
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
//                // TODO Auto-generated method stub
//                /*time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
//                        ":" + String.format("%02d", minute) +
//                        ":" + String.format("%02d", seconds));  */
//            }
//        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
//        mTimePicker.show();
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
            finish();
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
            MainActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }


    //@TODO Se existe outros projetos tornar a visibilidade do btnStats ativo. Senão deixar inativa. (Não só a visibilidade como a ação)

}
