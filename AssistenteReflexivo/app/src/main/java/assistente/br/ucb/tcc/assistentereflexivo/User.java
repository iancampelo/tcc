package assistente.br.ucb.tcc.assistentereflexivo;

/**
 * Created by ian.campelo on 9/2/14.
 */
public class User {
    private long userId;
    private String username;
    private String password;
    private String name;

    public User(){}

    public User(long userId, String username, String password, String name){

        setUserId(userId);
        setUsername(username);
        setName(name);
        setPassword(password);
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
}
