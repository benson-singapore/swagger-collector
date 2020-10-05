<h2>服务器部署</h2>

#### 一、springboot 项目部署此处省略。
> 可选择tomcat部署，或者 jar 方式部署都可以，自行打包部署。

#### 二、前端代码部署。
> 前端代码部署，需要配置反向代理，此处使用nginx作为代理服务。

##### 1. nginx 安装此处省略。

##### 2. 前端代码编译。

``` shell
yarn build
```

> 把前端代码编译为静态文件，执行完成后，会生成`dist`目录文件。

##### 3. 把`dist`上传至服务器。

##### 4. 修改nginx 配置文件。

``` shell
server {
    listen 8000 default_server;
    listen [::]:8000 default_server;
    server_name _;
    root /usr/share/nginx/html/swagger/dist;

    # Load configuration files for the default server block.
    include /etc/nginx/default.d/*.conf;

    location ~ ^/(assets|images|javascripts|stylesheets|swfs|system)/ {
        gzip_static on;
        expires max;
    }

    location / {
        try_files $uri /index.html;
    }

    # Avoid CORS and reverse proxy settings
    location /swagger/ {
        proxy_http_version 1.1;
        #proxy_set_header Cookie $http_cookie;
        #proxy_set_header Cookie '31A91AAB5578F320D2505F4DCA7489D2';
        proxy_pass http://127.0.0.1:8080; # [3]

    }
}
```

> `/usr/share/nginx/html/swagger/dist` 此路径为`dist`服务器地址。<br>
> `http://127.0.0.1:8080` 此地址为，后端项目地址
