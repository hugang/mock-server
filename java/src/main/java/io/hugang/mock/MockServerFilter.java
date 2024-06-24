package io.hugang.mock;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.hugang.mock.MockServerController.BASE_DIR;

@Component
public class MockServerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // if request url is not /mock/api, print error message
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        if ((requestURI.equals("/mock/api/add") || requestURI.equals("/mock/api/delete"))
                && request.getMethod().equals("POST")) {
            System.out.println("system api to manage mock data");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            System.out.println("mock api to get mock data");

            // origin
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            // credentials
            response.setHeader("Access-Control-Allow-Credentials", "true");
            // method
            response.setHeader("Access-Control-Allow-Methods", "*");
            // age
            response.setHeader("Access-Control-Max-Age", "3600");
            // header
            response.setHeader("Access-Control-Allow-Headers", "*");

            MockServerController bean = SpringUtil.getBean(MockServerController.class);
            UrlEntry urlEntry = new UrlEntry();
            urlEntry.setUrl(requestURI);
            urlEntry.setMethod(request.getMethod());
            urlEntry.setParam(request.getQueryString());
            UrlEntry urlEntryResult = bean.getUrl(urlEntry);
            if (urlEntryResult == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // return code
            response.setStatus(urlEntryResult.getCode());
            // return type
            if (urlEntryResult.getType() != null) {
                response.setHeader("Content-Type", urlEntryResult.getType());
                String respFile = urlEntryResult.getData().toString();
                response.setHeader("Content-Disposition", "attachment; filename=" + respFile);
                // get file bytes
                byte[] bytes = Files.readAllBytes(Paths.get(BASE_DIR + "/" + respFile));
                // response bytes
                response.getOutputStream().write(bytes);
                response.getOutputStream().close();
            } else {
                response.setHeader("Content-Type", "application/json");
                // return data
                response.getWriter().write(JSONUtil.toJsonStr(urlEntryResult.getData()));
                response.getWriter().close();
            }
        }
    }
}
