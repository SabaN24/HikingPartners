<%--
  Created by IntelliJ IDEA.
  User: Levani
  Date: 11.07.2017
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="main-content" id="galleryVue">
    <ul class="gallery-list">
        <li class="gallery-li" v-for="image in images">
            <img :src="image.src" alt="">
        </li>
    </ul>
</div>

<script>
    var galleryVue = new Vue({
        el: "#galleryVue",
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