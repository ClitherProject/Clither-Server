package org.clitherproject.clither.server.net;

import com.google.common.net.HttpHeaders;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

@SuppressWarnings("rawtypes")
public class Handshaker extends SimpleChannelInboundHandler {
    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext handlerContext, Object request) throws Exception {

        if (request instanceof FullHttpRequest) {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://" + fullRequest.headers().get(HttpHeaders.HOST) + "/", null, true);
            handshaker = wsFactory.newHandshaker(fullRequest);

            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(handlerContext.channel());
            } else {
                handshaker.handshake(handlerContext.channel(), fullRequest);
            }
        } else if (request instanceof WebSocketFrame) {

            WebSocketFrame frame = (WebSocketFrame) request;

            if (request instanceof CloseWebSocketFrame) {
                if (handshaker != null) {
                    handshaker.close(handlerContext.channel(), ((CloseWebSocketFrame) request).retain());
                }
            } else if (request instanceof PingWebSocketFrame) {
                handlerContext.channel().write(new PongWebSocketFrame(frame.content().retain()));
            } else {
                handlerContext.fireChannelRead(frame.retain());
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
