package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class PostReflectionActivity extends Activity {
    private EditText inpActPost, inpTimeExe;
    private static Act act = null;
    private TextView txtStatus;
    private ImageView gaugeBlue, gaugeGreen, gaugeRed;
    private ImageButton btnAddNote, btnNextPost;
    private String note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reflection);
        load();
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
                        finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
    private void load() {
        act = (Act)getApplicationContext();
        inpTimeExe = (EditText)findViewById(R.id.inpTimeExe);
        inpTimeExe.setText(getTimeActElapsed());
        inpActPost = (EditText)findViewById(R.id.inpActPost);
        inpActPost.setText(act.getNome());
        gaugeBlue = (ImageView)findViewById(R.id.imageViewBlue);
        gaugeGreen = (ImageView)findViewById(R.id.imageViewGreen);
        gaugeRed = (ImageView)findViewById(R.id.imageViewRed);
        txtStatus = (TextView)findViewById(R.id.textView3);
        btnAddNote = (ImageButton)findViewById(R.id.btnAddNotePost);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getResources().getString(R.string.notes));

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note = input.getText().toString();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        btnNextPost = (ImageButton)findViewById(R.id.btnNextPost);
        btnNextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.setAnotacoes(note);
                Intent intent = new Intent(v.getContext(),StatsActivity.class);
                startActivity(intent);
            }
        });
        setGauge();
    }

    private void setGauge() {
        //if(Act.media != 0)
        //switch(act.media)
        //case 1:
        gaugeGreen.setVisibility(View.INVISIBLE);
        gaugeRed.setVisibility(View.INVISIBLE);
        gaugeBlue.setVisibility(View.VISIBLE);
        txtStatus.setText("Otimista");

    }

    private String getTimeActElapsed() {
        return Integer.toString(act.getTempoGasto().getHours())+":"+Integer.toString(act.getTempoGasto().getMinutes())+
                ":"+Integer.toString(act.getTempoGasto().getSeconds());
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
            Intent settings = new Intent(PostReflectionActivity.this,SettingsActivity.class);
            PostReflectionActivity.this.startActivity(settings);

        }
        if(id == R.id.action_logout){
            Intent logout = new Intent(PostReflectionActivity.this, LoginActivity.class);
            PostReflectionActivity.this.startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
