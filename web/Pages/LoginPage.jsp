<%--
  Created by IntelliJ IDEA.
  User: vache
  Date: 6/22/2017
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<setTitle>
    Login
</setTitle>

<div id = "status"></div>

<div class="login-content">
    <div class="login-logo">
        <img src="../Content/img/logo.png" alt="" class="login-logo-img">
    </div>
    <div class="login-bar">
        <button class="login-button" class="button" onclick="login();">
            <i class="fa fa-facebook-f"></i><span>Log In with Facebook </span>
        </button>
    </div>
</div>

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