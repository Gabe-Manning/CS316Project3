import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        FileChannel fc;
        ByteBuffer fileContents;

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
                        replyBuffer = ByteBuffer.wrap("F\nCould not show the file list.".getBytes());
                        serverChannel.write(replyBuffer);
                    } else {
                        returnString = "S\nList:" + returnString;
                        replyBuffer = ByteBuffer.wrap(returnString.getBytes());
                        serverChannel.write(replyBuffer);
                    }
                    break;
                case "D":
                    fileString = command.substring(1);
                    fileName = new File(directory + "/" + fileString);
                    if (!fileName.exists() && !fileName.delete()) {
                        replyBuffer = ByteBuffer.wrap("F\nCould not delete the file.".getBytes());
                        serverChannel.write(replyBuffer);
                    } else {
                        fileName.delete();
                        replyBuffer = ByteBuffer.wrap("S\nFile deleted.".getBytes());
                        serverChannel.write(replyBuffer);
                    }
                    break;
                case "R":
                    String serverReply;
                    int firstColon = command.indexOf(':');
                    fileString = command.substring(1, firstColon);
                    String renamedName = command.substring(firstColon + 1);
                    fileName = new File(directory + "/" + fileString);
                    File renamedFile = new File(directory + "/" + renamedName);
                    if (fileName.renameTo(renamedFile)) {
                        serverReply = "S\nRenamed " + fileString + " to " + renamedName;
                        replyBuffer = ByteBuffer.wrap(serverReply.getBytes());
                        serverChannel.write(replyBuffer);
                    } else {
                        serverReply = "F\nCould not rename " + fileString + ".";
                        replyBuffer = ByteBuffer.wrap(serverReply.getBytes());
                        serverChannel.write(replyBuffer);
                    }
                    break;
                case "W":
                    fileString = command.substring(1);
                    fileName = new File(directory + "/" + fileString);
                    if (!fileName.exists()) {
                        serverChannel.write(ByteBuffer.wrap("F".getBytes()));
                        serverChannel.close();
                    } else {
                        serverChannel.write(ByteBuffer.wrap("S".getBytes()));
                        replyBuffer = ByteBuffer.allocate(1);
                        serverChannel.read(replyBuffer);
                        replyBuffer.flip();
                        byte[] replyArray = new byte[1];
                        replyBuffer.get(replyArray);
                        if (new String(replyArray).equals("A")) {
                            FileInputStream fis = new FileInputStream(directory + "/" + fileString);
                            fc = fis.getChannel();
                            int bufferSize = 1024;
                            fileContents = ByteBuffer.allocate(bufferSize);
                            while (fc.read(fileContents) >= 0) {
                                fileContents.flip();
                                serverChannel.write(fileContents);
                                fileContents.clear();
                            }
                            fis.close();
                            serverChannel.shutdownOutput();
                        }
                    }
                    break;
                case "U":
                    //Status code sending
                    fileString = command.substring(1);
                    fileName = new File("C:/Users/gabem/Desktop/CS316/CS316Project3/ClientFiles/" + fileString);
                    if (fileName.exists()) {
                        serverChannel.write(ByteBuffer.wrap("S".getBytes()));
                    } else {
                        serverChannel.write(ByteBuffer.wrap("F".getBytes()));
                    }
                    //Uploading file
                    FileOutputStream fo = new FileOutputStream(directory + "/" + fileString);
                    fc = fo.getChannel();
                    fileContents = ByteBuffer.allocate(1024);
                    while (serverChannel.read(fileContents) >= 0) {
                        fileContents.flip();
                        fc.write(fileContents);
                        fileContents.clear();
                    }
                    fo.close();
                    serverChannel.close();
                    break;
                default:
                    System.out.println("Received invalid command " + command);
            }
    }}
}