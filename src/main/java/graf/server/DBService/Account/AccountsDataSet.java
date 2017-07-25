package graf.server.DBService.Account;

public class AccountsDataSet {
    final Long id;
    final String login;
    final String pass;

    public AccountsDataSet(Long id, String login, String pass) {
        this.id = id;
        this.login = login;
        this.pass = pass;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
