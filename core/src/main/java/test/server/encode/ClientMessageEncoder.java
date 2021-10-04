package test.server.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.log.Log;
import test.server.messages.ClientMessage;

public class ClientMessageEncoder extends MessageToByteEncoder<ClientMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientMessage msg, ByteBuf out) throws Exception {
        if (msg == null){
            ctx.close();
            Log.e("сообщение null!!!");
            return;
        }
        out.writeInt(msg.size);
        for (int i = 0; i < msg.size; i++) {
            out.writeInt(msg.keys[i]);
        }
        msg.recycle();
    }
}
