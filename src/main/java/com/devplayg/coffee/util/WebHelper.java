package com.devplayg.coffee.util;

import org.springframework.web.servlet.view.RedirectView;

public class WebHelper {
    public static RedirectView getRedirectView(String uri) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uri);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }
//
//    public static HashMap<String, Object> getRequestSummary(HttpServletRequest req) {
//        HashMap<String, Object> m  = new HashMap<>();
//        m.put("uri", req.getRequestURI());
//        m.put("method", req.getMethod());
//        try {
//            m.put("body", CharStreams.toString(req.getReader()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return m;
//    }
}
