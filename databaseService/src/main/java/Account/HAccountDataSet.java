package Account;

import java.io.Serializable;

@Entity
@Table(name = "Accounts")
public class HAccountDataSet implements Serializable {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login", unique = true)
    private String login = "";

    @Column(name = "pass")
    private String pass = "";

    public HAccountDataSet(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public HAccountDataSet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
