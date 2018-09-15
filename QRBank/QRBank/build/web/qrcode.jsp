

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="refresh" content="100">
<%
    String fileName = request.getParameter("fileName");
    if(fileName == null) {
        fileName = "";
    }
    ArrayList<String> errors = new ArrayList<String>();
    if(fileName.length() <= 0) {
        errors.add("Invalid file.");
        request.setAttribute("errors", errors);
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
        return;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="js/jquery-1.11.3.min.js"></script>
        <script>
            $(document).ready(function() {
                function update() {
                    $("#image").attr('src', 'uploads/<%=fileName %>');
                    window.setTimeout(check, 5000);
                }
                function check() {
                    $.ajax( {
                        url:'./CheckSession',
                        success:function(data) {
                            if($.trim(data) === "success") {
                                window.location.href = "Welcome.jsp";
                            } else {
                                $("#status").html(data);
                                window.setTimeout(check, 5000);
                            }
                        }
                    });
                }
                window.setTimeout(update, 5000);
            });
        </script>
        <title>QR Code</title>
       <script src="libs/jquery-1.12.0.min.js"></script>
    </head>
    <body>
        <div style="border: #000; text-align: center;">
            <img src="images/loading.gif" id="image" width="500px" height="500px" /><br/>
        </div>
        <p id="status"></p>

    </body>
</html>