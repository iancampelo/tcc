package assistente.br.ucb.tcc.assistentereflexivo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;


import com.fasterxml.jackson.databind.ObjectMapper;

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

/*
funcao: "Gerente de Projetos"
id: "1"
nascimento: "1992-08-19"
nome: "Ian Campelo"
senha: "123456"
usuario: "ian@gmail.com"
 */
        }
        catch (Exception e){
            Log.e("ERRO_PARSER_UTIL_USER",e.getMessage());
        }
        return user;
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
}
