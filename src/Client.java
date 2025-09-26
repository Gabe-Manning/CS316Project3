import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Need <serverIP> and <serverPort>");
            return;
        }

        int serverPort = Integer.parseInt(args[1]);
        String command;
        do {
            System.out.println("Enter a command (List, Delete, Rename, Download, Upload, or Quit):");
            Scanner keyboard = new Scanner(System.in);
            String message = keyboard.nextLine();
            command = message.toUpperCase();
            switch(command) {
                case "LIST":
                    break;
                case "DELETE":
                    byte[] commandByte = command.getBytes();
                    System.out.println("Enter the file name of what you want to delete.");
                    String deleteFile = keyboard.nextLine();
                    byte[] deleteByte = deleteFile.getBytes();
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    ByteBuffer commandBuffer = ByteBuffer.allocate(commandByte.length);
                    commandBuffer.put(commandByte);
                    commandBuffer.flip();
                    ByteBuffer fileBuffer = ByteBuffer.allocate(deleteByte.length);
                    fileBuffer.put(deleteByte);
                    fileBuffer.flip();
                    channel.write(commandBuffer);
                    channel.write(fileBuffer);
                    channel.shutdownOutput();
                    //Done with sending

                    //Receiving
                    ByteBuffer serverReply = ByteBuffer.allocate(1024);
                    int bytesFromServer = channel.read(serverReply);
                    channel.close();
                    serverReply.flip();
                    byte[] printByte = new byte[bytesFromServer];
                    serverReply.get(printByte);
                    System.out.println(new String(printByte));
                    break;
                case "RENAME":
                    break;
                case "DOWNLOAD":
                    break;
                case "UPLOAD":
                    break;
                case "QUIT":
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
            }
        } while(!command.equals("QUIT"));
    }
}