package graf.server.MasterService.messages.AccountServer;

import graf.server.Base.AccountService;
import graf.server.Base.Address;
import graf.server.Base.Node;
import graf.server.MasterService.messages.Frontend.FAddUserMessage;

public class ASFindUserIdMessage extends _AccountServiceMessageTemplate {
    final String userName;

    public ASFindUserIdMessage(Address from, String userName) {
        super(from);
        this.userName = userName;
    }

    @Override
    public void exec(Node node) {
        AccountService AccountService = null;
        try {
            AccountService = (AccountService) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (AccountService == null) {
            return;
        }
        Integer userId = AccountService.getUserId(userName);
        AccountService.getMasterService().addMessage(new FAddUserMessage(AccountService.getAddress(), userId, userName));
    }
}
