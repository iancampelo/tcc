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
import org.json.JSONObject;

/**
 * Created by ian.campelo on 11/5/14.
 */
public class Util {
    public static User jsonToUser(String json){
        User user = null;

        try {
            user = new User();

            JSONObject jsonObj = new JSONObject(json);

            jsonObj.getString("usuario");

            user.setUsername(jsonObj.getString("usuario"));
            user.setFuncao(jsonObj.getString("funcao"));
            user.setBirthday(jsonObj.getString("nascimento"));
            user.setPassword(jsonObj.getString("senha"));
            user.setUserId(Integer.parseInt(jsonObj.getString("id")));
        }
        catch (Exception e){
            Log.e("ERRO_PARSER_UTIL_USER",e.getMessage());
        }
        return user;
    }

    public static User jsonToAct(String json){
        Act act = null;

        try {
            act = new Act();

            JSONObject jsonObj = new JSONObject(json);

        }
        catch (Exception e){
            Log.e("ERRO_PARSER_UTIL_ACT",e.getMessage());
        }
        return act;
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
