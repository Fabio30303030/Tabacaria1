import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet") // URL para acessar este servlet
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Dados de conexão com o banco de dados
        String url = "jdbc:mysql://localhost:3306/loginDB"; // Altere para o nome do seu banco de dados
        String dbUsername = "root"; // Altere para o seu usuário do MySQL
        String dbPassword = "sua_senha"; // Altere para a sua senha do MySQL

        // Obtendo os parâmetros do formulário
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Configurando o tipo de resposta
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Autentica o usuário
        if (authenticateUser(username, password, url, dbUsername, dbPassword)) {
            out.println("<h2>Login bem-sucedido! Bem-vindo, " + username + "!</h2>");
        } else {
            out.println("<h2>Nome de usuário ou senha incorretos.</h2>");
        }
    }

    // Método para autenticar o usuário no banco de dados
    private boolean authenticateUser(String username, String password, String url, String dbUsername, String dbPassword) {
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