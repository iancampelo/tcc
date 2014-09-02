package assistente.br.ucb.tcc.assistentereflexivo;

/**
 * Created by ian.campelo on 9/2/14.
 */
public class User {
    public long userId;
    public String username;
    public String password;

    public User(long userId, String username, String password){
        this.userId=userId;
        this.username=username;
        this.password=password;
    }
}
