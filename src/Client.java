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
            System.out.println("Enter a command (D, T, E, Q):");
            Scanner keyboard = new Scanner(System.in);
            String message = keyboard.nextLine();
            command = message.toUpperCase();
        } while(command!="Quit");
    }
}