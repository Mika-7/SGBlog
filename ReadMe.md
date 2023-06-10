# 个人博客项目说明

该项目为前后端分离博客系统，主要负责后端接口设计与实现。

## 技术栈

SpringBoot,MybatisPlus,SpringSecurity,EasyExcel,Swagger2,JWT,Redis,Echarts,Vue,ElementUI....
## 运行

在目录 `资源/SQL` 下，有项目 MySQL 数据库所需的数据表的 sql 脚本文件，运行项目前请先在数据库里新建 sg_blog 数据库，然后运行所有的 sql 脚本文件创建表。

该项目有前台和后台两套系统，前台即负责对外访问浏览页面，后台是负责管理员等角色登录管理博客后台页面。

例如：

当需要访问前台页面，切换到 `资源\前端工程\sg-blog-vue`你需要使用下命令启动前台前端工程

```shell
npm install && npm run dev
```

同时，使用 IDEA，启动后端 SG-Blog 服务，即可访问博客博文相关信息。

当需要访问后台页面，切换到 `资源\前端工程\sg-admin-vue`你需要使用下命令启动前台前端工程

```shell
npm install && npm run dev
```

同时，使用 IDEA，启动后端 SG-Admin 服务，即可访问博客管理博文、用户、标签等相关信息。
