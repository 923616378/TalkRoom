package com.snake.webSocket.interceptor;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 握手拦截器
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private static final Logger log = Logger.getLogger(HandshakeInterceptor.class);
    //握手前方法
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //获取httpsession
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);
            if (httpSession != null) {
                log.debug("HttpSessionId:"+httpSession.getId());
                attributes.put("currHttpSession",httpSession);
            }else{
                log.debug("httpsession is null");
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
    //握手后方法
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
