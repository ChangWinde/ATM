package Server;

//单例模式的服务器创建方法

public class SingleServer {
    private static ServerDaemon serverDaemon = null;

    public static ServerDaemon getServerDaemon(){
        if(serverDaemon == null)
            serverDaemon = new ServerDaemon();
        return serverDaemon;
    }
}
