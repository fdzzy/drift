package com.drift.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DBConnector;
import com.drift.core.User;
import com.drift.util.PhotoUtil;

/**
 * Servlet implementation class UploadPhoto2Servlet
 */
@WebServlet("/upload2")
public class UploadPhoto2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadPhoto2Servlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		
		User user = MyServletUtil.checkLogin(request, response);
		if(user == null) {
			return;
		}
		
		int uid = user.getUid();		
		//String path = request.getRealPath("photo") + "\\";	//上传文件目录
		String path = getServletContext().getRealPath("/photo"); //上传文件目录
		System.out.println(path);
		
		int status = PhotoUtil.uploadPhoto2(request, path, uid);
		if(status == DBConnector.DB_STATUS_OK) {
			OutputStream out = response.getOutputStream();
			out.write("upload succeed".getBytes());
		}
/*		File uploadPath = new File(path);//上传文件目录

	
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		// 临时文件目录
		File tempPathFile = new File(path+"/tmp");
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}
		try {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
			factory.setRepository(tempPathFile);//设置缓冲区目录

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(524288);  // 设置最大文件尺寸，这里是512KB

			List<FileItem> items = upload.parseRequest(request);//得到所有的文件
			Iterator<FileItem> i = items.iterator();
			//PhotoHandler photoHandler = new LocalPhotoHandler();
			int dbStatus = DBConnector.DB_STATUS_ERR_GENERIC;
			while (i.hasNext()) {
				//System.out.println("hasNext:");
				FileItem fi = (FileItem) i.next();
				String fileName = fi.getName();
				//System.out.println(fileName);
				if (fileName != null) {
					//File fullFile = new File(fi.getName());
					//System.out.println(fullFile);
					long currentTime = System.currentTimeMillis();
					//String newFileName = user.getEmail()+"_"+Long.toString(currentTime)+".jpg";
					String dateStr = MyServletUtil.timestampToDate(user.getRegisterTs());			
					String uploadPath = path + "/" + dateStr + "/" + uid; 
					File userpath = new File(uploadPath);
					System.out.println(userpath);
					if(!userpath.exists())
						userpath.mkdirs();
					String newFileName = Long.toString(currentTime)+".jpg";
					//System.out.println(newFileName);
					File savedFile = new File(userpath, newFileName);
					fi.write(savedFile);
					dbStatus = DBConnector.setPhoto(uid, newFileName);
					
					FileUtil.limitFiles(uploadPath);
				}
			}
			if(dbStatus == DBConnector.DB_STATUS_OK) {
				OutputStream out = response.getOutputStream();
				out.write("upload succeed".getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
