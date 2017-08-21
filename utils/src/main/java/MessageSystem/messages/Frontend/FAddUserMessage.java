package MessageSystem.messages.Frontend;

import frontend.Frontend;
import masterService.nodes.Address;
import masterService.nodes.Node;

public class FAddUserMessage extends _FrontendMessageTemplate {
    private final Long userId;
    private final Long sessionId;

    public FAddUserMessage(Address from, Long userId, Long sessionId) {
        super(from);
        this.userId = userId;
        this.sessionId = sessionId;
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
        frontend.updateUserId(sessionId, userId);

    }
}
