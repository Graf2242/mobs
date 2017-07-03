package graf.server.MasterService.messages.Frontend;

import graf.server.Base.Address;
import graf.server.Base.Frontend;
import graf.server.Base.Node;
import graf.server.MasterService.messages.Lobby.LAddUser;

public class FAddUserMessage extends _FrontendMessageTemplate {
    final Integer userId;
    final String userName;

    public FAddUserMessage(Address from, Integer userId, String userName) {
        super(from);
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = null;
        try {
            frontend = (Frontend) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (frontend == null) {
            return;
        }
        if (frontend.updateUserId(userName, userId)) {
            frontend.getMasterService().addMessage(new LAddUser(frontend.getAddress(), userId, userName, frontend.getSessionId(userName)));
        }
    }
}
