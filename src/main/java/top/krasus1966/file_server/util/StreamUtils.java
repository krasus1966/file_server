package top.krasus1966.file_server.util;


import top.krasus1966.file_server.entity.dto.FileInfoDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @author Krasus1966
 * {@code @date} 2021/6/25 22:30
 **/
public class StreamUtils {

    private StreamUtils() {
    }

    public static void download(FileInfoDTO fileInfo, HttpServletRequest req, HttpServletResponse resp, int bufferSize) throws IOException {
        resp.setContentType(fileInfo.getContentType());
        resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(Objects.requireNonNull(fileInfo.getFileName()), "UTF-8"));
        long skip = -1;
        long length = -1;
        long end = fileInfo.getFileLength() - 1;
        // 断点续传
        String range = (req.getHeader("Range"));
        if (range != null && range.length() > 0) {
            int idx = range.indexOf("-");
            skip = Long.parseLong(range.substring(6, idx));
            if ((idx + 1) < range.length()) {
                end = Long.parseLong(range.substring(idx + 1));
            }
            length = end - skip + 1;
        }
        if (range == null || range.length() <= 0) {
            resp.setHeader("Content-Length", "" + fileInfo.getFileLength());
            resp.setStatus(200);
        } else {
            resp.setHeader("Content-Length", "" + length);
            resp.setHeader("Content-Range", "bytes " + skip + "-" + end + "/" + fileInfo.getFileLength());
            resp.setStatus(206);
        }
        resp.flushBuffer();
        download(fileInfo.getInputStream(), resp.getOutputStream(), bufferSize, skip, length);
    }

    public static void download(InputStream in, OutputStream out, int bufferSize, long skip, long length) throws IOException {
        int len;
        byte[] bs = new byte[bufferSize];
        if (skip > 0) {
            in.skip(skip);
        }
        while ((len = in.read(bs)) != -1) {
            if (length > 0) {
                if (length > len) {
                    out.write(bs, 0, len);
                    length -= len;
                } else {
                    out.write(bs, 0, (int) length);
                }
            } else {
                out.write(bs, 0, len);
            }
        }
    }
}
