# Itone
An android app about the resource platform of books  
这是一个分享大学生教材资源的平台，采用mvc架构。

项目主体有pdf阅读器、下载、消息通知、作业推送、图书资源浏览、用户信息管理、课程表等多个模块

## 项目结构

- define
 - 静态常量及枚举
- function
 - 逻辑功能处理部分，是mvc架构中c的部分
- service
 - android service，其中只有一个关于更新的service
- UIview
 - 包含了项目所有的activity和自定义的控件
- utils
 - 静态工具
 
其中pdf阅读器采用mupdf作为内核，网络通信、数据库管理及反向注入采用的是xutils3框架。由于该项目包含内容较多，难以一一详述，在此不做过多说明。
