package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Application;

import com.google.gson.annotations.*;

import java.io.Serializable;

/**
 * Created by ian.campelo on 9/2/14.
 */
public class User extends Application implements Serializable  {
    public static final String URL_USER = "http://192.168.1.4:8080/webservice/usuario";

    @SerializedName("id")
    private long userId = 0;
    @SerializedName("usuario")
    private String username = "ian.campelo@gmail.com";
    @SerializedName("senha")
    private String password = "1234567";
    @SerializedName("nome")
    private String name = "Ian Coelho Campelo Serpa";
    @SerializedName("nascimento")
    private String birthday = "19/08/1992";
    private String funcao = "Gerente";

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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", funcao='" + funcao + '\'' +
                '}';
    }
    public String toJson(){
        return "{" +
                "\"id\":"+Long.toString(getUserId())+"," +
                "\"usuario\":\""+getUsername()+"\"," +
                "\"senha\":\""+getPassword()+"\"," +
                "\"nome\":\""+getName()+"\"," +
                "\"nascimento\":\""+getBirthday()+"\"," +
                "\"funcao\":\""+getFuncao()+"\"" +
                "}";
        /*
        {
    "id": 9,
    "usuario": "ian@gmail.com",
    "senha": "123456",
    "nome": "Ian Campelo",
    "nascimento":"19/08/1992",
    "funcao":"Gerente de Projetos"
}
         */

    }

}
