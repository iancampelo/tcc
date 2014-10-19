package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

    private ImageView gaugeBlue, gaugeRed, gaugeGreen;
    private TextView txtMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO Fazer método que pega todas as atividade do usuário e faz a média, do status das atividades
        //TODO O nível KMB não pode ser String, pois temos que ver em qual nível ele esta, então a comparação de níveis vai ser
        //TODO if(nivel == 0 /*pessimista*/)
        //TODO switch(qntAtividades)
        //TODO case: >2 {nível 1}
        //TODO case: >4 {nível 2}
        //TODO case: >5 {nível 3}
        //TODO case: >6 {nivel 4}
        //TODO default: nível 0
        load();
        setGauge();

        //TODO -PEGAR IMAGEM DE FUNDO COM O GAUGE, E SOBREPOR COM OS INDICADORES, DE ACORDO COM O STATUS DO KMB
        //TODO -SE FOR -1 OCULTA O 0 E O 1, E POR AI VAI...
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

    private void load() {
        txtMain = (TextView)findViewById(R.id.txtAvgMain);
        gaugeBlue = (ImageView)findViewById(R.id.gaugeBlue);
        gaugeRed = (ImageView)findViewById(R.id.gaugeRed);
        gaugeGreen = (ImageView)findViewById(R.id.gaugeGreen);
    }

    private void setGauge() {
        //if(Act.media != 0)
        //switch(act.media)
        //case 1:
        gaugeGreen.setVisibility(View.INVISIBLE);
        gaugeRed.setVisibility(View.VISIBLE);
        gaugeBlue.setVisibility(View.INVISIBLE);
        txtMain.setText("PESSIMISTA");

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
