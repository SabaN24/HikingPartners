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

    function post(path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.

        // The rest of this code assumes you are not using a library.
        // It can be made less wordy if you use one.
        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);

        for(var key in params) {
            if(params.hasOwnProperty(key)) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);

                form.appendChild(hiddenField);
            }
        }

        document.body.appendChild(form);
        form.submit();
    }

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
                    loginUser(response.authResponse.accessToken);
            }, {scope: 'user_birthday, email'});
        });
    }

    function loginUser(accessToken) {
        FB.api('/me?fields=name,email,age_range,link,id,picture,gender,birthday', function (response) {
//            console.log("----fb data----");
//            console.log(response);
            post("/LoginServlet", {
                accessToken: accessToken,
                name: response.name,
                email: response.email,
                age_range_min: response.age_range.min,
                age_range_max: response.age_range.max,
                link: response.link,
                id: response.id,
                picture_url: response.picture.data.url,
                gender: response.gender,
                birthday: response.birthday
            });
        });
    }

</script>