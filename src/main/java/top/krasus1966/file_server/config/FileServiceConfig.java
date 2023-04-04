package top.krasus1966.file_server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.file_server.service.IFileChunkService;
import top.krasus1966.file_server.service.IFileQueryService;
import top.krasus1966.file_server.service.IFileStoreService;
import top.krasus1966.file_server.service.impl.MongoFileChunkServiceImpl;
import top.krasus1966.file_server.service.impl.MongoFileQueryServiceImpl;
import top.krasus1966.file_server.service.impl.MongoFileStoreServiceImpl;

/**
 * @author Krasus1966
 * @date 2023/4/4 23:51
 **/
@Configuration
public class FileServiceConfig {
    @Bean
    @ConditionalOnMissingBean(IFileStoreService.class)
    @ConditionalOnBean(GridFsTemplate.class)
    public IFileStoreService fileStoreService(GridFsTemplate gridFsTemplate) {
        return new MongoFileStoreServiceImpl(gridFsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(IFileQueryService.class)
    @ConditionalOnBean(GridFsTemplate.class)
    public IFileQueryService fileQueryService(GridFsTemplate gridFsTemplate) {
        return new MongoFileQueryServiceImpl(gridFsTemplate);
    }


    @Bean
    @ConditionalOnMissingBean(IFileChunkService.class)
    @ConditionalOnBean(GridFsTemplate.class)
    public IFileChunkService fileChunkService(GridFsTemplate gridFsTemplate) {
        return new MongoFileChunkServiceImpl(gridFsTemplate);
    }
}
