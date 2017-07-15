package Servlets;

import Database.HikeDM;
import Database.MainDM;
import Models.Hike.HikeInfo;
import Models.Photo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Levani on 04.07.2017.
 * Uploads cover photo, stores it in /Content/img folder and adds it to database.
 */
@WebServlet("/UploadCover")
public class UploadCoverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");
        if(userID == null){
            response.sendRedirect("/Login");
        }
        ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
        HikeDM hikeDM = HikeDM.getInstance();
        try {
            List<FileItem> files = servletFileUpload.parseRequest(request);
            UUID newFileName;
            List<String> descriptions = new ArrayList<>();
            String newFilePath;
            int hikeID = Integer.parseInt(request.getParameter("hikeID"));
            int i = 0;
            for(FileItem file : files){
                if(file.isFormField()){
                    descriptions.add(file.getString());
                }else{
                    String description = descriptions.get(i);
                    newFileName = UUID.randomUUID();
                    String fileType = file.getContentType().substring(file.getContentType().indexOf('/') + 1);
                    newFilePath = "/Content/img/" + newFileName + "." + fileType;
                    File newFile = new File(Helper.root(this) + newFilePath);
                    newFile.createNewFile();
                    file.write(newFile);
                    Helper.savePrefferedSize(newFile);
                    hikeDM.addCoverPhoto(description, newFilePath, hikeID);
                    i++;
                }
            }
            List<Photo> photos = MainDM.getInstance().getCoverPhotos(hikeID);
            Gson gson = new GsonBuilder().serializeNulls().create();
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(gson.toJson(photos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/Home");
    }
}
