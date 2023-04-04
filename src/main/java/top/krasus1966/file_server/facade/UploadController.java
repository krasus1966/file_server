package top.krasus1966.file_server.facade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.file_server.entity.R;
import top.krasus1966.file_server.entity.dto.FileChunkDTO;
import top.krasus1966.file_server.entity.dto.FileChunkResultDTO;
import top.krasus1966.file_server.service.IFileService;

/**
 * @author Krasus1966
 * @date 2023/4/3 23:00
 **/
@RestController
public class UploadController {

    private final IFileService fileService;

    public UploadController(IFileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/chunkIsExists")
    public R<FileChunkResultDTO> chunkIsExists(FileChunkDTO fileChunkDTO) {
        FileChunkResultDTO fileChunkResultDTO = fileService.chunkIsExists(fileChunkDTO);
        return R.success(fileChunkResultDTO);
    }

    @PostMapping("/uploadChunk")
    public R<FileChunkResultDTO> uploadChunk(FileChunkDTO fileChunkDTO) {
        FileChunkResultDTO fileChunkResultDTO = fileService.uploadChunk(fileChunkDTO);
        return R.success(fileChunkResultDTO);
    }

    @PostMapping("/mergeChunk")
    public R<Boolean> mergeChunk(FileChunkDTO fileChunkDTO) {
        Boolean isMerge = fileService.mergeChunk(fileChunkDTO);
        return R.success(isMerge);
    }
}
