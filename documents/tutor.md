---
title: tutor
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.30"

---

# tutor

Base URLs:

# Authentication

# 学生/家长

## POST 学生/家长注册接口

POST /api/student/register

输入username和password完成学生/家长端的用户注册

> Body 请求参数

```json
{
  "username": "test",
  "password": "123456"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» username|body|string| 是 | 用户名|none|
|» password|body|string| 是 | 密码|none|

> 返回示例

```json
{
  "code": 1,
  "msg": "注册成功",
  "data": null,
  "map": {}
}
```

```json
{
  "code": 0,
  "msg": "该用户名已被注册",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 学生/家长登录接口

POST /api/student/login

学生/家长通过输入账号、密码，进行登录验证，后端返回token

> Body 请求参数

```json
{
  "username": "string",
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» username|body|string| 是 | 用户名|none|
|» password|body|string| 是 | 密码|none|

> 返回示例

```json
{
  "code": 1,
  "msg": null,
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsicGFzc3dvcmQiOiIxMjM0NTYiLCJzdHVJZCI6MSwidXNlcm5hbWUiOiJ0ZXN0In0sImV4cCI6MTc0Nzk1NjEyOX0.N-g3datplpzGP7sXo_ocwh8vkud1Dw1qWVPoCcUPYTU",
  "map": {}
}
```

```json
{
  "code": 0,
  "msg": "密码错误，请重新尝试",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|any|true|none|数据|返回token|

*oneOf*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|string|false|none||none|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|integer|false|none||none|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|boolean|false|none||none|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|array|false|none||none|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|object|false|none||none|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»» *anonymous*|number|false|none||none|

*continued*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 发布招聘信息接口

POST /api/recruit/publishRecruit

上传预期定价、学校层次、针对学生水平、科目、线上/线下形式、详细描述的信息，帮助学生/家长找到具体的家教

> Body 请求参数

```json
{
  "price": 0,
  "schLevel": "string",
  "stuLevel": "string",
  "subject": "string",
  "online": true,
  "detail": "string",
  "time_num": "string",
  "days": [
    "string"
  ],
  "start_times": [
    "string"
  ],
  "end_times": [
    "string"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» price|body|integer| 是 | 定价|家教的设定加个，多少元/小时|
|» schLevel|body|string| 是 | 学校层次|想要找到家教的层次，可以为jbw、eyy、syl、yb、eb，分别对应酒吧舞、二幺幺、双一流、一本、二本|
|» stuLevel|body|string| 是 | 学生水平|ABCDE，五档水平|
|» subject|body|string| 是 | 科目|想要补课的科目|
|» online|body|boolean| 是 | 线上|线上或者线下的形式，线上是true，线下是false|
|» detail|body|string| 是 | 描述细节|300字以内的描述细节|
|» time_num|body|string| 是 | 招聘信息中设置的时间数量|有几个时间这个字段就设置成多少|
|» days|body|[string]| 是 | 星期几|记录星期几|
|» start_times|body|[string]| 是 | 开始时间|记录时间|
|» end_times|body|[string]| 是 | 结束时间|none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "成功发布招聘信息",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 获取家教老师信息列表接口

GET /api/recommend/getAllRecommends

获取全部家教老师的应聘信息

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|pageNum|query|integer| 否 ||none|
|pageSize|query|integer| 否 ||none|
|subject|query|string| 否 ||none|
|online|query|string| 否 ||none|
|Authorization|header|string| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": null,
  "data": [
    {
      "recommend": {
        "recommendId": 3,
        "price": 200,
        "subject": "英语",
        "online": false,
        "detail": "sit sunt Ut ea Excepteur",
        "tchId": 1
      },
      "recommendDates": [
        {
          "dateId": 2,
          "day": "mon",
          "startTime": "11:42:58",
          "endTime": "22:00:15",
          "recommendId": 3
        },
        {
          "dateId": 3,
          "day": "tue",
          "startTime": "13:55:36",
          "endTime": "21:00:00",
          "recommendId": 3
        },
        {
          "dateId": 4,
          "day": "fri",
          "startTime": "14:00:00",
          "endTime": "23:00:00",
          "recommendId": 3
        }
      ]
    }
  ],
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取自己发布的招聘信息列表

GET /api/recruit/getMyRecruits

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|pageNum|query|integer| 否 ||none|
|pageSize|query|integer| 否 ||none|
|subject|query|string| 否 ||none|
|online|query|string| 否 ||none|
|Authorization|header|string| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": null,
  "data": [
    {
      "recruit": {
        "recruitId": 1,
        "price": 196,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "物理",
        "online": false,
        "detail": "adipisicing officia fugiat sit",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 3,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 4,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 5,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 6,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 7,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 8,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 9,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 11,
        "price": 242,
        "schLevel": "eyy",
        "stuLevel": "C",
        "subject": "计算机",
        "online": false,
        "detail": "et",
        "stuId": 1
      },
      "recruitDates": [
        {
          "dateId": 5,
          "day": "mon",
          "startTime": "18:00:00",
          "endTime": "20:00:00",
          "recruitId": 11
        },
        {
          "dateId": 6,
          "day": "mon",
          "startTime": "15:00:00",
          "endTime": "17:00:00",
          "recruitId": 11
        }
      ]
    }
  ],
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 修改招聘信息

POST /api/recruit/updateRecruit

提交招聘信息需要的全部内容，后端更新数据库中的存储

> Body 请求参数

```json
{
  "type": "object",
  "properties": {
    "price": {
      "type": "integer",
      "title": "定价",
      "description": "家教的设定加个，多少元/小时"
    },
    "sch_level": {
      "type": "string",
      "title": "学校层次",
      "description": "想要找到家教的层次，可以为jbw、eyy、syl、yb、eb，分别对应酒吧舞、二幺幺、双一流、一本、二本"
    },
    "stu_level": {
      "type": "string",
      "title": "学生水平",
      "description": "ABCDE，五档水平"
    },
    "subject": {
      "type": "string",
      "title": "科目",
      "description": "想要补课的科目"
    },
    "online": {
      "type": "boolean",
      "title": "线上",
      "description": "线上或者线下的形式，线上是true，线下是false"
    },
    "detail": {
      "type": "string",
      "title": "描述细节",
      "description": "300字以内的描述细节"
    },
    "days": {
      "type": "array",
      "items": {
        "type": "string"
      },
      "title": "星期几",
      "description": "记录星期几"
    },
    "time_num": {
      "type": "string",
      "title": "招聘信息中设置的时间数量",
      "description": "有几个时间这个字段就设置成多少"
    },
    "start_times": {
      "type": "array",
      "items": {
        "type": "string"
      },
      "title": "开始时间",
      "description": "记录时间"
    },
    "end_times": {
      "type": "array",
      "items": {
        "type": "string"
      },
      "title": "结束时间"
    }
  },
  "required": [
    "price",
    "sch_level",
    "stu_level",
    "subject",
    "online",
    "detail",
    "time_num",
    "start_times",
    "days",
    "end_times"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» recruit_id|body|string| 是 | 招聘信息编号|none|
|» price|body|integer| 是 | 定价|家教的设定加个，多少元/小时|
|» sch_level|body|string| 是 | 学校层次|想要找到家教的层次，可以为jbw、eyy、syl、yb、eb，分别对应酒吧舞、二幺幺、双一流、一本、二本|
|» stu_level|body|string| 是 | 学生水平|ABCDE，五档水平|
|» subject|body|string| 是 | 科目|想要补课的科目|
|» online|body|boolean| 是 | 线上|线上或者线下的形式，线上是true，线下是false|
|» detail|body|string| 是 | 描述细节|300字以内的描述细节|
|» time_num|body|string| 是 | 招聘信息中设置的时间数量|有几个时间这个字段就设置成多少|
|» days|body|[string]| 是 | 星期几|记录星期几|
|» start_times|body|[string]| 是 | 开始时间|记录时间|
|» end_times|body|[string]| 是 | 结束时间|none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "更新成功",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 删除招聘信息

POST /api/recruit/deleteRecruit

给定recruit_id删除对应的招聘信息

> Body 请求参数

```json
{
  "recruit_id": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» recruit_id|body|integer| 是 | 招聘信息编号|none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "删除成功",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 上传学生个人信息

POST /api/stuInfo/uploadMsg

上传学生的性别、年级、家庭住址、电话号码、头像，自动将信息存入到数据库中。
- 如果先前没有数据存入，那么将数据加入到数据库中
- 如果先前已经有数据存入了，那么更新数据

> Body 请求参数

```json
{
  "gender": "string",
  "grade": 0,
  "addr": "string",
  "phoneNum": "string",
  "avatar": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» gender|body|string| 是 | 性别|M或者F，代表男性和女性|
|» grade|body|integer| 是 | 年级|整型数字|
|» addr|body|string| 是 | 地址|家庭住址|
|» phoneNum|body|string| 是 | 电话号码|none|
|» avatar|body|string| 是 | 头像|这里应该传的是一个阿里云OSS的链接|

> 返回示例

```json
{
  "code": 1,
  "msg": "上传成功",
  "data": null,
  "map": {}
}
```

```json
{
  "code": 1,
  "msg": "更新成功！",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 获取学生/家长的个人信息

GET /api/stuInfo/getStuMsg

get方法，提供token，自动查询对应在stuInfo表中的学生个人信息
- 如果之前录入过信息，会自动把数据库里存储的信息返回回来
- 如果之前没有录入过信息，那么直接返回null

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "code": 1,
  "msg": null,
  "data": null,
  "map": {}
}
```

```json
{
  "code": 1,
  "msg": null,
  "data": {
    "infoId": 1,
    "gender": "F",
    "grade": 12,
    "addr": "西大直街92号",
    "phoneNum": "60417615867",
    "avatar": "https://avatars.githubusercontent.com/u/50435606",
    "stuId": 1
  },
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 修改学生/家长个人头像

POST /api/stuInfo/updateAvatar

上传头像的url，修改头像

> Body 请求参数

```json
{
  "avatar": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» avatar|body|string| 是 | 图像链接|图像的oss链接|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "更新成功",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 依据教师编号获取教师个人信息

GET /api/tchInfo/getTchMsgById

给定编号获取信息

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|tchId|query|integer| 否 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 大模型推荐家教老师

POST /api/teacher/llmRecommendTeacher

> Body 请求参数

```json
{
  "prompt": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» prompt|body|string| 是 | 提示词|一段提示词，用来描述学生的个人情况|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 家教老师

## POST 家教老师注册接口

POST /api/teacher/register

家教老师输入用户名和密码完成注册

> Body 请求参数

```json
{
  "username": "string",
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» username|body|string| 是 | 用户名|none|
|» password|body|string| 是 | 密码|none|

> 返回示例

```json
{
  "code": 1,
  "msg": "注册成功",
  "data": null,
  "map": {}
}
```

```json
{
  "code": 0,
  "msg": "该用户名已被注册",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 家教老师登录接口

POST /api/teacher/login

输入用户名和密码，完成登录，data字段返回token

> Body 请求参数

```json
{
  "username": "string",
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» username|body|string| 是 | 用户名|none|
|» password|body|string| 是 | 密码|none|

> 返回示例

```json
{
  "code": 1,
  "msg": null,
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsicGFzc3dvcmQiOiIxMjM0NTYiLCJ0eXBlIjoidGVhY2hlciIsInVzZXJuYW1lIjoidGVzdCJ9LCJleHAiOjE3NDc5NjM1MTB9.w7SIyt1iCqr9rsezOvDonmI5a7JEc4NgYfNdVSZgL8I",
  "map": {}
}
```

```json
{
  "code": 0,
  "msg": "密码错误，请重新尝试",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 发布家教应聘信息接口

POST /api/recommend/publishRecommend

给定价格、科目、线上/线下，细节和时间相关设定，发布应聘信息

> Body 请求参数

```json
{
  "price": 0,
  "subject": "string",
  "online": true,
  "detail": "string",
  "time_num": 0,
  "days": [
    "string"
  ],
  "start_times": [
    "string"
  ],
  "end_times": [
    "string"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» price|body|integer| 是 | 定价|家教的设定加个，多少元/小时|
|» subject|body|string| 是 | 科目|想要补课的科目|
|» online|body|boolean| 是 | 线上|线上或者线下的形式，线上是true，线下是false|
|» detail|body|string| 是 | 描述细节|300字以内的描述细节|
|» time_num|body|integer| 是 | 招聘信息中设置的时间数量|有几个时间这个字段就设置成多少|
|» days|body|[string]| 是 | 星期几|记录星期几|
|» start_times|body|[string]| 是 | 开始时间|记录时间|
|» end_times|body|[string]| 是 | 结束时间|none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "成功发布自荐信息",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET 获取学生/家长需求列表接口

GET /api/recruit/getAllRecruits

get请求方法，获取全部招聘信息

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|pageNum|query|integer| 否 ||none|
|pageSize|query|integer| 否 ||none|
|Authorization|header|string| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": null,
  "data": [
    {
      "recruit": {
        "recruitId": 1,
        "price": 196,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "物理",
        "online": false,
        "detail": "adipisicing officia fugiat sit",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 3,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 4,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 5,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 6,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 7,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 8,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 9,
        "price": 200,
        "schLevel": "jbw",
        "stuLevel": "A",
        "subject": "数学",
        "online": true,
        "detail": "consectetur id do fugiat",
        "stuId": 1
      },
      "recruitDates": null
    },
    {
      "recruit": {
        "recruitId": 11,
        "price": 242,
        "schLevel": "eyy",
        "stuLevel": "C",
        "subject": "计算机",
        "online": false,
        "detail": "et",
        "stuId": 1
      },
      "recruitDates": [
        {
          "dateId": 5,
          "day": "mon",
          "startTime": "18:00:00",
          "endTime": "20:00:00",
          "recruitId": 11
        },
        {
          "dateId": 6,
          "day": "mon",
          "startTime": "15:00:00",
          "endTime": "17:00:00",
          "recruitId": 11
        }
      ]
    }
  ],
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 获取自己发布的家教应聘信息列表

GET /api/recommend/getMyRecommends

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|pageNum|query|integer| 否 ||none|
|pageSize|query|integer| 否 ||none|
|subject|query|string| 否 ||none|
|online|query|string| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": null,
  "data": [
    {
      "recommend": {
        "recommendId": 3,
        "price": 200,
        "subject": "英语",
        "online": false,
        "detail": "sit sunt Ut ea Excepteur",
        "tchId": 1
      },
      "recommendDates": [
        {
          "dateId": 2,
          "day": "mon",
          "startTime": "11:42:58",
          "endTime": "22:00:15",
          "recommendId": 3
        },
        {
          "dateId": 3,
          "day": "tue",
          "startTime": "13:55:36",
          "endTime": "21:00:00",
          "recommendId": 3
        },
        {
          "dateId": 4,
          "day": "fri",
          "startTime": "14:00:00",
          "endTime": "23:00:00",
          "recommendId": 3
        }
      ]
    }
  ],
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 修改应聘信息

POST /api/recommend/updateRecommend

重新填写对应字段，修改

> Body 请求参数

```json
{
  "recommend_id": "string",
  "price": 0,
  "subject": "string",
  "online": true,
  "detail": "string",
  "time_num": "string",
  "days": [
    "string"
  ],
  "start_times": [
    "string"
  ],
  "end_times": [
    "string"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» recommend_id|body|string| 是 | 自荐编号|none|
|» price|body|integer| 是 | 定价|家教的设定加个，多少元/小时|
|» subject|body|string| 是 | 科目|想要补课的科目|
|» online|body|boolean| 是 | 线上|线上或者线下的形式，线上是true，线下是false|
|» detail|body|string| 是 | 描述细节|300字以内的描述细节|
|» time_num|body|string| 是 | 招聘信息中设置的时间数量|有几个时间这个字段就设置成多少|
|» days|body|[string]| 是 | 星期几|记录星期几|
|» start_times|body|[string]| 是 | 开始时间|记录时间|
|» end_times|body|[string]| 是 | 结束时间|none|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "更新成功",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 删除应聘信息

POST /api/recommend/deleteRecommend

> Body 请求参数

```json
{
  "recommend_id": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» recommend_id|body|integer| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "msg": "string",
  "data": "string",
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 上传家教老师个人信息

POST /api/tchInfo/uploadMsg

上传家教老师的性别、学校、学校层次、教育背景、电话号码和头像url，从而完成个人基本信息的录入

> Body 请求参数

```json
{
  "gender": "string",
  "school": "string",
  "schLevel": "string",
  "eduBg": "string",
  "phoneNum": "string",
  "avatar": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» gender|body|string| 是 | 性别|只能是M或者F，分别代表男性和女性|
|» school|body|string| 是 | 学校|填写学校名称|
|» schLevel|body|string| 是 | 学校层次|jbw、eyy、syl、yb、eb，分别代表酒吧舞、二幺幺、双一流、一本、二本|
|» eduBg|body|string| 是 | 教育背景|bachelor、master、doctor，分别代表本硕博|
|» phoneNum|body|string| 是 | 电话号码|none|
|» avatar|body|string| 是 | 头像|是一个阿里云OSS的链接形式|

> 返回示例

```json
{
  "code": 1,
  "msg": "上传成功",
  "data": null,
  "map": {}
}
```

```json
{
  "code": 1,
  "msg": "更新成功！",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 获取家教老师个人信息

GET /api/tchInfo/getTchMsg

get方法，提供token，自动查询对应在tchInfo表中的教师个人信息
- 如果之前录入过信息，会自动把数据库里存储的信息返回回来
- 如果之前没有录入过信息，那么直接返回null

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|pageNum|query|integer| 否 ||none|
|pageSize|query|string| 否 ||none|
|Authorization|header|string| 是 ||none|

> 返回示例

```json
{
  "code": 1,
  "msg": null,
  "data": null,
  "map": {}
}
```

```json
{
  "code": 1,
  "msg": null,
  "data": {
    "infoId": 1,
    "gender": "F",
    "school": "哈尔滨工业大学",
    "schLevel": "jbw",
    "eduBg": "bachelor",
    "phoneNum": "09004632247",
    "avatar": "https://avatars.githubusercontent.com/u/58246940",
    "tchId": 1
  },
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST 修改家教老师个人头像

POST /api/tchInfo/updateAvatar

上传头像的url，修改头像

> Body 请求参数

```json
{
  "avatar": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» avatar|body|string| 是 | 头像|头像图片的阿里云URL|

> 返回示例

> 200 Response

```json
{
  "code": 1,
  "msg": "更新成功",
  "data": null,
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## GET 依据学生编号获取学生个人信息

GET /api/stuInfo/getStuMsgById

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|stuId|query|integer| 否 ||none|
|Authorization|header|string| 否 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 大模型推荐家教学生

POST /api/student/llmRecommendStudent

> Body 请求参数

```json
{
  "prompt": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» prompt|body|string| 是 | 提示词|家教老师对自己情况的描述|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 工具

## POST 上传文件

POST /api/file/upload

上传一个文件，返回一个阿里云oss上的url

> Body 请求参数

```yaml
file: ""

```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» file|body|string(binary)| 否 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "msg": "string",
  "data": "string",
  "map": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|» msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|» data|string|true|none||none|
|» map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

## POST LLM阻塞调用接口

POST /api/llm/chat

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|prompt|query|string| 否 ||none|
|Authorization|header|string| 否 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST LLM流式调用接口

POST /api/llm/stream

> Body 请求参数

```json
[
  {
    "role": "user",
    "content": "你好，你是谁？"
  },
  {
    "role": "assistant",
    "content": "我是一个由 Spring AI 驱动的大语言模型助手。"
  },
  {
    "role": "user",
    "content": "用 Java 写一个快速排序算法"
  }
]
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|Authorization|header|string| 否 ||none|
|body|body|object| 否 ||none|

> 返回示例

> 200 Response

```
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

<h2 id="tocS_R">R</h2>

<a id="schemar"></a>
<a id="schema_R"></a>
<a id="tocSr"></a>
<a id="tocsr"></a>

```json
{
  "code": 0,
  "msg": "string",
  "data": "string",
  "map": {}
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|true|none|编码|1代表请求成功，0代表请求失败|
|msg|string¦null|true|none|信息|对返回结果的一些描述性信息|
|data|string|true|none||none|
|map|object¦null|true|none|动态数据|一些额外的数据可能会从这个字段返回|

<h2 id="tocS_招聘信息">招聘信息</h2>

<a id="schema招聘信息"></a>
<a id="schema_招聘信息"></a>
<a id="tocS招聘信息"></a>
<a id="tocs招聘信息"></a>

```json
{
  "price": 0,
  "sch_level": "string",
  "stu_level": "string",
  "subject": "string",
  "online": true,
  "detail": "string",
  "time_num": "string",
  "days": [
    "string"
  ],
  "start_times": [
    "string"
  ],
  "end_times": [
    "string"
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|price|integer|true|none|定价|家教的设定加个，多少元/小时|
|sch_level|string|true|none|学校层次|想要找到家教的层次，可以为jbw、eyy、syl、yb、eb，分别对应酒吧舞、二幺幺、双一流、一本、二本|
|stu_level|string|true|none|学生水平|ABCDE，五档水平|
|subject|string|true|none|科目|想要补课的科目|
|online|boolean|true|none|线上|线上或者线下的形式，线上是true，线下是false|
|detail|string|true|none|描述细节|300字以内的描述细节|
|time_num|string|true|none|招聘信息中设置的时间数量|有几个时间这个字段就设置成多少|
|days|[string]|true|none|星期几|记录星期几|
|start_times|[string]|true|none|开始时间|记录时间|
|end_times|[string]|true|none|结束时间|none|

