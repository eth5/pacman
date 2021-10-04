package test.server.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import test.serialization.ISerializer;
import test.server.messages.InitialGameDataMessage;
import test.server.messages.MessageType;
import test.server.messages.ServerCommand;
import test.server.messages.State;

import java.nio.charset.Charset;
import java.util.List;

public class MessagesDecoder extends ByteToMessageDecoder {
    protected final ISerializer serializator;
    public MessagesDecoder(ISerializer serializator){
        this.serializator = serializator;
    }
    private boolean readHead = true;
    private int len;
    private  int type;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (readHead){
            if (in.readableBytes() < 8) return;
            readHead = false;
            len = in.readInt();
            type = in.readInt();
            // Log.i("получил длину сообщения: " + len + " байт type: " + type);
            return;
        }
        // Log.i("получаю сообщение...");
        if (in.readableBytes() < len) return;

        ByteBuf byteBuf = in.readBytes(len);
        CharSequence charSequence = byteBuf.readCharSequence(len, Charset.defaultCharset());

        // Log.i(this, "прилетело: " + type);
        // Log.i(this, charSequence.toString());

        Object object = decodeJsonToObject(type,charSequence.toString());
        out.add(object);

        byteBuf.release();
        readHead = true;
    }
    protected Object decodeJsonToObject(int type, String jsonString) {
        switch (type){
            case MessageType.SERVER_COMMAND: return serializator.createFromJson(jsonString, ServerCommand.class);
            case MessageType.STATE: return serializator.createFromJson(jsonString, State.class);
            case MessageType.INITIAL_GAME_DATA: return serializator.createFromJson(jsonString, InitialGameDataMessage.class);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
