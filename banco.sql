import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/loginDB"; // URL do banco de dados
        String dbUsername = "root"; // Usuário do MySQL
        String dbPassword = "sua_senha"; // Senha do MySQL

        String username = "usuario"; // Nome de usuário para teste
        String password = "senha123"; // Senha para teste

        // Verifica se o usuário existe no banco de dados
        if (authenticateUser(username, password, url, dbUsername, dbPassword)) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + username + "!");
        } else {
            System.out.println("Nome de usuário ou senha incorretos.");
        }
    }

    // Método para autenticar o usuário no banco de dados
    public static boolean authenticateUser(String username, String password, String url, String dbUsername, String dbPassword) {
        try {
            // Conectar ao banco de dados
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Consulta SQL para verificar se o usuário existe
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Executa a consulta
            ResultSet resultSet = statement.executeQuery();

            // Verifica se há algum resultado
            boolean userExists = resultSet.next();

            // Fecha a conexão e o statement
            resultSet.close();
            statement.close();
            connection.close();

            return userExists; // Retorna verdadeiro se o usuário existir
        } catch (Exception e) {
            System.out.println("Erro de conexão: " + e.getMessage());
            return false;
        }
    }
}