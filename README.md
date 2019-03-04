# anan-rabbitmq
demo for RabbitMQ + springboot

> CentOS RabbitMQ Server 

##  首先需要安装erlang
> 参考：http://fedoraproject.org/wiki/EPEL/FAQ#howtouse

```sh
rpm -Uvh http://download.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-8.noarch.rpm
yum install erlang
```
## 安装RabbitMQ
```sh
wget http://www.rabbitmq.com/releases/rabbitmq-server/v3.6.6/rabbitmq-server-3.6.6-1.el7.noarch.rpm

yum install rabbitmq-server-3.6.6-1.el7.noarch.rpm 

service rabbitmq-server start
service rabbitmq-server status
```

### 出现问题
```sh
Redirecting to /bin/systemctl status rabbitmq-server.service
● rabbitmq-server.service - RabbitMQ broker
   Loaded: loaded (/usr/lib/systemd/system/rabbitmq-server.service; disabled; vendor preset: disabled)
   Active: active (running) since Mon 2019-03-04 15:21:52 CST; 6s ago
 Main PID: 793 (beam.smp)
   Status: "Initialized"
   CGroup: /system.slice/rabbitmq-server.service
           ├─ 793 /usr/lib64/erlang/erts-5.10.4/bin/beam.smp -W w -A 64 -P 1048576 -t 5000000 -stbt db -zdbbl 32000 -K true -- -root /usr/lib64/erlang ...
           ├─ 929 /usr/lib64/erlang/erts-5.10.4/bin/epmd -daemon
           ├─1061 inet_gethost 4
           └─1062 inet_gethost 4

Mar 04 15:21:48 anan rabbitmq-server[793]: RabbitMQ 3.6.6. Copyright (C) 2007-2016 Pivotal Software, Inc.
Mar 04 15:21:48 anan rabbitmq-server[793]: ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
Mar 04 15:21:48 anan rabbitmq-server[793]: ##  ##
Mar 04 15:21:48 anan rabbitmq-server[793]: ##########  Logs: /var/log/rabbitmq/rabbit@anan.log
Mar 04 15:21:48 anan rabbitmq-server[793]: ######  ##        /var/log/rabbitmq/rabbit@anan-sasl.log
Mar 04 15:21:48 anan rabbitmq-server[793]: ##########
Mar 04 15:21:48 anan rabbitmq-server[793]: Starting broker...
Mar 04 15:21:52 anan rabbitmq-server[793]: systemd unit for activation check: "rabbitmq-server.service"
Mar 04 15:21:52 anan systemd[1]: Started RabbitMQ broker.
Mar 04 15:21:52 anan rabbitmq-server[793]: completed with 0 plugins.

```

打开 /var/log/rabbitmq/rabbit@anan.log
```sh
[root@anan rabbitmq]# cat rabbit\@anan.log

=INFO REPORT==== 4-Mar-2019::15:21:48 ===
Starting RabbitMQ 3.6.6 on Erlang R16B03-1
Copyright (C) 2007-2016 Pivotal Software, Inc.
Licensed under the MPL.  See http://www.rabbitmq.com/

=INFO REPORT==== 4-Mar-2019::15:21:48 ===
node           : rabbit@anan
home dir       : /var/lib/rabbitmq
config file(s) : /etc/rabbitmq/rabbitmq.config (not found)
cookie hash    : 9csyWc/ryZpumbtCpBTFYg==
log            : /var/log/rabbitmq/rabbit@anan.log
sasl log       : /var/log/rabbitmq/rabbit@anan-sasl.log
database dir   : /var/lib/rabbitmq/mnesia/rabbit@anan
```
#### 注意，这里出现**config file(s) : /etc/rabbitmq/rabbitmq.config (not found)**
> 这里显示的是没有找到配置文件，我们可以自己创建这个文件

```sh
cd /etc/rabbitmq/
vi rabbitmq.config
```
##### 编辑内容如下
```
[{rabbit, [{loopback_users, []}]}].
```
> 后面的“.”是要的，不是写错
这里的意思是开放使用，rabbitmq默认创建的用户guest，密码也是guest，这个用户默认只能是本机访问，localhost或者127.0.0.1，从外部访问需要添加上面的配置。

##### 保存配置后重启服务
```sh
service rabbitmq-server stop
service rabbitmq-server start
```

## 开放 15672 端口
> 这里虚拟机操作，没有机子没装防火墙，固做记录未亲测

```sh
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --reload 

# 直接关闭防火墙
service iptables stop
```

## RabbitMQ无法访问Web管理页面
> 启动RabbitMQ后，没法访问Web管理页面，RabbitMQ安装后默认是不启动管理模块的，所以需要配置将管理模块启动 

```sh
rabbitmqctl start_app
rabbitmq-plugins enable rabbitmq_management
rabbitmqctl stop
```

## 设置 RabbitMQ 远程 IP 登陆 (以下操作不需要重启)
> rabbitmq默认创建的用户guest，密码也是guest

1. 创建一个 anan 的账号
```sh
rabbitmqctl add_user anan 123456
```
1. 设置用户角色
```sh
rabbitmqctl set_user_tags anan  administrator
```
1. 设置用户权限
```sh
rabbitmqctl set_permissions -p "/" anan ".*" ".*" ".*"
```
1. 设置完成后可以查看当前用户和角色（需要开启服务）
```sh
rabbitmqctl list_users
```

> 访问： http://ip:15672

***
- [在CentOS7上安装RabbitMQ](https://www.cnblogs.com/uptothesky/p/6094357.html)
- [How can I install the packages from the EPEL software repository?](https://fedoraproject.org/wiki/EPEL/FAQ#howtouse)
- [RabbitMQ无法访问Web管理页面](https://blog.csdn.net/qq_35873847/article/details/78721696)
