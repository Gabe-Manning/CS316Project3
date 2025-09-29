import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws Exception {
        int port = 3000;
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.socket().bind(new InetSocketAddress(port));

        ByteBuffer replyBuffer;
        String fileString;
        File fileName;
        File directory = new File("C:/Users/gabem/Desktop/CS316/CS316Project3/ServerFiles");

        while(true) {
            SocketChannel serverChannel = listenChannel.accept();
            ByteBuffer commandBuffer = ByteBuffer.allocate(1024);
            int bytesReadCommand = serverChannel.read(commandBuffer);
            commandBuffer.flip();
            byte[] clientSent = new byte[bytesReadCommand];
            commandBuffer.get(clientSent);
            String command = new String(clientSent);
            String commandFirst = command.substring(0, 1).toUpperCase();
            switch(commandFirst) {
                case "L":
                    String list[] = directory.list();
                    String returnString = "";
                    for (int i = 0; i < list.length; i++) {
                        returnString = returnString + " " + (i + 1) + ". " + list[i];
                    }
                    if (returnString.equals("")) {
                        replyBuffer = ByteBuffer.wrap("F".getBytes());
                        serverChannel.write(replyBuffer);
                    } else {
                        returnString = "S\n" + returnString;
                        replyBuffer = ByteBuffer.wrap(returnString.getBytes());
                        serverChannel.write(replyBuffer);
                    }
                    break;
                case "D":
                    fileString = command.substring(1);
                    fileName = new File(directory + "/" + fileString);
                    if (fileName.exists() && !fileName.delete()) {
                        replyBuffer = ByteBuffer.wrap("F".getBytes());
                        serverChannel.write(replyBuffer);
                    } else {
                        replyBuffer = ByteBuffer.wrap("S".getBytes());
                        serverChannel.write(replyBuffer);
                    }
                    break;
                case "R":
                    break;
                case "W":
                    break;
                case "U":
                    break;
                default:
                    System.out.println("Received invalid command " + command);
            }
    }}
}