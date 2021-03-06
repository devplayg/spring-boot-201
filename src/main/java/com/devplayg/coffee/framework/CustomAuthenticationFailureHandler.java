package com.devplayg.coffee.framework;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private AuditService auditService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
        HashMap<String, Object> m = new HashMap<>();
        m.put("uri", req.getRequestURI());
        m.put("method", req.getMethod());

        // Save input parameters but password
        HashMap<String, String[]> param = new HashMap<>(req.getParameterMap());
        param.put("app_password", null);
        m.put("parameter", param);
        auditService.audit(AuditCategory.LOGIN_FAILED, m);

        res.sendRedirect("/login?error&username=" + req.getParameter("app_username"));
    }

//    public String getBody(HttpServletRequest request) throws IOException {
//
//        String body = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//
//        try {
//            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//                stringBuilder.append("");
//            }
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    throw ex;
//                }
//            }
//        }
//
//        body = stringBuilder.toString();
//        return body;
//    }
}
