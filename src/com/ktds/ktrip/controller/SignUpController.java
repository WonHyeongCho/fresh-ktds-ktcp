package com.ktds.ktrip.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ktds.ktrip.dao.UserDAO;
import com.ktds.ktrip.domain.UserVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/signupCheck")
public class SignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		//������ ����� ������ ���
		String savePath = "/var/lib/tomcat8/webapps/user_img";
		String defaultPhotoPath="/var/lib/tomcat8/webapps/user_img/default.jpg";
		String picturePath;
		String dbPath;
		
		//���� ũ�� 15MB�� ����
		int sizeLimit = 1024*1024*15;

		//�� request ��ü,               �� ����� ���� ���,       �� ���� �ִ� ũ��,    �� ���ڵ� ���,       �� ���� �̸��� ���ϸ� ���� ó��
		//(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding, FileRenamePolicy policy)
		//�Ʒ��� ���� MultipartRequest�� ������ ���ָ� ������ ���ε� �ȴ�.(���� ��ü�� ���ε� �Ϸ�)
		MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());

		UserVO vo = new UserVO();
		vo.setId(multi.getParameter("id"));
		vo.setPwd(multi.getParameter("pwd"));
		vo.setEmail(multi.getParameter("email"));
		vo.setName(multi.getParameter("name"));
		vo.setSex(multi.getParameter("sex"));
		vo.setPhone_num(multi.getParameter("phone_num"));
		vo.setNational(multi.getParameter("country"));
		vo.setResidential_contry(multi.getParameter("residential_country"));
		if(multi.getFilesystemName("photo")==null) {
			dbPath="http://13.125.48.37:8080/user_img/default.jpg";
		}else {
			picturePath=savePath+"/"+multi.getParameter("id")+".jpg";
			/*
			 * ������ο��� ������ �ҷ����� ������ �̸��� ����ID�� ����
			 */
			File file=new File(savePath+"/"+multi.getFilesystemName("photo"));
			file.renameTo(new File(picturePath));
			dbPath = "http://13.125.48.37:8080/user_img/" + multi.getParameter("id") + ".jpg";
		}

		System.out.println("������ "+savePath);
		//System.out.println("������� "+picturePath);
		System.out.println("������ �Ѿ���� ���� �̸� "+multi.getFilesystemName("photo"));
		
		System.out.println("�̸�����Ϸ�");
		System.out.println(vo.toString());

		UserDAO dao = new UserDAO();
		int cheackvalue = dao.insertUser(vo, dbPath);

		if (cheackvalue == -1) {
			response.sendRedirect("/ktrip/signup-fail-action.jsp");
		}else {
			dao.login(vo);
			session.setAttribute("user_id", vo.getNum_id());
			request.setAttribute("success", "true");
			response.sendRedirect("/ktrip/index.jsp");
		}
	}
}
