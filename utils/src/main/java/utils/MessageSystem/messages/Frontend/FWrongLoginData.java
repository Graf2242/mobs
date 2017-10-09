package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.frontend.UserSessionStatus;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

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
