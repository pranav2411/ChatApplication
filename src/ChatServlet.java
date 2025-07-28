import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class ChatServlet extends HttpServlet {
    private static List<String> messages = Collections.synchronizedList(new ArrayList<>());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        for (String msg : messages) {
            out.println(msg + "<br/>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg = request.getParameter("message");
        String username = (String)request.getSession().getAttribute("username");
        if (msg != null && !msg.trim().isEmpty() && username != null) {
            messages.add("<b>" + username + ":</b> " + msg);
        }
    }
}