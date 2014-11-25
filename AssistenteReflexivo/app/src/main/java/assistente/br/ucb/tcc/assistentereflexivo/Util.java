package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by ian.campelo on 11/5/14.
 */
public class Util {
    public static User jsonToUser(String json){
        User user = null;

        try {
            user = new User();
            JSONObject jsonObj = new JSONObject(json);
            user.setUsername(jsonObj.getString("usuario"));
            user.setFuncao(jsonObj.getString("funcao"));
            user.setBirthday(jsonObj.getString("nascimento"));
            user.setPassword(jsonObj.getString("senha"));
            user.setUserId(Integer.parseInt(jsonObj.getString("id")));
            user.setName(jsonObj.getString("nome"));
        }
        catch (Exception e){
            Log.e("ERRO_PARSER_UTIL_USER",e.getMessage());
        }
        return user;
    }

    public static ArrayList<Act> jsonToActList(String json){
        ArrayList<Act> acts = new ArrayList<Act>();

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray ja = jsonObject.getJSONArray("atividade");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject rec = ja.getJSONObject(i);
                acts.add(jsonToAct(rec.toString()));
            }
        } catch (Exception e) {
            Log.e("PARSE_LIST_ACT",e.getCause().toString());
            return null;
        }
        return acts;
    }

    public static Act getTimes(Act ativ, String ativStr) throws JSONException {
        JSONObject jsonObj = new JSONObject(ativStr);
        String tempoEst = jsonObj.getString("tempoEstimado");
        if(tempoEst != null){
            if(!tempoEst.contains("null")){
                if(!tempoEst.isEmpty()){
                    ativ.setTempoEstimado(Time.valueOf(tempoEst));
                }
            }
        }
        if(!jsonObj.isNull("tempoGasto")){
            String tmpG = jsonObj.getString("tempoGasto");
            if(tmpG != null){
                if(!tmpG.contains("null")){
                    if(!tmpG.isEmpty()){
                        ativ.setTempoGasto(Time.valueOf(tmpG));
                    }
                }
            }
        }
        else
            ativ.setTempoGasto(Time.valueOf("00:00:00"));
        return ativ;
    }
    public static ArrayList<String> actsToNames(ArrayList<Act> acts){
        ArrayList<String> _acts = new ArrayList<String>();
        for (Act act : acts) {
            _acts.add(act.getNome());
        }
        return _acts;
    }

    public static Act jsonToAct(String json){
        Act act;

        try {
            act = new Act();

            JSONObject jsonObj = new JSONObject(json);

            act.setUserid(jsonObj.getInt("uid"));
            act.setNome(jsonObj.getString("nome"));
            act.setPredicao(jsonObj.getInt("predicao"));
            act.setEstrategia(jsonObj.getString("estrategia"));
            act.setRecursos(jsonObj.getString("recursos"));
            act.setGrauAtencao(jsonObj.getString("grauAtencao"));

            act.setObjetivo(jsonObj.getString("objetivo"));
            act.setResultado(jsonObj.getInt("resultado"));
            act.setId(jsonObj.getInt("id"));
            act = getTimes(act,json);

            if(jsonObj.isNull("comprensao"))
                act.setComprensao(null);
            else
                act.setComprensao(jsonObj.getString("comprensao"));
            if(jsonObj.isNull("anotacoes"))
                act.setAnotacoes(null);
            else
                act.setAnotacoes(jsonObj.getString("anotacoes"));
            if(jsonObj.isNull("kma"))
                act.setKma(Integer.parseInt(null));
            else
                act.setKma(jsonObj.getInt("kma"));
            if(jsonObj.isNull("kmb"))
                act.setKmb(Integer.parseInt(null));
            else
                act.setKmb(jsonObj.getInt("kmb"));
        }
        catch (Exception e){
            Log.e("ERRO_PARSER_UTIL_ACT",e.getMessage());
            act = null;
        }
        return act;
    }
    public static void error(String logMsg,String msgError, Context mContext) {
        if(msgError==null)Log.e(logMsg, "Error"); else Log.e(logMsg, msgError);
        Toast myToast = Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT);
        myToast.show();
    }

    public static String getUrl(int url, Context ctx) {
        return ctx.getString(R.string.url_server)+ctx.getString(url);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show , Context ctx, final View mainForm, final View prgForm) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = ctx.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mainForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            prgForm.setVisibility(show ? View.VISIBLE : View.GONE);
            prgForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    prgForm.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            prgForm.setVisibility(show ? View.VISIBLE : View.GONE);
            mainForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String str = Util.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
                ediTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

}
