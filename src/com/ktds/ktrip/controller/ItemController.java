package com.ktds.ktrip.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ktds.ktrip.domain.ApplyVO;
import com.ktds.ktrip.domain.ItemVO;
import com.ktds.ktrip.dao.ItemDAO;

//Leeyong coding guide_item list & CRUD

@WebServlet("/itemServlet")
public class ItemController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		myFunction(request, response);
	}

	protected void myFunction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
//		
		HttpSession session = request.getSession();
		session.setAttribute("guide_id", 2); // �α��ο��� session ���� ����

		String actionMode = request.getParameter("actionMode");

		if (actionMode.equals(null)) {
			int guide_id = (int)session.getAttribute("guide_id");
			response.sendRedirect("/ktrip/itemServlet?actionMode=LIST&user_id=" + guide_id);

		} else if (actionMode.equals("INS")) {
			// item�� �߰��� ���

			ItemVO item = new ItemVO();
			int guide_id = (int) session.getAttribute("guide_id");
			item.setTitle(request.getParameter("title"));
			item.setConcept(request.getParameter("concept"));
			item.setContents(request.getParameter("contents"));
			item.setDestination(request.getParameter("destination"));
			item.setDuration(Integer.parseInt(request.getParameter("duration")));
			item.setGuide_id((int) session.getAttribute("guide_id"));
			item.setItem_status(request.getParameter("item_status"));
			item.setNum_max(Integer.parseInt(request.getParameter("num_max")));
			item.setNum_min(Integer.parseInt(request.getParameter("num_min")));
			item.setPrice(Integer.parseInt(request.getParameter("price")));
			item.setThumbnail(request.getParameter("thumbnail"));

			ItemDAO idao = new ItemDAO();
			idao.addProduct(item);

			// RequestDispatcher rd =
			// request.getRequestDispatcher("/itemServlet?actionMode=LIST");
			// rd.forward(request, response);
			response.sendRedirect("/ktrip/itemServlet?actionMode=LIST&user_id=" + guide_id);
		} else if (actionMode.equals("UPDATE")) {
			// item�� ������ ��� fin

			ItemVO item = new ItemVO();
			// List<ItemVO> list = new ArrayList<ItemVO>();
			ItemDAO idao = new ItemDAO();

			int guide_id = (int) session.getAttribute("guide_id");
			item.setItem_id(Integer.parseInt(request.getParameter("item_id")));
			item.setTitle(request.getParameter("title"));
			item.setConcept(request.getParameter("concept"));
			item.setContents(request.getParameter("contents"));
			item.setDuration(Integer.parseInt(request.getParameter("duration")));
			item.setNum_max(Integer.parseInt(request.getParameter("num_max")));
			item.setNum_min(Integer.parseInt(request.getParameter("num_min")));
			item.setPrice(Integer.parseInt(request.getParameter("price")));
			item.setThumbnail(request.getParameter("thumbnail"));
			
			idao.updateItem(item, guide_id);
			response.sendRedirect("/ktrip/itemServlet?actionMode=LIST&user_id=" + guide_id);

		} else if (actionMode.equals("DEL")) {
			// ������ ������ �����Ѱ�� Del fin

			ItemVO item = new ItemVO();
			ItemDAO idao = new ItemDAO();

			int item_id = Integer.parseInt(request.getParameter("item_id"));
			// user_id�� ���ǿ��� �޾ƿ�

			// list�� �Ѱ���

			idao.delProduct(item_id);
			System.out.println("DEL �Ϸ�");
			RequestDispatcher rd = request.getRequestDispatcher("/itemServlet?actionMode=LIST");
			rd.forward(request, response);

		} else if (actionMode.equals("STATCHANGE")) {
			// ��
			ItemDAO idao = new ItemDAO();
			ItemVO item = new ItemVO();

			int apply_id = Integer.parseInt(request.getParameter("apply_id"));
			int user_id = Integer.parseInt(request.getParameter("user_id"));// ��û��id
			int item_status = Integer.parseInt(request.getParameter("item_status"));
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			idao.changeStatus(user_id, item_status, apply_id);
			int guide_id = (int) session.getAttribute("guide_id");
			
			response.sendRedirect("/ktrip/itemServlet?actionMode=SELECT&user_id=" + guide_id);
			
		} else if (actionMode.equals("SELECT")) {

			// ��ǰ�� �������� ��� fin
			System.out.println("ȣ�����>>>");

			ItemDAO idao = new ItemDAO();
			List<ApplyVO> applyList = new ArrayList<ApplyVO>();
			int guide_id = (int) session.getAttribute("guide_id");// (request.getParameter("user_id"));
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			// list = idao.showAll(user_id);
			System.out.println("item_ID>>>" + item_id);

			ItemVO item = idao.showSelect(item_id, guide_id);
			applyList = idao.showApply(item_id);

			request.setAttribute("applyList", applyList);
			request.setAttribute("item", item);
			for (int i = 0; i < applyList.size(); i++) {
				System.out.println("SEL>>>" + applyList.get(i).getName());
				System.out.println("SEL>>>" + applyList.get(i).getStatusToString());
			}
			System.out.println("SEL>>>" + item.getTitle());
			System.out.println("SEL>>>" + item.getNum_min());
			System.out.println("SEL>>" + item.getPrice());
			System.out.println("SEL>>>" + item.getNum_max());
			RequestDispatcher rd = request.getRequestDispatcher("/guide_data.jsp");
			// response.sendRedirect("/guide_data.jsp");
			rd.forward(request, response);
			// ���� �������� ���� �����͸� ������������ �Ѱ��ش�.

		} else if (actionMode.equals("LIST")) {

			ItemDAO idao = new ItemDAO();
			List<ItemVO> list = new ArrayList<ItemVO>();
			// db�� �ִ� ��� ����Ʈ ������

			int guide_id = (int) session.getAttribute("guide_id");
			list = idao.showAll(guide_id);
			request.setAttribute("list", list);
			for (int i = 0; i < list.size(); i++) {
				System.out.println("LIST>>>" + list.get(i).getTitle());
				System.out.println("LIST>>>" + list.get(i).getDuration());
			}
			RequestDispatcher rd = request.getRequestDispatcher("/guide.jsp");
			rd.forward(request, response);
		}

	}
}