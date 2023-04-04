package top.krasus1966.file_server.facade;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.krasus1966.base_project.common.core.anno.auth.Auth;
import top.krasus1966.base_project.common.core.exception.BizException;
import top.krasus1966.base_project.file.entity.FileInfo;
import top.krasus1966.base_project.file.service.IFileService;
import top.krasus1966.base_project.file.util.StreamUtils;
import top.krasus1966.file_server.entity.dto.FileInfoDTO;
import top.krasus1966.file_server.service.IFileService;
import top.krasus1966.file_server.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Krasus1966
 * @date 2022/11/2 20:18
 **/
@RestController
@Slf4j
@RequestMapping("/file/download")
public class DownloadController {


    private final IFileService fileService;

    public DownloadController(IFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 通用文件下载
     *
     * @param fileId 文件id
     * @return void
     * @method download
     * @author krasus1966
     * @date 2022/1/19 09:53
     * @description 通用文件下载
     */
    @GetMapping("/{fileId}")
    public void download(@PathVariable("fileId") String fileId) {
        if (CharSequenceUtil.isBlank(fileId)){
            throw new BizException("文件id不能为空");
        }
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        HttpServletResponse resp = requestAttributes.getResponse();
        HttpServletRequest req = requestAttributes.getRequest();
        try {
            FileInfoDTO fileInfo = fileService.getOneById(fileId);
            if (null == fileInfo) {
                return;
            }
            StreamUtils.download(fileInfo, req, resp, 1024);
        } catch (Exception e) {
            if (!(e instanceof IOException)) {
                log.error("/file/download/download ERROR", e);
            }
        }
    }

    /**
     * 下载压缩包
     *
     * @param fileIds  文件id
     * @param fileName 文件名
     * @return void
     * @method downloadZip
     * @author krasus1966
     * @date 2022/1/19 09:54
     * @description 下载压缩包
     */
    @GetMapping("/downloadZip")
    public void downloadZip(String fileIds, String fileName) throws IOException {
        if (CharSequenceUtil.isBlank(fileIds)){
            throw new BizException("文件id不能为空");
        }
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        HttpServletResponse resp = requestAttributes.getResponse();
        List<FileInfoDTO> fileDocuments =
                fileService.query(FileInfo.builder().id(fileIds).build());
        if (fileDocuments.isEmpty()) {
            throw new BizException("文件不存在");
        }
        fileDocuments = fileService.findInputStream(fileDocuments);
        StringJoiner paths = new StringJoiner(",");
        List<InputStream> list = new ArrayList<>();
        for (FileInfoDTO file : fileDocuments) {
            if (file.getInputStream() != null){
                paths.add(file.getFileName());
                list.add(file.getInputStream());
            }
        }
        if (list.isEmpty()) {
            throw new BizException("文件不存在");
        }
        InputStream[] inputStreams = new InputStream[list.size()];
        File zipFile = ZipUtil.zip(File.createTempFile(UUID.randomUUID().toString(), ".tmp"),
                paths.toString().split(","), list.toArray(inputStreams));
        resp.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(StrUtil.isBlankIfStr(fileName) ?
                        System.currentTimeMillis() + ".zip" : fileName + ".zip", "UTF-8"));
        resp.setContentType("application/x-zip-compressed");
        try (InputStream is = Files.newInputStream(zipFile.toPath())) {
            StreamUtils.download(is, resp.getOutputStream(), 1024, -1, -1);
        } catch (Exception e) {
            if (!(e instanceof IOException)) {
                log.error("/file/download/downloadZip ERROR", e);
            }
        } finally {
            // 判断文件是否存在并执行删除，如果删除失败，加入到虚拟机结束时再删除
            if (zipFile.exists() && !zipFile.delete()) {
                zipFile.deleteOnExit();
            }
        }
    }
}
