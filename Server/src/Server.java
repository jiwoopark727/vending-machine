import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static BufferedReader reader;
    public static PrintWriter fileWriter;
    public static PrintWriter fileWriter2;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is listening on port " + 8000);

            while (true) {
                socketConfig(serverSocket);

                while (true) {
                    String str = reader.readLine();
                    switch (str) {
                        case "update":  //클라이언트에서 변경 사항이 생기면 update.txt에 추가하는 코드
                            String line;
                            while (!(line = reader.readLine()).equals("done")) {
                                fileWriter.println(line);
                            }
                            System.out.println("File received and saved as update.txt");
                            //변경사항 분기해서 처리하는 메소드
                            break;
                        case "beverage": // 음료 데이터 받아서 저장하는 코드
                            String line2;
                            while (!(line2 = reader.readLine()).equals("done")) {
                                fileWriter2.println(line2);
                            }
                            System.out.println("File received and saved as beverages.txt");
                            break;
                        case "soldOut":    /*클라이언트에서 재고 소진 메시지는 받는 코드*/
                            String line3;
                            while(!(line3 = reader.readLine()).equals("done")) {
                                System.out.println(line3);
                            }
                            break;
                        case "changeName": //음료 이름 변경시 받을 내용
                            String line4;
                            while(!(line4 = reader.readLine()).equals("done")) {
                                fileWriter.println(line4);
                                changeBeverageName(line4);
                            }
                            // line4 짤라서 beverage.txt

                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void socketConfig(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("New client connected");

        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        OutputStream outputStream = new FileOutputStream("update.txt");
        fileWriter = new PrintWriter(outputStream, true);

        OutputStream outputStream2 = new FileOutputStream("beverages.txt");
        fileWriter2 = new PrintWriter(outputStream2, true);
    }

    private static void changeBeverageName(String line) throws IOException {
        // "음료이름변경 오우따 -> 물" 포맷의 라인 처리
        String[] parts = line.split(" ");

        if (parts.length < 4 || !parts[0].equals("음료이름변경") || !parts[2].equals("->")) {
//            System.out.println("잘못된 형식: " + line);
//            System.out.println(line);
            return;
        }

        String oldName = parts[1];
        String newName = parts[3];

        // beverages 파일 읽기
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("beverages.txt"))) {
            String beverageLine;
            while ((beverageLine = br.readLine()) != null) {
                String[] beverageParts = beverageLine.split(" ");
                if (beverageParts[0].equals(oldName)) {
                    lines.add(newName + " " + beverageParts[1] + " " + beverageParts[2]);
                } else {
                    lines.add(beverageLine);
                }
            }
        }

        // 업데이트된 내용을 파일에 다시 쓰기
        try (PrintWriter writer = new PrintWriter(new FileWriter("beverages.txt"))) {
            for (String updatedLine : lines) {
                writer.println(updatedLine);
            }
        }

        System.out.println("beverages.txt에서 음료 이름이 업데이트되었습니다");
    }
}