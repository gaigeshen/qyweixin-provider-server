### 企业微信服务商服务器

#### 先执行数据库脚本
- resources/scripts/tables.sql

#### 然后添加启动配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/database
    username: root
    password: 123456
  qyweixin:
    server-host: https://qyapi.weixin.qq.com
    corp-id: your corp id
    provider-secret: your provider secret
    suite-id: your suite id
    suite-secret: your suite secret
    token: your token
    aes-key: your aes key
```

#### 查看运行日志
```shell
tail -f -n 100 /tmp/qyweixin-provider-server/qyweixin-provider-server.log
```