package com.cloud.sso.pro.servlet;


import com.cloud.sso.pro.storage.Cache;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Map;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // 处理退出请求 本来应该用报文封装
        String ST = request.getParameter("CAS-ST");
        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        if (ST == null || "".equals(ST)){ // 说明是主动 退出
            System.out.println("logout");

            response.sendRedirect("http://localhost:8088/logout?service=" + url);
//            PostMethod postMethod = new PostMethod("http://localhost:8088/logout");
//            HttpClient httpClient = new HttpClient();
//            try {
//                httpClient.executeMethod(postMethod);
//                session.invalidate();
//                response.sendRedirect("logoutSuccess");
//                postMethod.releaseConnection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }else {
            // 接收来自服务器的通知 映射关系
            String localST = Cache.map.get("CAS-ST");
            if (ST.equals(localST)){
                session.invalidate();
                Cache.map.remove("CAS-ST");
                response.sendRedirect("logoutSuccess");
            }
        }

    }
}
