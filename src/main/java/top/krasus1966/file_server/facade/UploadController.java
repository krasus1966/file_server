package top.krasus1966.file_server.facade;

import org.springframework.web.bind.annotation.*;
import top.krasus1966.file_server.entity.R;
import top.krasus1966.file_server.entity.dto.FileChunkDTO;
import top.krasus1966.file_server.entity.dto.FileChunkResultDTO;
import top.krasus1966.file_server.entity.dto.FileInfoDTO;
import top.krasus1966.file_server.entity.dto.PageResultDTO;
import top.krasus1966.file_server.service.IFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/3 23:00
 **/
@RestController
@RequestMapping("/file/upload")
public class UploadController extends BaseController {

    private final IFileService fileService;

    public UploadController(IFileService fileService, HttpServletRequest request,
                            HttpServletResponse response) {
        super(request, response);
        this.fileService = fileService;
    }

    @PostMapping("/store")
    public R<List<FileInfoDTO>> store(List<FileChunkDTO> fileChunkDTO) throws IOException {
        return R.success(fileService.store(fileChunkDTO));
    }

    @PostMapping("/delete")
    public R<List<FileInfoDTO>> delete(String ids) throws IOException {
        fileService.delete(ids);
        return R.success();
    }

    @GetMapping("/chunk")
    public R<FileChunkResultDTO> chunkIsExists(FileChunkDTO fileChunkDTO) throws IOException {
        FileChunkResultDTO fileChunkResultDTO = fileService.chunkIsExists(fileChunkDTO);
        return R.success(fileChunkResultDTO);
    }

    @PostMapping("/chunk")
    public R<FileChunkResultDTO> uploadChunk(FileChunkDTO fileChunkDTO) {
        fileService.uploadChunk(fileChunkDTO);
        return R.success();
    }

    @PostMapping("/mergeChunk")
    public R<FileChunkDTO> mergeChunk(@RequestBody FileChunkDTO fileChunkDTO) {
        Boolean isMerge = fileService.mergeChunk(fileChunkDTO);
        if (isMerge) {
            return R.success(fileChunkDTO);
        }
        return R.failed();
    }
}
