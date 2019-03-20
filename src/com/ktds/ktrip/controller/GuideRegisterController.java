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

		// ������ ����� ������ ���
		String portfolio_savePath = "/var/lib/tomcat8/webapps/portfolio_img";
		String id_documentsavePath = "/var/lib/tomcat8/webapps/portfolio_img";
		String defaultPhotoPath = "http://13.125.48.37:8080/portfolio_img/default.jpg";
		String dbPath_port;
		String dbPath_id;

		// ���� ũ�� 15MB�� ����
		int sizeLimit = 1024 * 1024 * 15;

		// �� request ��ü, �� ����� ���� ���, �� ���� �ִ� ũ��, �� ���ڵ� ���, �� ���� �̸��� ���ϸ� ���� ó��
		// (HttpServletRequest request, String saveDirectory, int maxPostSize, String
		// encoding, FileRenamePolicy policy)
		// �Ʒ��� ���� MultipartRequest�� ������ ���ָ� ������ ���ε� �ȴ�.(���� ��ü�� ���ε� �Ϸ�)
		MultipartRequest multi = new MultipartRequest(request, portfolio_savePath, sizeLimit, "utf-8",
				new DefaultFileRenamePolicy());

		int user_id = Integer.parseInt(multi.getParameter("user_id"));
		String second_lang = multi.getParameter("second_lang");
		String stay_duration = multi.getParameter("stay_duration");
		String introduction = multi.getParameter("introduction");
		String portfolio = portfolio_savePath + "/" + user_id + "_" + "portfolio" + ".jpg";
		String id_document = id_documentsavePath + "/" + user_id + "_" + "id_document" + ".jpg";
		System.out.println("���� ���ε� �Ϸ�");

		System.out.println("�������̵� " + user_id);
		System.out.println("��2�ܱ��� " + second_lang);
		System.out.println("�Ⱓ " + stay_duration);
		System.out.println("��Ʈ������ " + portfolio);
		System.out.println("�����ڷ� " + id_document);

		if (multi.getFilesystemName("portfolio") == null) {
			portfolio = defaultPhotoPath;
			id_document = defaultPhotoPath;
			dbPath_port = defaultPhotoPath;
			dbPath_id = defaultPhotoPath;
		} else {
			/*
			 * ������ο��� ������ �ҷ����� ������ �̸��� ����ID�� ����
			 */
			File pfile = new File(portfolio_savePath + "/" + multi.getFilesystemName("portfolio"));
			pfile.renameTo(new File(portfolio));
			pfile = new File(id_documentsavePath + "/" + multi.getFilesystemName("id_document"));
			pfile.renameTo(new File(id_document));
			System.out.println("�����̸� ���� �Ϸ�");
			dbPath_port = "http://13.125.48.37:8080/portfolio_img/" + user_id + "_" + "portfolio" + ".jpg";
			dbPath_id = "http://13.125.48.37:8080/portfolio_img/" + user_id + "_" + "id_document" + ".jpg";
		}

		UserDAO dao = new UserDAO();
		int check = dao.registerGuide(user_id, second_lang, stay_duration, introduction, dbPath_port, dbPath_id);
		System.out.println("���̵� ��û ����� " + check);
		if (check == -1) {
			resp.sendRedirect("/ktrip/guide-fail-action.jsp");
		} else {
			resp.sendRedirect("/ktrip/guide-success-action.jsp");
		}
	}

}