package top.krasus1966.file_server.service.impl;


import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.file_server.entity.dto.FileChunkDTO;
import top.krasus1966.file_server.entity.dto.FileChunkResultDTO;
import top.krasus1966.file_server.service.IFileChunkService;

/**
 * @author Krasus1966
 * @date 2022/11/2 16:10
 **/
public class MongoFileChunkServiceImpl extends AbstractMongoFileServiceImpl implements IFileChunkService {

    public MongoFileChunkServiceImpl(GridFsTemplate gridFsTemplate) {
        super(gridFsTemplate);
    }

    @Override
    public FileChunkResultDTO chunkIsExists(FileChunkDTO fileChunkDTO) {
        return null;
    }

    @Override
    public FileChunkResultDTO uploadChunk(FileChunkDTO fileChunkDTO) {
        return null;
    }

    @Override
    public Boolean mergeChunk(FileChunkDTO fileChunkDTO) {
        return null;
    }
}
