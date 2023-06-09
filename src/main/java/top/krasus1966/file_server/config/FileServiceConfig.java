package top.krasus1966.file_server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.file_server.service.*;
import top.krasus1966.file_server.service.mongo.MongoFileChunkServiceImpl;
import top.krasus1966.file_server.service.mongo.MongoFileQueryServiceImpl;
import top.krasus1966.file_server.service.mongo.MongoFileStoreServiceImpl;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/4 23:51
 **/
@Configuration
public class FileServiceConfig {
    @Bean
    @ConditionalOnMissingBean(IFileStoreService.class)
    public IFileStoreService fileStoreService(GridFsTemplate gridFsTemplate) {
        return new MongoFileStoreServiceImpl(gridFsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(IFileQueryService.class)
    public IFileQueryService fileQueryService(GridFsTemplate gridFsTemplate) {
        return new MongoFileQueryServiceImpl(gridFsTemplate);
    }


    @Bean
    @ConditionalOnMissingBean(IFileChunkService.class)
    public IFileChunkService fileChunkService(GridFsTemplate gridFsTemplate) {
        return new MongoFileChunkServiceImpl(gridFsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(IFileService.class)
    @DependsOn({"fileStoreService","fileQueryService","fileChunkService"})
    public IFileService fileService(IFileStoreService fileStoreService,
                                    IFileQueryService fileQueryService,
                                    IFileChunkService fileChunkService) {
        return new FileServiceImpl(fileStoreService, fileQueryService, fileChunkService);
    }
}
