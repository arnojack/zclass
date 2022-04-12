# zclass
[后端代码链接](https://github.com/arnojack/zclass_back)
#### 这个产品涉及到安卓手机数据库，listview，broadcast，java反射，activity传参，httpserve，websocket等方面
- 主要功能有
  1. 检索时间，定时提醒
  2. 填写课表，存入数据库
  3. 提交数据传入后端
  4. 多人多房间聊天室

#### 写产品的途中我积累了以下知识
- 写项目最好层次分明，将不同的class封装起来，如分为DAO层，Service层，Controller层
- url传参涉及到特殊字符或者汉字，要对url进行编码和解码
- 自定义对象实现Serializable接口后，可以转换为json类型传入网络
- 在调试过程中，遇到无法调试的代码段，可以写log，将数据打印到日志
- 如果涉及到serve，最好将此代码段写入生命周期的onstart里
