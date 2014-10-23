package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class StatsActivity extends Activity {
    TextView gradeKma, gradeKmb;
    ImageView gaugeBlueKma, gaugeGreenKma, gaugeRedKma,gaugeBlueKmb,gaugeGreenKmb, gaugeRedKmb;
    ImageButton btnBackAdd, btnShowActs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        load();
        setGauge(gradeKma,true);
        setGauge(gradeKmb,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void load() {
        gradeKma        = (TextView)findViewById(R.id.textView);
        gradeKmb        = (TextView)findViewById(R.id.textView3);
        gaugeBlueKma    = (ImageView)findViewById(R.id.imageViewBlue);
        gaugeRedKma     = (ImageView)findViewById(R.id.imageViewRed);
        gaugeGreenKma   = (ImageView)findViewById(R.id.imageViewGreen);
        gaugeBlueKmb    = (ImageView)findViewById(R.id.imageViewBlue2);
        gaugeGreenKmb   = (ImageView)findViewById(R.id.imageViewGreen2);
        gaugeRedKmb     = (ImageView)findViewById(R.id.imageViewRed2);
        btnBackAdd      = (ImageButton)findViewById(R.id.btnBackAdd);
        btnBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salva todas as alterações da atividade!!!
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnShowActs     =(ImageButton)findViewById(R.id.btnShowActs);
        btnShowActs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CALL POPUP WITH LAYOUT E LISTVIEW DE TODAS AS ATIVIDADES DAQUELE USER
                //TODO AÇÃO DE DELETAR E AÇÃO DE VER ATIVIDADE, SE FOR VER, PEGA OS DADOS E ATUALIZA ACT
                //TODO SE FOR DELETAR PEGA O ID E CHAMA A FUNÇÃO
                finish();
            }
        });

    }

    private void setGauge(TextView txtStatus, boolean isKma) {
        if(isKma) {
            //if(Act.mediaKma != NULL)
            //switch(act.media)
            //case 1:
            gaugeGreenKma.setVisibility(View.INVISIBLE);
            gaugeRedKma.setVisibility(View.INVISIBLE);
            gaugeBlueKma.setVisibility(View.VISIBLE);
            txtStatus.setText(getResources().getString(R.string.optimistic));
        }
        else{
            //if(Act.mediaKma != NULL)
            //switch(act.media)
            //case 1:
            gaugeGreenKmb.setVisibility(View.INVISIBLE);
            gaugeRedKmb.setVisibility(View.VISIBLE);
            gaugeBlueKmb.setVisibility(View.INVISIBLE);
            txtStatus.setText(getResources().getString(R.string.pessimistic));
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(StatsActivity.this,SettingsActivity.class);
            StatsActivity.this.startActivity(settings);
        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(StatsActivity.this, LoginActivity.class);
            StatsActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
