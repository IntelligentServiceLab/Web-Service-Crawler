## Web爬虫系统分为三个子系统
- **crawlerData**：用Python代码写的爬虫程序，爬取的网站地址为programmableweb
- **srb-abmin**：Vue框架搭建的前端项目
- **crawler-system**：SpringBoot框架搭建的后端项目，前后端项目需要配合一起使用才能够达到展示数据的效果

## 版本配置
| Package | Version | 
| :-----| :----- | 
| python | 3.10.2 | 
|beautifulsoup4| 4.10.0|
|configparser| 5.2.0|
|files| 1.1.1|
|openpyxl| 3.0.9|
|pandas| 1.4.2|
|properties| 0.6.1|
|pyinstaller-hooks-contrib|2022.2|
|python-dateutil|2.8.2|
|requests|2.27.1|
|urllib3|1.26.8|

## 系统使用说明
### 子系统crawlerData使用说明
- 需要的环境：IDEA+MySQL+Redis
- Step 1：使用Pyinstaller把程序打包成了.exe可执行文件
- Step 2：点击crawlerData/dist/main/main.exe运行程序，得到的数据保存在crawlerData/dist/main/data目录下
- ![image](https://user-images.githubusercontent.com/68261447/171332747-37362856-4590-407a-8642-13074d322d9e.png)


### 子系统crawler-system使用说明
  ##需要安装的软件：IDEA+MySQL+Redis+SQLyog（这个使用Navicat也行，这只是一个数据库的可视化工具）
  ##数据库文件也在工具目录tool之内，自己导入这个文件.sql后缀的文件即可
  ##application.yml配置说明
  ![image](https://user-images.githubusercontent.com/68261447/171333984-d9537e77-eec2-46ad-bc13-85969324e45f.png)
![image](https://user-images.githubusercontent.com/68261447/171334158-6482d95c-ddd6-428d-ab55-989db6b69888.png)
- Step 1：打开Redis
  在Redis的安装目录下，有一个redis-server.exe的文件，点击就可以打开Redis
![image](https://user-images.githubusercontent.com/68261447/171334268-0b7e05d6-01a8-49c4-b6d8-2056634f4faf.png)
- Step 2：在IDEA中打开crawler-system项目，然后点击运行
![image](https://user-images.githubusercontent.com/68261447/171334196-66f8a8a3-d9d7-4995-8846-2ea8facd5e96.png)
- Step 3：通过端口号即可访问数据
  示例：
  

### 子系统3使用说明
- Step 1：
- Step 2：
- Step 3：
