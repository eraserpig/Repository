import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * @Author gmq
 * @Date 2023-09-15
 * @Description
 **/
public class JDbctest {


        private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/dramatic";
        private static final String DB_USER = "dramatic";
        private static final String DB_PASSWORD = "dra123";

        public static void main(String[] args) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                String sql = "INSERT INTO aml_sensitive_word (id, keyword, status, create_user) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                Random random = new Random();
                for (int i = 0; i < 10000; i++) {
                    pstmt.setInt(1, i);
                    pstmt.setString(2, getRandomChineseWord()); // 使用你自定义的getRandomChineseWord()函数生成随机的中文单词或汉字
                    pstmt.setInt(3, 1); // status字段为整型，这里我们设为1，你可以根据需要进行修改
                    pstmt.setString(4, "admin"); // create_user字段为字符串，这里我们设为"admin"，你可以根据需要进行修改
                    pstmt.executeUpdate();
                }
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 你需要自定义这个函数来生成随机的中文单词或汉字
        private static String getRandomChineseWord() {
            // 这里只是一个例子，你需要自己实现这个函数
            String[] words = {"中文", "单词", "汉字", "示例", "单词", "汉字"};
            return words[new Random().nextInt(words.length)];
        }











}
