import java.io.File;
import java.io.FileInputStream;
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
            FileChannel fc;

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
                    System.out.println("Enter the name of the file that you want to rename.");
                    fileName = keyboard.nextLine();
                    command = command + fileName;
                    System.out.println("Please enter the new name for the file.");
                    String renamedName = keyboard.nextLine();
                    command = command + ":" + renamedName;
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
                case "W":
                    break;
                case "U":
                    System.out.println("Enter the name of the file that you want to upload");
                    fileName = keyboard.nextLine();
                    command = command + fileName;
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(commandBuffer);
                    //Checks to see if file exists and sends contact to server

                    //Receiving status code on whether to upload the file
                    serverReply = ByteBuffer.allocate(1024);
                    bytesFromServer = channel.read(serverReply);
                    serverReply.flip();
                    replyByte = new byte[bytesFromServer];
                    serverReply.get(replyByte);
                    String serverCode = new String(replyByte);
                    System.out.println(serverCode);

                    //Uploads the file
                    File uploadFile = new File("C:/Users/gabem/Desktop/CS316/CS316Project3/ClientFiles/" + fileName);
                    if (serverCode.equals("S")) {
                        FileInputStream fs = new FileInputStream(uploadFile);
                        fc = fs.getChannel();
                        int bufferSize = 1024;
                        if (bufferSize > fc.size()) {
                            bufferSize = (int) fc.size();
                        }

                        ByteBuffer fileContent = ByteBuffer.allocate(bufferSize);
                        while (fc.read(fileContent) >= 0) {
                            channel.write(fileContent.flip());
                            fileContent.clear();
                        }
                        channel.shutdownOutput();
                        //Done with sending
                        channel.close();
                        System.out.println("File uploaded with contents intact.");
                    } else {
                        System.out.println("The file contents could not be uploaded. The resulting file is empty.");
                    }
                    break;
                case "Q":
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
            }
        } while(!command.equals("Q"));
    }
}