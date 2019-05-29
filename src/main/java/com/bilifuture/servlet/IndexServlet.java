package com.bilifuture.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//@WebServlet(urlPatterns= {"/aaa","/bbb"},asyncSupported=true,loadOnStartup=1)
public class IndexServlet extends HttpServlet{

	private static final long serialVersionUID = -4727488979319097172L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
//		Collection<Part> parts = req.getParts();
//		for (Part part : parts) {
//			
//		}
		PrintWriter out = resp.getWriter();
		out.println("进入Servlet的时间：" + new Date() + ".");
		out.flush();
		//在子线程中执行业务调用，并由其负责输出响应，主线程退出
		AsyncContext ctx = req.startAsync();
		ctx.addListener(new MyServletListener());
		ctx.start(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					PrintWriter writer = ctx.getResponse().getWriter();
					writer.println("子任務執行完成");
					writer.flush();
					ctx.complete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		out.println("结束Servlet的时间：" + new Date() + ".");
		out.flush();
}
}
class MyServletListener implements AsyncListener{

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		AsyncContext context = event.getAsyncContext();
		ServletResponse response = context.getResponse();
		PrintWriter writer = response.getWriter();
		writer.println("當前servelt已經完成工作");
		writer.flush();
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		AsyncContext context = event.getAsyncContext();
		ServletResponse response = context.getResponse();
		PrintWriter writer = response.getWriter();
		writer.println("當前servelt超時");
		writer.flush();
		
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		AsyncContext context = event.getAsyncContext();
		ServletResponse response = context.getResponse();
		PrintWriter writer = response.getWriter();
		writer.println("當前servelt發生異常");
		writer.flush();
		
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		AsyncContext context = event.getAsyncContext();
		ServletResponse response = context.getResponse();
		PrintWriter writer = response.getWriter();
		writer.println("當前servelt開始異步方法");
		writer.flush();
		
	}
	
}