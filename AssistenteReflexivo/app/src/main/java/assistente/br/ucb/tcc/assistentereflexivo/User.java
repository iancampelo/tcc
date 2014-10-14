package assistente.br.ucb.tcc.assistentereflexivo;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ian.campelo on 9/2/14.
 */
public class User implements Serializable{


    private long userId;
    @SerializedName("usuario")
    private String username;
    @SerializedName("senha")
    private String password;
    @SerializedName("nome")
    private String name;
    @SerializedName("nascimento")
    private String birthday;
    private String funcao;

    public User(){}

    public User(long userId, String username, String password, String name, String birthday, String funcao) {
        setUserId(userId);
        setUsername(username);
        setPassword(password);
        setName(name);
        setBirthday(birthday);
        setFuncao(funcao);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
