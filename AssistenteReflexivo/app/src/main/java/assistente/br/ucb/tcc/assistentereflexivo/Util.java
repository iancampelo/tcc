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
    private static Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context _mContext) {
        mContext = _mContext;
    }

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

            if(!jsonObject.isNull("atividade")) {

                Object item = jsonObject.get("atividade");
                if(item instanceof JSONArray) {
                    JSONArray ja = jsonObject.getJSONArray("atividade");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject rec = ja.getJSONObject(i);
                        acts.add(jsonToAct(rec.toString()));
                    }
                }else{
                    acts.add(jsonToAct(jsonObject.toString()));
                }
            }
            else
                return null;
        } catch (Exception e) {
            Log.e("PARSE_LIST_ACT",e.getCause().toString());
            return null;
        }
        return acts;
    }

    public static Act getTimes(Act ativ, String ativStr) throws JSONException {
        JSONObject jsonObj = new JSONObject(ativStr);
        String tempoEst = jsonObj.optString("tempoEstimado",null);
        if(tempoEst != null){
            if(!tempoEst.contains("null")){
                if(!tempoEst.isEmpty()){
                    ativ.setTempoEstimado(Time.valueOf(tempoEst));
                }
            }
        }
        if(!jsonObj.isNull("tempoGasto")){
            String tmpG = jsonObj.optString("tempoGasto",null);
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
        User user = (User)mContext;

        //TODO arrumar quando tiver só uma atividade, do Jean viado!
        try {
            act = new Act();

            JSONObject jsonObj = new JSONObject(json);

            act.setNome(jsonObj.optString("nome",null));
            act.setPredicao(jsonObj.optInt("predicao"));
            act.setEstrategia(jsonObj.optString("estrategia",null));
            act.setRecursos(jsonObj.optString("recursos",null));
            act.setGrauAtencao(jsonObj.optString("grauAtencao",null));
            act.setObjetivo(jsonObj.optString("objetivo",null));
            act.setResultado(jsonObj.optInt("resultado"));
            act.setId(jsonObj.optInt("id"));
            act.setComprensao(jsonObj.optString("comprensao",null));
            act.setAnotacoes(jsonObj.optString("anotacoes",null));
            act.setKma(jsonObj.optDouble("kma"));
            act.setKmb(jsonObj.optDouble("kmb"));
            act.setUserid(jsonObj.optInt("uid",user.getUserId()));
            act = getTimes(act,json);
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

    //TODO TESTING WITH INVISIBLE
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show , Context ctx, final View mainForm, final View prgForm) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = ctx.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainForm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mainForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainForm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            prgForm.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
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
            prgForm.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mainForm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
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
