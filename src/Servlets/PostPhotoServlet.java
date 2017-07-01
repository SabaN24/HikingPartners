package Servlets;

import Database.GalleryDM;
import Database.HikeFeedDM;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Saba on 29.06.2017.
 */
@WebServlet("/PostPhoto")
public class PostPhotoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");
        if(userID == null){
            response.sendRedirect("/Login");
        }
        ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
        try {
            List<FileItem> files = servletFileUpload.parseRequest(request);
            for(FileItem file : files){
                UUID newFileName = UUID.randomUUID();
                String fileType = file.getContentType().substring(file.getContentType().indexOf('/') + 1);
                String newFilePath = "/Content/img/" + newFileName + "." + fileType;
                File newFile = new File(Helper.root(this) + newFilePath);
                newFile.createNewFile();
                file.write(newFile);
                Helper.savePrefferedSize(newFile);
                Map<String, Object> data = new HashMap<>();
                data.put("imgUrl", newFilePath);
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/Home");
    }
}
