package com.selfdemo;

import com.selfdemo.netty.AppClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Date:2023/8/7
 * Author:ljs
 * Description:
 */

public class NettyTest {

    @Test
    public void testByteBuf() {
        ByteBuf head = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();

        //通过逻辑组装而不是物理拷贝, 实现在JVM中的零拷贝
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        byteBuf.addComponents(head,body);
    }

    @Test
    public void testWrapper() {
        byte[] buf1 = new byte[1024];
        byte[] buf2 = new byte[1024];
        // 共享byte数组的内容而不是拷贝,这也算零拷贝
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf1, buf2);
    }

    @Test
    public void testSlice() {
        byte[] buf1 = new byte[1024];
        byte[] buf2 = new byte[1024];
        // 共享byte数组的内容而不是拷贝,这也算零拷贝
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf1, buf2);

        //同样可以将一个byteBuf,分割成多个,这也算零拷贝
        ByteBuf slice = byteBuf.slice(1, 5);
        ByteBuf slice2 = byteBuf.slice(2, 5);
    }

    @Test
    public void testMessage() throws IOException {
        ByteBuf message = Unpooled.buffer();
        message.writeBytes("ljs".getBytes(StandardCharsets.UTF_8));
        message.writeByte(1);
        message.writeShort(125);
        message.writeInt(256);
        message.writeByte(1);
        message.writeByte(0);
        message.writeByte(2);
        message.writeLong(2531212L);
        //用对象流转换为字节数组
        AppClient appClient = new AppClient();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(appClient);
        byte[] buffer = outputStream.toByteArray();
        message.writeBytes(buffer);

        System.out.println(message);
    }

    @Test
    public void testCompress() throws IOException {
        byte[] buf = new byte[]{12,12,12,12,12,33,23,25,13};

        //本质就是,将buf作为输入,将结果输出到另一个字节数组中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos);

        gzipOutputStream.write(buf);
        gzipOutputStream.finish();

        byte[] bytes = baos.toByteArray();
        System.out.println(Arrays.toString(bytes));

    }

    @Test
    public void testDeCompress() throws IOException {
        byte[] buf = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, -1, -29, -31, 1, 2, 69, 113, 73, 94, 0, -16, 29, 86, -58, 9, 0, 0, 0};

        //本质就是,将buf作为输入,将结果输出到另一个字节数组中
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        GZIPInputStream gzipInputStream = new GZIPInputStream(bais);

        byte[] bytes = gzipInputStream.readAllBytes();
        System.out.println(buf.length+"->"+bytes.length);
        System.out.println(Arrays.toString(bytes));

    }
}
