package graf.server.MasterService.messages.Frontend;

import graf.server.Base.Address;
import graf.server.Base.Frontend;
import graf.server.Base.Node;
import graf.server.Frontend.UserSessionStatus;

public class FWrongLoginData extends _FrontendMessageTemplate {
    private final Long sessionId;

    public FWrongLoginData(Address from, Long sessionId) {
        super(from);
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        frontend.updateSessionStatus(sessionId, UserSessionStatus.WRONG_LOGIN_INFO);
    }
}
