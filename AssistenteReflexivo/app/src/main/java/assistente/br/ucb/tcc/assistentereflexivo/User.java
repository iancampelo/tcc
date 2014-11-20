package assistente.br.ucb.tcc.assistentereflexivo;

import android.app.Application;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ian.campelo on 9/2/14.
 */
public class User extends Application implements Serializable  {
    private int userId;
    private String username;
    private String password;
    private String name;
    private String birthday;
    private String funcao;

    public User(){}

    public User(int userId, String username, String password, String name, String birthday, String funcao) {
        setUserId(userId);
        setUsername(username);
        setPassword(password);
        setName(name);
        setBirthday(birthday);
        setFuncao(funcao);
    }


//SET
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

//GET
    @JsonProperty("usuario")
    public String getUsername() {
        return username;
    }
    @JsonProperty("senha")
    public String getPassword() {
        return password;
    }
    @JsonProperty("nome")
    public String getName() {
        return name;
    }
    @JsonProperty("aniversario")
    public String getBirthday() {
        return birthday;
    }
    @JsonProperty("funcao")
    public String getFuncao() {
        return funcao;
    }
    @JsonProperty("id")
    public int getUserId() {
        return userId;
    }


//OVERRIDES
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
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null)
            return false;
        if (funcao != null ? !funcao.equals(user.funcao) : user.funcao != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (funcao != null ? funcao.hashCode() : 0);
        return result;
    }

}
