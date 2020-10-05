<h2>接口授权配置</h2>

#### 一、新建token授权访问调用接口。

<img src="./images/swagger-colltctor-04.png" style="width:780px;" class="no-zoom" />

#### 二、Token Key

> token key，为获取token后，结果获取键。<br>
>列：`result.tails.access_token`
>``` json
{
    "code": "000",
    "msg": "成功",
    "result": {
        "id": "1",
        "loginName": "admin",
        "no": "001",
        "name": "系统管理员",
        "email": "admin@admin.com",
        "mobile": "123123",
        "userType": "1",
        "photo": "{\"select\":true,\"key\":4}",
        "tails": {
            "access_token": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMSIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2VydmljZSJdLCJleHAiOjE2MDE5NzMzODMsImF1dGhvcml0aWVzIjpbImFkbWluIl0sImp0aSI6ImZhYzJiYjFkLTdlMWItNDFkMS04ZDg5LTY1MDBlMGZjNDgyMiIsImNsaWVudF9pZCI6InVhYS1zZXJ2aWNlIn0.XAuRvihBhi_39J-yYAQ7neQUlutyKU5iyeO1K-Z5Pqg"
        }
    }
}
>```

#### 三、关联Swagger 配置授权模式。

 <img src="./images/swagger-colltctor-08.png" style="width:780px;" class="no-zoom" />

