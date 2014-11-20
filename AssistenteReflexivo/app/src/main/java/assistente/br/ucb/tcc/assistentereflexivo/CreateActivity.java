package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;


public class CreateActivity extends Activity implements NumberPicker.OnValueChangeListener{
    private static Act act = null;
    private static User user= null;
    private static Context mContext = null;
    private EditText inpName;
    private Spinner spinner;
    private NumberPicker npHrs;
    private ImageButton btnNextCreate;
    private NumberPicker npMin;
    private NumberPicker npSec;
    private Enumerators enums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mContext = getApplicationContext();
        act = (Act)mContext;
        user = (User)mContext;
        load();
    }

    public void load() {
        act.setUserid(user.getUserId());
        btnNextCreate = (ImageButton) findViewById(R.id.btnNextCreate);
        enums = new Enumerators();
        enums.setmContext(mContext);


        inpName = (EditText) findViewById(R.id.inpName);
        spinner = (Spinner) findViewById(R.id.spinPrediction);

        spinner.setAdapter(new ArrayAdapter<Enumerators.EnumSpinPrediction>(this,
                android.R.layout.simple_spinner_dropdown_item, Enumerators.EnumSpinPrediction.values()));

        //HOURS
        npHrs = (NumberPicker) findViewById(R.id.numHrs);
        npHrs.setMaxValue(23);
        npHrs.setMinValue(0);
        npHrs.setOnValueChangedListener(this);

        //MINUTES
        npMin = (NumberPicker) findViewById(R.id.numMin);
        npMin.setMaxValue(59);
        npMin.setMinValue(0);
        npMin.setOnValueChangedListener(this);

        //SECS
        npSec = (NumberPicker) findViewById(R.id.numSecs);
        npSec.setMaxValue(59);
        npSec.setMinValue(0);
        npSec.setOnValueChangedListener(this);

        btnNextCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!checkFields()){
                    return;
                }
                act.setNome(inpName.getText().toString());
                act.setPredicao(Enumerators.EnumSpinPrediction.findIDbyString(spinner.getSelectedItem().toString()));
                Time time = new Time(0);
                time.setSeconds(npSec.getValue());
                time.setMinutes(npMin.getValue());
                time.setHours(npHrs.getValue());
                act.setTempoEstimado(time);
                act.setUserid(((User)mContext).getUserId());

                finish();
                Intent myIntent = new Intent(CreateActivity.this, PreReflectionActivity.class);
                CreateActivity.this.startActivity(myIntent);
            }
        });
    }

    private boolean checkFields() {
        inpName.setError(null);
        String name = inpName.getText().toString();

        View focus = null;
        boolean valid = true;

        if(TextUtils.isEmpty(name)){
            inpName.setError(getString(R.string.error_field_required));
            focus=inpName;
            focus.requestFocus();
            valid = false;
        }
        if((npMin.getValue()==0)&&(npHrs.getValue()==0)&&(npSec.getValue()==0)){
            Toast myToast = Toast.makeText(this, R.string.error_numberpicker, Toast.LENGTH_SHORT);
            myToast.show();
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
        if(id == R.id.action_logout){
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(CreateActivity.this,SettingsActivity.class);
            CreateActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){

            case R.id.numSecs:
                int ValSecs = newVal;
                break;
            case R.id.numHrs:
                int ValHrs = newVal;
                break;
            case R.id.numMin:
                int ValMin = newVal;
                break;
        }
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
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        System.exit(0);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}

