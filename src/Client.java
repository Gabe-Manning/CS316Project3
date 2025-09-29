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
        char command;
        do {
            System.out.println("Enter a command ('L' for List, 'D' for Delete, 'R' Rename, 'W' for Download, 'U' Upload, Q ' for Quit):");
            Scanner keyboard = new Scanner(System.in);
            String message = keyboard.nextLine();
            command = message.toUpperCase().charAt(0);
            switch(command) {
                case 'L':
                    //byte[] commandByte = command.getBytes();
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    ByteBuffer commandBuffer = ByteBuffer.allocate(2);
                    commandBuffer.putChar(command);
                    commandBuffer.flip();
                    channel.write(commandBuffer);
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
                case 'D':
                    //commandByte = command.getBytes();
                    System.out.println("Enter the file name of what you want to delete.");
                    String deleteFile = keyboard.nextLine();
                    byte[] deleteByte = deleteFile.getBytes();
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    commandBuffer = ByteBuffer.allocate(2);
                    commandBuffer.putChar(command);
                    commandBuffer.flip();
                    ByteBuffer fileBuffer = ByteBuffer.allocate(deleteByte.length);
                    fileBuffer.put(deleteByte);
                    fileBuffer.flip();
                    channel.write(commandBuffer);
                    channel.write(fileBuffer);
                    channel.shutdownOutput();
                    //Done with sending

                    //Receiving
                    serverReply = ByteBuffer.allocate(1024);
                    bytesFromServer = channel.read(serverReply);
                    channel.close();
                    serverReply.flip();
                    printByte = new byte[bytesFromServer];
                    serverReply.get(printByte);
                    System.out.println(new String(printByte));
                    break;
                case 'R':
                    break;
                case 'W':
                    break;
                case 'U':
                    break;
                case 'Q':
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
            }
        } while(command!='Q');
    }
}