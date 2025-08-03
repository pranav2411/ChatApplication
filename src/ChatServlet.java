import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ChatServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3309/chatdb";
    private static final String DB_USER = "root"; // or your MySQL username
    private static final String DB_PASSWORD = ""; // or your password

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM chat_messages ORDER BY timestamp ASC")) {

            while (rs.next()) {
                String user = rs.getString("username");
                String msg = rs.getString("message");
                Timestamp time = rs.getTimestamp("timestamp");
                String ip = rs.getString("ip_address");

                out.println("<b>" + user + ":</b> " + msg + "<br/>");
            }

        } catch (SQLException e) {
            out.println("Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg = request.getParameter("message");
        String username = (String) request.getSession().getAttribute("username");
        String ip = request.getRemoteAddr();

        if (msg != null && !msg.trim().isEmpty() && username != null) {
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO chat_messages (username, message, ip_address) VALUES (?, ?, ?)")) {

                stmt.setString(1, username);
                stmt.setString(2, msg);
                stmt.setString(3, ip);
                stmt.executeUpdate();

            } catch (SQLException e) {
                response.getWriter().println("Database insert error: " + e.getMessage());
            }
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found", e);
        }
    }
}
