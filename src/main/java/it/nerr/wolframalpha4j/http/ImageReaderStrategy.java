package it.nerr.wolframalpha4j.http;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImageReaderStrategy implements ReaderStrategy<InputStream> {

    @Override
    public boolean canRead(@Nullable Class<?> type, @Nullable String contentType) {
        if (type == null || contentType == null) {
            return false;
        }
        return InputStream.class.isAssignableFrom(type) && contentType != null && contentType.startsWith("image/");
    }

    @Override
    public Mono<InputStream> read(Mono<ByteBuf> content, Class<InputStream> responseType) {
        return content.map(bb -> {
            byte[] bytes = new byte[bb.readableBytes()];
            bb.readBytes(bytes);
            return (InputStream) new ByteArrayInputStream(bytes);
        });
    }

}
