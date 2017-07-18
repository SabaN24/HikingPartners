<%--
  Created by IntelliJ IDEA.
  User: Levani
  Date: 11.07.2017
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="/Content/css/lightbox.min.css" rel="stylesheet">

<div class="main-content" v-if="images.length">
    <ul class="gallery-list">
        <li class="gallery-li" v-for="image in images">
            <a :href="image.src" data-lightbox="viewer">
                <img :src="image.src" alt="">
            </a>
        </li>
    </ul>
</div>
<script src="/Content/js/lightbox.min.js"></script>
<script>
    lightbox.option({
        fadeDuration: 500,
        imageFadeDuration: 400,
        resizeDuration: 500,
        wrapAround: true
    });
    var galleryVue = new Vue({
        el: "#vueapp",
        data: {
            images: []
        },
        methods: {
            fetchData: function(){
                var th = this;
                axios.post("/GetGalleryServlet?hikeID=" + hikeId, {}).then(function (response) {
                    th.images = response.data;
                });
            }
        },
        created: function () {
            this.fetchData();
        }
    });
</script>