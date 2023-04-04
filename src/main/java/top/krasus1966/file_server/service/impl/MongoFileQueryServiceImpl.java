package top.krasus1966.file_server.service.impl;


import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.file_server.entity.dto.FileChunkDTO;
import top.krasus1966.file_server.entity.dto.FileInfoDTO;
import top.krasus1966.file_server.entity.dto.PageResultDTO;
import top.krasus1966.file_server.service.IFileQueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2022/11/2 16:10
 **/
public class MongoFileQueryServiceImpl extends AbstractMongoFileServiceImpl implements IFileQueryService {
    public MongoFileQueryServiceImpl(GridFsTemplate gridFsTemplate) {
        super(gridFsTemplate);
    }


    @Override
    public List<FileInfoDTO> query(FileChunkDTO fileInfo) throws IOException {
        return super.queryFileInfo(fileInfo, false);
    }

    @Override
    public List<FileInfoDTO> findInputStream(FileChunkDTO fileInfo) throws IOException {
        return super.queryFileInfo(fileInfo, true);
    }

    @Override
    public PageResultDTO<FileInfoDTO> queryPage(FileChunkDTO fileInfo, long page, long pageSize) {
        long skip = (page - 1) * pageSize;
        Query query =
                Query.query(new Criteria()).with(Sort.by(Sort.Direction.DESC, "crtTime")).skip(skip).limit((int) pageSize);
        Query countQuery = new Query();
        if (null != fileInfo.getFileName() && !"".equals(fileInfo.getFileName().trim())) {
            query.addCriteria(Criteria.where("fileName").alike(Example.of(fileInfo.getFileName())));
            countQuery.addCriteria(Criteria.where("fileName").alike(Example.of(fileInfo.getFileName())));
        }

        MongoCursor<GridFSFile> iterator = gridFsTemplate.find(countQuery).iterator();
        long count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        GridFSFindIterable gridFSFiles = gridFsTemplate.find(query);
        List<FileInfoDTO> fileList = new ArrayList<FileInfoDTO>();
        for (GridFSFile gridFSFile : gridFSFiles) {
            try {
                fileList.add(gridFs2FileInfoDTO(false, gridFSFile));
            } catch (IOException e) {

            }
        }
        return new PageResultDTO<>(fileList, count);
    }

    @Override
    public FileInfoDTO getOneById(String id) {
        return super.getOneById(id);
    }
}
