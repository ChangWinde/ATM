package Client;

//单例模式获取通信客户端

public class SingleClient {
    private static CommunicationClient communicationClient = null;

    public static CommunicationClient getCommunicationClient() {
        if(communicationClient == null)
            communicationClient = new CommunicationClient();
        return communicationClient;
    }
}
