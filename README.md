# zclass
[后端代码链接](https://github.com/arnojack/zclass_back)
#### 这个产品涉及到安卓手机数据库，listview，broadcast，java反射，activity传参，httpserve，websocket等方面
- 主要功能有
  1. 检索时间，定时提醒
  2. 填写课表，存入数据库
  3. 提交数据传入后端
  4. 多人多房间聊天室
  5. 拍照，或从相册选择头像
  6. 图片上传，下载

#### 写产品的途中我积累了以下知识
- 写项目最好层次分明，将不同的class封装起来，如分为DAO层，Service层，Controller层
- url传参涉及到特殊字符或者汉字，要对url进行编码和解码
- 自定义对象实现Serializable接口后，可以转换为json类型传入网络
- 在调试过程中，遇到无法调试的代码段，可以写log，将数据打印到日志
- 如果涉及到serve，最好将此代码段写入生命周期的onstart里
- 写完dialog后，要在ondestory里加close方法
- hashmap在发生哈希冲突时无法直接取出value
#### 写产品的途中我遇到了以下问题
- url传参无法识别
- websocket无法重定向  //经过多次使用log查看代码流程，我决定取消重定向，重写onOpen方法，发送信息给服务器，在服务器端切换房间
