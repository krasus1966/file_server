### 文件分片上传
默认使用MongoDB中的GridFS存储文件，分片上传先上传到临时目录后合并，合并后存储到MongoDB再删除临时文件
#####
提供IFileService和三个相关联的接口，可通过实现所有接口切换存储中间件(未测试)
提供I18NUtils，可以实现国际化
#####
页面事例使用vue-simple-upload





