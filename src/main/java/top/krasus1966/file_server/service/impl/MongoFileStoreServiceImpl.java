package top.krasus1966.file_server.service.impl;


import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.file_server.entity.dto.FileChunkDTO;
import top.krasus1966.file_server.entity.dto.FileInfoDTO;
import top.krasus1966.file_server.factory.FileChunkFactory;
import top.krasus1966.file_server.service.IFileStoreService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Krasus1966
 * @date 2022/11/2 16:10
 **/
public class MongoFileStoreServiceImpl extends AbstractMongoFileServiceImpl implements IFileStoreService {


    public MongoFileStoreServiceImpl(GridFsTemplate gridFsTemplate) {
        super(gridFsTemplate);
    }

    @Override
    public List<FileInfoDTO> store(List<FileChunkDTO> files) throws IOException {
        List<FileInfoDTO> idList = new ArrayList<>();
        for (FileChunkDTO file : files) {
            // 文件存在跳过上传
            FileInfoDTO fileInfoDTO = checkFileExists(file);
            if (null != fileInfoDTO) {
                idList.add(fileInfoDTO);
                continue;
            }
            // 存储到GridFS
            ObjectId objectId =
                    gridFsTemplate.store(new ByteArrayInputStream(file.getFile().getBytes()),
                            file.getFileName(),
                            file.getContentType());
            file.setFileId(objectId.toHexString());
            idList.add(FileChunkFactory.getInstance().toFileInfo(file));
        }
        return idList;
    }

    @Override
    public FileInfoDTO checkFileExists(FileChunkDTO file) throws IOException {
        // 通过摘要信息查询文件是否存在，存在则不再继续存储
        String md5 =
                Arrays.toString(new Binary(BsonBinarySubType.MD5, file.getFile().getBytes()).getData());
        Query query = Query.query(Criteria.where("md5").is(md5));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (null != gridFSFile) {
            return gridFs2FileInfoDTO(false, gridFSFile);
        }
        return null;
    }

    @Override
    public void delete(String ids) {
        Query query = new Query();
        if (null != ids && !"".equals(ids.trim())) {
            String[] fileIds = ids.split(",");
            query.addCriteria(Criteria.where("id").in(fileIds));
            gridFsTemplate.delete(query);
        }
    }
}
