package io.hugang.mock.filter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.hugang.mock.entity.TApiEntity;
import io.hugang.mock.entity.TAttachmentEntity;
import io.hugang.mock.service.TApiService;
import io.hugang.mock.service.TAttachmentService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Component
@AllArgsConstructor
public class ApiFilter implements Filter {
    private final TApiService tApiService;
    private final TAttachmentService tAttachmentService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // get url, method
        String url = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String mockUi = httpRequest.getHeader("__mock_ui__");

        if (StrUtil.isNotEmpty(mockUi)) {
            filterChain.doFilter(request, response);
            return;
        }
        // prepare response
        Enumeration<String> origins = httpRequest.getHeaders("Origin");
        if (origins.hasMoreElements()) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origins.nextElement());
        } else {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        }

        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        Enumeration<String> accessControlRequestHeaders = httpRequest.getHeaders("Access-Control-Request-Headers");
        if (accessControlRequestHeaders.hasMoreElements()) {
            httpResponse.setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders.nextElement());
        } else {
            httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        }

        if ("OPTIONS".equals(method)) {
            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            httpResponse.getWriter().write("");
            return;
        }

        // use tapiService to get data
        List<TApiEntity> allApis = tApiService.getBaseMapper().selectList(null);
        for (TApiEntity api : allApis) {
            // 比较url和method
            boolean match = api.getMethod().equals(method) && api.getUrl().equals(url);
            // 比较param
            if (StrUtil.isNotEmpty(api.getParam())) {
                match = match && url.contains(api.getParam());
            }
            // 打印日志
            System.out.println("url: " + url + ", method: " + method + ", param: " + api.getParam() + ", match: " + match);
            // 返回数据
            if (match && StrUtil.isNotEmpty(api.getData())) {
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.getWriter().write(JSONUtil.parseObj(api.getData()).toString());
                return;
            } else if (match && StrUtil.isNotEmpty(api.getAttachmentName())) {
                TAttachmentEntity attachmentEntity = tAttachmentService.getById(api.getAttachmentId());

                // 返回附件
                httpResponse.setHeader("Content-Disposition", "attachment;filename=" + api.getAttachmentName());
                httpResponse.setContentType("application/octet-stream");
                httpResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

                byte[] bytes = FileUtil.readBytes(attachmentEntity.getUrl());
                httpResponse.getOutputStream().write(bytes);
                return;
            }
        }
        httpResponse.getWriter().write("no data");
    }

}
