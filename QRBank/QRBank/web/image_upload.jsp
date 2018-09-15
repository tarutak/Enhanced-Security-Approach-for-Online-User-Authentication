<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="java.util.Random"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.ByteArrayInputStream"%>
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
//    Class.forName(Constants.className);
//    Connection connection = DriverManager.getConnection(Constants.url, Constants.username, Constants.password);
    /* String username = request.getParameter("username");
     String password = request.getParameter("password");
     String email=request.getParameter("email");
     String mobile=request.getParameter("mobile");
     */
    int max = 100;
    int min = 1;
    Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;
    System.out.print(randomNum);
    String image = request.getParameter("image");
    String userId = request.getParameter("user_id");
    if(userId == null || userId.isEmpty()) {
        userId = "1";
    }
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] imageBytes = decoder.decodeBuffer(image);
    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
    BufferedImage face = ImageIO.read(bis);
    File faceFile = new File(Constants.session + userId + ".jpg");
    ImageIO.write(face, "jpg", faceFile);
    object.put("status", "success");
//    PreparedStatement statement = connection.prepareStatement("INSERT INTO auth (image) VALUES (?)");
//    statement.setString(1, faceFile.getAbsolutePath());
//    int status = statement.executeUpdate();
    /* if(status > 0) {
     statement = connection.prepareStatement("SELECT user_id FROM registration WHERE user_name=? AND password=?");
     statement.setString(1, username);
     statement.setString(2, password);
     statement.setString(3, email);
     ResultSet result = statement.executeQuery();
     if(result.next()) {
     object.put("status", "success");
     object.put("user_id", result.getString("user_id"));
     } else {
     object.put("status", "Failed to register. Try again later.");
     }
     } else {
     object.put("status", "Failed to register. Try again later."); */

%>
<%=object.toString()%>