
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 *
 * @author Jorge
 */
public class TicTacToeClient {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("codebank.xyz", 38006)) {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            ConnectMessage oup = new ConnectMessage("Jorge");
            CommandMessage begin = new CommandMessage(CommandMessage.Command.NEW_GAME);
            
            o.writeObject(oup);
            o.writeObject(begin);
            System.out.println("HW");
            
            int[][] map = new int[3][3];
            for(int i = 0 ; i < 3 ; i++){
                for(int j = 0; j < 3; j++){
                map[i][j] = is.read();
                }
            }
            for(int k =0 ; k < 3; k++){
                for(int m = 0; m < 3; m++){
                    System.out.print(map[k][m] + " ");
                }
                System.out.println();
            }
        }
    }
}
