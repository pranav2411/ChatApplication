<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.ServletContext" %>
<%
    String username = request.getParameter("username");
    if (username == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    session.setAttribute("username", username);
%>
<html>
<head>
    <title>Simple JSP Chat</title>
    <script>
        function sendMessage() {
            var msg = document.getElementById('msg').value;
            if (msg.trim() === "") return;
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'ChatServlet', true);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
            xhr.send('message=' + encodeURIComponent(msg));
            document.getElementById('msg').value = '';
        }

        function refreshMessages(){
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if(xhr.readyState == 4 && xhr.status == 200){
                    document.getElementById('messages').innerHTML = xhr.responseText;
                }
            };
            xhr.open('GET', 'ChatServlet', true);
            xhr.send();
        }

        setInterval(refreshMessages, 1000); // Refresh every second
    </script>
</head>
<body onload="refreshMessages()">
    <h2>Welcome, <%= username %></h2>
    <div id="messages" style="border:1px solid #999;height:200px;width:300px;overflow-y:scroll;"></div>
    <input type="text" id="msg" placeholder="Type a message..." autofocus/>
    <button onclick="sendMessage()">Send</button>
</body>
</html>