<template>
    <div class="hello">
        <uploader :options="options"
                  @file-success="onFileSuccess"
                  @file-progress="onFileProgress"
                  @file-error="onFileError"
                  class="uploader-example"
                  @files-added="onfiles"
                  @file-complete="oncomplete"
                  @complete="Complete"
                  >
            <uploader-unsupport></uploader-unsupport>
            <uploader-drop>
                <uploader-btn>上传文件</uploader-btn>
                <uploader-btn :directory="true">上传文件夹</uploader-btn>
            </uploader-drop>
            <uploader-list></uploader-list>
        </uploader>
    </div>
</template>

<script>

import axios from "axios";
import SparkMD5 from 'spark-md5'

export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
    return {
      options: {
        target: '/API-BACKEND/file/upload/chunk',
        chunkSize: 1024 * 1024 * 5,  //3MB
        fileParameterName: 'file', //上传文件时文件的参数名，默认file
        singleFile: false, // 启用单个文件上传。上传一个文件后，第二个文件将超过现有文件，第一个文件将被取消。
        query: function (file) {
          console.log(file)
          return {
            // "userId": Cookies.get('userId'),
            "fileType": file.getType(),
            "md5" : file.uniqueIdentifier,
          }
        },
        maxChunkRetries: 3,  //最大自动失败重试上传次数
        testChunks: true,     //是否开启服务器分片校验
        checkChunkUploadedByResponse: function (chunk, message) {
          let res = JSON.parse(message);
          if (res.data.skip) {
            this.skip = true;
            return true;
          }
          return (res.data.uploaded || []).indexOf(chunk.offset + 1) >= 0;
        },
        simultaneousUploads: 3, //并发上传数
      },
    };
  },
  methods: {
    // 文件上传
    onFileSuccess(rootFile, file, response, chunk) {
      let res = JSON.parse(response);
      console.log(res)
      if (res.successful && !this.skip) {
        axios
          .post(
            "/API-BACKEND/file/upload/mergeChunk",
            {
              md5: file.uniqueIdentifier,
              fileName: file.name,
              totalChunks: chunk.offset,
              contentType: file.contentType,
            },
            {

            }
          )
          .then((res) => {
            if (res.successful) {
              console.log("上传成功");
            } else {
              console.log(res);
            }
          })
          .catch(function (error) {
            console.log(error);
          });

      } else {
        console.log("上传成功，不需要合并");
      }
      if (this.skip) {
        this.skip = false;
      }
    },
    // 文件上传进度的回调
    onFileProgress(rootFile, file, chunk) {
      console.log(
        `上传中 ${file.name}，chunk：${chunk.startByte / 1024 / 1024} ~ ${
          chunk.endByte / 1024 / 1024
        }`
      );
      this.U_success = "1";
    },
    // 文件上传出错
    onFileError(rootFile, file, response) {
      console.log(rootFile);
      console.log(response);
    },
    //判断文件是否已经上传
    onfiles(files,fileList) {
      files.forEach((e) => {
        fileList.push(e);
        this.computeMD5(e);
      });
    },
    Complete() {
      console.log("上传完毕");
      this.U_success = "上传完毕";
    },
    //清空上传完成的文件列表
    oncomplete() {
      // this.$refs.upload.files = [];
      // this.$refs.upload.fileList = [];
      console.log("上传成功！");
      // this.$message.success("上传成功！");
    },
    computeMD5(file) {
      let fileReader = new FileReader();
      let time = new Date().getTime();
      let blobSlice =
        File.prototype.slice ||
        File.prototype.mozSlice ||
        File.prototype.webkitSlice;
      let currentChunk = 0;
      const chunkSize = 1024 * 1024;
      let chunks = Math.ceil(file.size / chunkSize);
      let spark = new SparkMD5.ArrayBuffer();
      // 文件状态设为"计算MD5"
      file.cmd5 = true; //文件状态为“计算md5...”
      file.pause();
      loadNext();
      fileReader.onload = (e) => {
        spark.append(e.target.result);
        if (currentChunk < chunks) {
          currentChunk++;
          loadNext();
          // 实时展示MD5的计算进度
          console.log(
            `第${currentChunk}分片解析完成, 开始第${
              currentChunk + 1
            } / ${chunks}分片解析`
          );
        } else {
          let md5 = spark.end();
          console.log(
            `MD5计算完毕：${file.name} \nMD5：${md5} \n分片：${chunks} 大小:${
              file.size
            } 用时：${new Date().getTime() - time} ms`
          );
          spark.destroy(); //释放缓存
          file.uniqueIdentifier = md5; //将文件md5赋值给文件唯一标识
          file.cmd5 = false; //取消计算md5状态
          file.resume(); //开始上传
        }
      };
      fileReader.onerror = function () {
        this.error(`文件${file.name}读取出错，请检查该文件`);
        file.cancel();
      };
      function loadNext() {
        let start = currentChunk * chunkSize;
        let end =
          start + chunkSize >= file.size ? file.size : start + chunkSize;
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end));
      }
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
    margin: 40px 0 0;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    display: inline-block;
    margin: 0 10px;
}

a {
    color: #42b983;
}
</style>
