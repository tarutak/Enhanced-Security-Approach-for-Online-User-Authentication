<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="org.json.JSONObject"%>
<%@page import="commons.Database"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="commons.Initializer"%>
<%@page import="constants.Constants"%>
<%
    JSONObject object = new JSONObject();
    Class.forName(Constants.className);
    Connection connection = DriverManager.getConnection(Constants.url, Constants.username, Constants.password);
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String id = request.getParameter("userId");
    if (username == null) {
        object.put("status", "error");
        out.println(object.toString());
        return;
    }

    System.out.print(username);
    if (username.length() <= 0) {
        object.put("status", "Invalid Input");
%>	
<%=object.toString()%>
<%
        return;
    }
    PreparedStatement statement = connection.prepareStatement("SELECT username,password,userId FROM users WHERE username='" + username + "'");
    ResultSet result = statement.executeQuery();
    if (result.next()) {
        object.put("username", result.getString("username"));
        object.put("password", result.getString("password"));
        object.put("user_id", result.getString("userId"));
        object.put("status", "success");
        System.out.println("Login success");
    } else {
        System.out.println("Failed to login.");
        object.put("status", "Failed to login. Try again later.");
    }


%>
<%=object.toString()%>