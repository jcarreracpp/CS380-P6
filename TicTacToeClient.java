
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author Jorge
 */
public class TicTacToeClient {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("codebank.xyz", 38006)) {
            ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
            Scanner in = new Scanner(System.in);
            
            ConnectMessage oup = new ConnectMessage("Jorge");
            CommandMessage begin = new CommandMessage(CommandMessage.Command.NEW_GAME);
            MoveMessage move;
            
            oo.writeObject(oup);
            oo.writeObject(begin);
            
            BoardMessage response = (BoardMessage)oi.readObject();
            
            byte row;
            byte col;
            boolean errorAvoided = true;
            
            while (response.getStatus() == BoardMessage.Status.IN_PROGRESS && errorAvoided) {

                byte[][] map = response.getBoard();
                char[][] c = convertTTT(map);

                for (int n = 0; n < 3; n++) {
                    for (int p = 0; p < 3; p++) {
                        System.out.print(c[n][p] + " ");
                    }
                    System.out.println();
                }
                System.out.print("\nEnter row (1-3): ");
                row = Byte.valueOf(in.nextLine());
                row--;
                System.out.print("\nEnter column (1-3): ");
                col = Byte.valueOf(in.nextLine());
                col--;
                System.out.println();
                
                move = new MoveMessage(row, col);
                oo.writeObject(move);
                try{
                    response = (BoardMessage)oi.readObject();
                } catch(ClassCastException e){
                    System.out.println("Error encountered! Exiting game.");
                    begin = new CommandMessage(CommandMessage.Command.EXIT);
                    oo.writeObject(begin);
                    errorAvoided = false;
                }
            }
            
            if(!errorAvoided){
            }else if(response.getStatus() == BoardMessage.Status.PLAYER1_VICTORY){
                System.out.println("You Won!");
            }else if(response.getStatus() == BoardMessage.Status.PLAYER2_VICTORY){
                System.out.println("You Lost!");
            }else if(response.getStatus() == BoardMessage.Status.STALEMATE){
                System.out.println("Tie!");
            }
        }
    }
    public static char[][] convertTTT(byte[][] b){
        char[][] c = new char[3][3];
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j <3; j++){
                switch(b[i][j]){
                    case 0: c[i][j] = '-';
                            break;
                    case 1: c[i][j] = 'X';
                            break;
                    default:c[i][j] = 'O';
                            break;
                }
            }
        }
        return c;
    }
}
