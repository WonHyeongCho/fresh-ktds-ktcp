package com.ktds.ktrip.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ktds.ktrip.dao.UserDAO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/guideRegisterCheck")
public class GuideRegisterController extends HttpServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// 파일이 저장될 서버의 경로
		String portfolio_savePath = "/var/lib/tomcat8/webapps/portfolio_img";
		String id_documentsavePath = "/var/lib/tomcat8/webapps/portfolio_img";
		String defaultPhotoPath = "http://13.125.48.37:8080/portfolio_img/default.jpg";
		String dbPath_port;
		String dbPath_id;

		// 파일 크기 15MB로 제한
		int sizeLimit = 1024 * 1024 * 15;

		// ↓ request 객체, ↓ 저장될 서버 경로, ↓ 파일 최대 크기, ↓ 인코딩 방식, ↓ 같은 이름의 파일명 방지 처리
		// (HttpServletRequest request, String saveDirectory, int maxPostSize, String
		// encoding, FileRenamePolicy policy)
		// 아래와 같이 MultipartRequest를 생성만 해주면 파일이 업로드 된다.(파일 자체의 업로드 완료)
		MultipartRequest multi = new MultipartRequest(request, portfolio_savePath, sizeLimit, "utf-8",
				new DefaultFileRenamePolicy());

		int user_id = Integer.parseInt(multi.getParameter("user_id"));
		String second_lang = multi.getParameter("second_lang");
		String stay_duration = multi.getParameter("stay_duration");
		String introduction = multi.getParameter("introduction");
		String portfolio = portfolio_savePath + "/" + user_id + "_" + "portfolio" + ".jpg";
		String id_document = id_documentsavePath + "/" + user_id + "_" + "id_document" + ".jpg";
		System.out.println("파일 업로드 완료");

		System.out.println("유저아이디 " + user_id);
		System.out.println("제2외국어 " + second_lang);
		System.out.println("기간 " + stay_duration);
		System.out.println("포트폴리오 " + portfolio);
		System.out.println("증빙자료 " + id_document);

		if (multi.getFilesystemName("portfolio") == null) {
			portfolio = defaultPhotoPath;
			id_document = defaultPhotoPath;
			dbPath_port = defaultPhotoPath;
			dbPath_id = defaultPhotoPath;
		} else {
			/*
			 * 사진경로에서 파일을 불러오고 사진의 이름을 유저ID로 변경
			 */
			File pfile = new File(portfolio_savePath + "/" + multi.getFilesystemName("portfolio"));
			pfile.renameTo(new File(portfolio));
			pfile = new File(id_documentsavePath + "/" + multi.getFilesystemName("id_document"));
			pfile.renameTo(new File(id_document));
			System.out.println("파일이름 변경 완료");
			dbPath_port = "http://13.125.48.37:8080/portfolio_img/" + user_id + "_" + "portfolio" + ".jpg";
			dbPath_id = "http://13.125.48.37:8080/portfolio_img/" + user_id + "_" + "id_document" + ".jpg";
		}

		UserDAO dao = new UserDAO();
		int check = dao.registerGuide(user_id, second_lang, stay_duration, introduction, dbPath_port, dbPath_id);
		System.out.println("가이드 신청 결과값 " + check);
		if (check == -1) {
			resp.sendRedirect("/ktrip/guide-fail-action.jsp");
		} else {
			resp.sendRedirect("/ktrip/guide-success-action.jsp");
		}
	}

}