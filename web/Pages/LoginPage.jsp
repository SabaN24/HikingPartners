<%--
  Created by IntelliJ IDEA.
  User: vache
  Date: 6/22/2017
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hiking Partners</title>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title></title>
        <link rel="stylesheet" href="../Content/css/normalize.css">
        <link rel="stylesheet" href="../Content/css/font-awesome.min.css">
        <link rel="stylesheet" href="../Content/css/login-page.css">
    </head>
</head>
<body>

<script>
    window.fbAsyncInit = function() {
        FB.init({
            appId      : '237100913454789',
            cookie     : true,
            xfbml      : true,
            version    : 'v2.8'
        });
        FB.AppEvents.logPageView();
    };

    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    function login() {
        FB.getLoginStatus(function(response) {
            FB.login(function (response) {
                if (response.status === 'connected')
                    //getData();
                    window.location = '/Home'
            }, {scope: 'user_birthday, email'});
        });
    }

    function getData() {
        FB.api('/me?fields=name,email,age_range,link,id,picture,gender,birthday', function (response) {
            document.getElementById('status').innerHTML = response.birthday;
        });
    }

</script>

<div id = "status"></div>
<div class="logo">
    <img src="../Content/img/logo.png" alt="" class="logo-img">

</div>
<div class="login-bar">
    <i class="fa fa-facebook-f"></i>
    <%--<button class="button"
            onclick="window.location = '/HikePage/Home'"
            style="vertical-align:middle"><span>Log In with Facebook </span></button>--%>
    <button class="button"
            onclick="login();"
            style="vertical-align:middle"><span>Log In with Facebook </span></button>


</div>


<footer>
    <div class="footer-info"> All Rights Reserved  © HikingPartners.ge  2017</div>
</footer>
</body>
</html>
