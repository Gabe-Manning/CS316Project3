import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.println("Enter a command ('L' for List, 'D' for Delete, 'R' Rename, 'W' for Download, 'U' Upload, Q ' for Quit):");
            String message = keyboard.nextLine();
            command = message.toUpperCase();

            ByteBuffer commandBuffer;
            SocketChannel channel;
            ByteBuffer serverReply;
            int bytesFromServer;
            byte[] replyByte;
            String fileName;

            switch(command) {
                case "L":
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(commandBuffer);
                    channel.shutdownOutput();
                    //Done with sending

                    //Receiving
                    serverReply = ByteBuffer.allocate(1024);
                    bytesFromServer = channel.read(serverReply);
                    serverReply.flip();
                    replyByte = new byte[bytesFromServer];
                    serverReply.get(replyByte);
                    System.out.println(new String(replyByte));
                    channel.close();
                    break;
                case "D":
                    System.out.println("Enter the name of the file that you want to delete.");
                    fileName = keyboard.nextLine();
                    command = command + fileName;
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(commandBuffer);
                    channel.shutdownOutput();
                    //Done with sending

                    //Receiving
                    serverReply = ByteBuffer.allocate(1024);
                    bytesFromServer = channel.read(serverReply);
                    serverReply.flip();
                    replyByte = new byte[bytesFromServer];
                    serverReply.get(replyByte);
                    System.out.println(new String(replyByte));
                    channel.close();
                    break;
                case "R":
                    break;
                case "W":
                    break;
                case "U":
                    break;
                case "Q":
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
            }
        } while(command.equals("Q"));
    }
}