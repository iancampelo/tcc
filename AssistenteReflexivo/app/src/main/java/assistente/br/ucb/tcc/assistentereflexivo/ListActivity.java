package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class ListActivity extends Activity {
    private static Act act;
    private static User user;
    private static ArrayList<Act> acts;
    private static View mScrollView,mProgressView;
    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = getApplicationContext();
        Intent myIntent = getIntent();
        acts = (ArrayList<Act>)myIntent.getSerializableExtra("acts");
        act = (Act) mContext;
        user = (User) mContext;
        mProgressView = findViewById(R.id.list_progress);
        mScrollView = findViewById(R.id.ScrlViewList);
        if(acts != null){

            final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rel_list);
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.topMargin = 150;

            final ListView list = new ListView(this);

            ArrayAdapter<String> adp = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_dropdown_item_1line, Util.actsToNames(acts));
            adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            list.setAdapter(adp);
            list.setLayoutParams(params);

            rl.addView(list);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            Intent logout = new Intent(ListActivity.this, LoginActivity.class);
            ListActivity.this.startActivity(logout);
            finish();
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(ListActivity.this,SettingsActivity.class);
            ListActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
