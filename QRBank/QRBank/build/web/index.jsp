
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="css/bootstrap.min.css">

        <!-- jQuery library -->
        <script src="libs/jquery-1.12.0.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <title>Bank Login</title>
        <style>
            body {
                padding-top: 90px;
            }
            .panel-login {
                border-color: #ccc;
                -webkit-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
                -moz-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
                box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
            }
            .panel-login>.panel-heading {
                color: #00415d;
                background-color: #fff;
                border-color: #fff;
                text-align:center;
            }
            .panel-login>.panel-heading a{
                text-decoration: none;
                color: #666;
                font-weight: bold;
                font-size: 15px;
                -webkit-transition: all 0.1s linear;
                -moz-transition: all 0.1s linear;
                transition: all 0.1s linear;
            }
            .panel-login>.panel-heading a.active{
                color: #029f5b;
                font-size: 18px;
            }
            .panel-login>.panel-heading hr{
                margin-top: 10px;
                margin-bottom: 0px;
                clear: both;
                border: 0;
                height: 1px;
                background-image: -webkit-linear-gradient(left,rgba(0, 0, 0, 0),rgba(0, 0, 0, 0.15),rgba(0, 0, 0, 0));
                background-image: -moz-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
                background-image: -ms-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
                background-image: -o-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
            }
            .panel-login input[type="text"],.panel-login input[type="email"],.panel-login input[type="password"] {
                height: 45px;
                border: 1px solid #ddd;
                font-size: 16px;
                -webkit-transition: all 0.1s linear;
                -moz-transition: all 0.1s linear;
                transition: all 0.1s linear;
            }
            .panel-login input:hover,
            .panel-login input:focus {
                outline:none;
                -webkit-box-shadow: none;
                -moz-box-shadow: none;
                box-shadow: none;
                border-color: #ccc;
            }
            .btn-login {
                background-color: #59B2E0;
                outline: none;
                color: #fff;
                font-size: 14px;
                height: auto;
                font-weight: normal;
                padding: 14px 0;
                text-transform: uppercase;
                border-color: #59B2E6;
            }
            .btn-login:hover,
            .btn-login:focus {
                color: #fff;
                background-color: #53A3CD;
                border-color: #53A3CD;
            }
            .forgot-password {
                text-decoration: underline;
                color: #888;
            }
            .forgot-password:hover,
            .forgot-password:focus {
                text-decoration: underline;
                color: #666;
            }

            .btn-register {
                background-color: #1CB94E;
                outline: none;
                color: #fff;
                font-size: 14px;
                height: auto;
                font-weight: normal;
                padding: 14px 0;
                text-transform: uppercase;
                border-color: #1CB94A;
            }
            .btn-register:hover,
            .btn-register:focus {
                color: #fff;
                background-color: #1CA347;
                border-color: #1CA347;
            }
            a.button {
                display: block;
                width: 150px;
                text-decoration: none;
                background-color: #1CB94A;
                color: #fff;
                font-weight: bolder;
                padding-top: 10px;
                padding-bottom: 10px;
            }
            a.button:hover {
                display: block;
                width: 150px;
                text-decoration: none;
                background-color: #1CA347;
                color: #fff;
                font-weight: bolder;
            }
        </style>
        <script>
            $(function () {

                $('#login-form-link').click(function (e) {
                    $("#login-form").delay(100).fadeIn(100);
                    $("#register-form").fadeOut(100);
                    $('#register-form-link').removeClass('active');
                    $(this).addClass('active');
                    e.preventDefault();
                });
                $('#register-form-link').click(function (e) {
                    $("#register-form").delay(100).fadeIn(100);
                    $("#login-form").fadeOut(100);
                    $('#login-form-link').removeClass('active');
                    $(this).addClass('active');
                    e.preventDefault();
                });

            });
        </script>
        <script>
            function validateRegister(form) {
                var fullName = form.fullName.value;
                if(fullName.length <= 0) {
                    alert("Enter full name.");
                    return false;
                }
                var mobile = form.mobile_num.value;
                if(mobile.length !== 10) {
                    alert("Enter valid mobile number.");
                    return false;
                }
                for(var i=0;i<mobile.length;i++) {
                    if(isNaN(mobile[i])) {
                        alert("Enter valid mobile number. " + mobile[i]);
                        return false;
                    }
                }
                var imei = form.imei.value;
                if(imei.length !== 15) {
                    alert("Enter valid imei number.");
                    return false;
                }
                for(var i=0;i<imei.length;i++) {
                    if(isNaN(imei[i])) {
                        alert("Enter valid imei number. " + imei[i]);
                        return false;
                    }
                }
                var username = form.username.value;
                if(username.length <= 0) {
                    alert("Enter username.");
                    return false;
                }
                var password = form.password.value;
                if(password.length <= 0) {
                    alert("Enter password.");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="panel panel-login">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-6">
                                    <a href="#" class="active" id="login-form-link">Login</a>
                                </div>
                                <div class="col-xs-6">
                                    <a href="#" id="register-form-link">Register</a>
                                </div>
                            </div>
                            <hr>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <form id="login-form" action="./GenerateQR" method="POST" role="form" style="display: block;">
                                        <%
                                            if (request.getAttribute("errors") != null) {
                                                ArrayList<String> errors = (ArrayList<String>) request.getAttribute("errors");
                                                for (String error : errors) {
                                        %> <p width="150px" align="center" colspan="2"><font color="red"><%=error%></font><p>

                                            <%
                                                    }
                                                }
                                            %>									<div class="form-group">
                                            <input type="text" name="username" id="username" tabindex="1" class="form-control" placeholder="Username" value="">
                                        </div>

                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-6 col-sm-offset-3">
                                                    <input type="submit" name="login" id="login-submit" tabindex="4" class="form-control btn btn-login danger" value="Log In">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <center><a class="button" style="float: left;" href="RecoverAccount.jsp">I lost my mobile.</a><a class="button" href="ForgetPassword.jsp" style="float: right;">Forget password.</a></center>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                            <form id="register-form" action="Register" method="post" role="form" style="display: none;" onsubmit="return validateRegister(this);">
                                        <div class="form-group">
                                            <input type="text" name="fullName" id="fullName" tabindex="1" class="form-control" placeholder="Full Name" value="" required="">
                                        </div>
                                        <div class="form-group">
                                            <input type="text" name="mobile_num" id="mobile" tabindex="1" class="form-control" placeholder="Mobile" value="" required="">
                                        </div>
                                        <div class="form-group">
                                            <input type="text" name="imei" id="mobile" tabindex="1" class="form-control" placeholder="IMEI Number" value="" required="">
                                        </div>
                                        <div class="form-group">
                                            <input type="text" name="username" id="username" tabindex="1" class="form-control" placeholder="User name" value="" required="">
                                        </div>
                                        <div class="form-group">
                                            <input type="password" name="password" id="password" tabindex="2" class="form-control" placeholder="Password" required="">
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-6 col-sm-offset-3">
                                                    <input type="submit" name="register-submit" id="register-submit" tabindex="4" class="form-control btn btn-register" value="Register Now">
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
