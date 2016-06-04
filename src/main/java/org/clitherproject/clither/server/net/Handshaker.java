/**
 * This file is part of Clither.
 *
 * Clither is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Clither is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Clither.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clitherproject.clither.server.net;

import com.google.common.net.HttpHeaders;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

@SuppressWarnings("rawtypes")
public class Handshaker extends SimpleChannelInboundHandler {
    private WebSocketServerHandshaker serverHandshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext handlerContext, Object request) throws Exception {

        if (request instanceof FullHttpRequest) {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://" + fullRequest.headers().get(HttpHeaders.HOST) + "/", null, true);
            serverHandshaker = wsFactory.newHandshaker(fullRequest);

            if (serverHandshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(handlerContext.channel());
            } else {
                serverHandshaker.handshake(handlerContext.channel(), fullRequest);
            }
        } else if (request instanceof WebSocketFrame) {

            WebSocketFrame frame = (WebSocketFrame) request;

            if (request instanceof CloseWebSocketFrame) {
                if (serverHandshaker != null) {
                    serverHandshaker.close(handlerContext.channel(), ((CloseWebSocketFrame) request).retain());
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
