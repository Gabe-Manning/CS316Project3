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
                    break;
                case "RENAME":
                    break;
                case "DOWNLOAD":
                    break;
                case "UPLOAD":
                    break;
                case "QUIT":
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
            }
        } while(command!="QUIT");
    }
}