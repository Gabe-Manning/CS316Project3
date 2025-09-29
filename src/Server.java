import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws Exception {
        int port = 3000;
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(port));

        while(true) {
            //accept() handles client connection requests and establishes a new channel with each client
            //serverChannel represents this new channel
            SocketChannel serverChannel = listenChannel.accept();
            ByteBuffer commandBuffer = ByteBuffer.allocate(1024);

            int bytesReadCommand = serverChannel.read(commandBuffer);
            commandBuffer.flip();
            char command = commandBuffer.getChar();
            System.out.println(command);
            switch(command) {
                case 'L':
                    byte[] commandByte = new byte[bytesReadCommand];
                    commandBuffer.get(commandByte);
                    String folderPath = "C:/Users/gabem/Desktop/CS316/CS316Project3/ServerFiles/";
                    File folder = new File(folderPath);
                    List<String> fileNames = new ArrayList<>();
                    if (folder.exists() && folder.isDirectory()) {
                        File[] files = folder.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    fileNames.add(file.getName());
                                }
                            }
                        }
                        String success = "Success";
                        byte[] successByte = success.getBytes();
                        ByteBuffer successBuffer = ByteBuffer.wrap(successByte);
                        serverChannel.write(successBuffer);
                        serverChannel.close();
                    } else {
                        String fail = "Fail";
                        byte[] failByte = fail.getBytes();
                        ByteBuffer failBuffer = ByteBuffer.wrap(failByte);
                        serverChannel.write(failBuffer);
                        serverChannel.close();
                    }
                    break;
                case 'D':
                    commandByte = new byte[bytesReadCommand];
                    commandBuffer.get(commandByte);
                    ByteBuffer fileNameBuffer = ByteBuffer.allocate(1024);
                    int bytesReadFile = serverChannel.read(fileNameBuffer);
                    fileNameBuffer.flip();
                    byte[] a = new byte[bytesReadFile];
                    String nameFile = String.valueOf(fileNameBuffer.get(a));
                    File file = new File("C:/Users/gabem/Desktop/CS316/CS316Project3/ServerFiles/" + nameFile);
                    if (file.exists()) {
                        file.delete();
                        String success = "Success";
                        byte[] successByte = success.getBytes();
                        ByteBuffer successBuffer = ByteBuffer.wrap(successByte);
                        serverChannel.write(successBuffer);
                        serverChannel.close();
                    } else {
                        String fail = "Fail";
                        byte[] failByte = fail.getBytes();
                        ByteBuffer failBuffer = ByteBuffer.wrap(failByte);
                        serverChannel.write(failBuffer);
                        serverChannel.close();
                    }
                    break;
                case 'R':
                    break;
                case 'W':
                    break;
                case 'U':
                    break;
                default:
                    System.out.println("Received invalid command " + command);
            }
    }}
}