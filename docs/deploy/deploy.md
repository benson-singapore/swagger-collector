<h2>项目运行</h2>

> 本地项目运行，只需要下载源码，然后启动即可访问。

#### 一、后端项目。

##### 1. 安装数据mysql脚本。

##### 2. 下载源码，修改mysql数据访问地址。

##### 3. 修改配置文件，设置登录名与密码。

``` yaml
## basic-common config
basic-common:
  # 登录名与密码配置
  api:
    loginName: swagger
    password: 123456
```

4. 启动项目。（ CollectApiApplication -> main 方法）


#### 二、前端项目。

##### 1. 前端项目是antd react版本的。需要node环境，如果没有请自行安装。（推荐 yarn模式安装，npm 会比较慢）

##### 2. 安装 node_modules。

``` shell
yarn
```
> NPM安装方法： npm install

##### 3. 配置前端代理 ( config -> proxy.ts)。

``` javascript
export default {
  dev: {
    '/swagger/': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      pathRewrite: { '/swagger': '' },
    },
  },
};
```
> 此处只更改 `http://localhost:8080`地址与端口即可。其他配置为默认配置，不要私自更改。

##### 4. 启动项目

``` shell
yarn start
```

> `区分环境`对于很多项目来说都需要一个区分环境的变量，Pro 中内置 dev，test, pre 三个环境。可以分别通过运行 `npm run start:test` 等命令启动。
