package graf.server.MasterService.messages.DBService;

import graf.server.Base.Address;
import graf.server.Base.DBService;
import graf.server.Base.Node;

public class DBCreateUser extends _DBServiceMessageTemplate {
    private String login;
    private String pass;
    private Long sessionId;

    public DBCreateUser(Address from, String login, String pass, Long sessionId) {
        super(from);
        this.login = login;
        this.pass = pass;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        DBService dbService = (DBService) node;
        dbService.createAccount(login, pass);
    }
}
