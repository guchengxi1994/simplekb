# simplekb

![image-20241001233919593](./images/image-20241001233919593.png)

**几个要点**:

1. mysql的全文索引（docker）
   > 配置完成 /etc/mysql/my.cnf之后，要修改权限
   >
   > chmod 644 /etc/mysql/my.cnf
   >
   > 否则会报 mysql: [Warning] World-writable config file '/etc/mysql/my.cnf' is ignored. 导致配置修改不生效 