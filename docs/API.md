<h1 align="center">API接口文档</h1>

## 1 通用翻译接口

### 接口API:

    https://fanyi-api.baidu.com/api/trans/vip/translate

#### 接入方式

    通用翻译API通过 HTTP 接口对外提供多语种互译服务。需要通过调用通用翻译API，传入待翻译的内容，并指定要翻译的源语言（支持源语言语种自动检测）和目标语言种类，就可以得到相应的翻译结果。

#### 输入参数
    
- 字符编码：统一采用 UTF-8 编码格式
- query 长度：为保证翻译质量，请将单次请求长度控制在 6000 bytes以内（汉字约为输入参数 2000 个）

| 字段名 |  类型  | 是否必填 |     描述      |             备注             |
| :----: | :----: | :------: | :-----------: | :---------------------------: |
|   q    | string |    是    | 请求翻译query |            UTF-8编码           |
|  from  | string |    是    |  翻译源语言   |          可设置为auto          |
|   to   | string |    是    | 翻译目标语言  |         不可设置为auto         |
| appid  | string |    是    |     APPID     |       可在管理控制台查看       |
|  salt  | string |    是    |    随机数     |      可为字母或数字的字符串     |
|  sign  | string |    是    |     签名      |     appid+q+salt+密钥的MD5值    |

#### 输出参数

- 返回的结果是json格式，包含以下字段：

|       字段名       |  类型   |   描述   |                            备注                             |
| :----------------: | :-----: | :------: | :----------------------------------------------------------: |
|        from        | string  |  源语言  | 返回用户指定的语言，或者自动检测出的语种（源语言设为auto时）  |
|         to         | string  | 目标语言 |                    返回用户指定的目标语言                    |
|    trans_result    |  array  | 翻译结果 |                 返回翻译结果，包括src和dst字段                |
| trans_result.*.src | string  |   原文   |                      接入举例中的“apple”                     |
| trans_result.*dst  | string  |   译文   |                      接入举例中的“苹果”                      |
|     error_code     | integer |  错误码  |                      仅当出现错误时显示                      |

### **接入举例**

例如：将英文单词 apple 翻译成中文：

#### **请求参数**：

​     q=apple
​     from=en
​     to=zh
​     appid=2015063000000001（请替换为您的appid）
​     salt=1435660288（随机码）
​     平台分配的密钥: 12345678

#### **生成签名sign：**

- Step1. 拼接字符串1：
  拼接appid=2015063000000001+q=apple+salt=1435660288+密钥=12345678得到字符串1：“2015063000000001apple143566028812345678”
- Step2. 计算签名：（对字符串1做MD5加密）
  sign=MD5(2015063000000001apple143566028812345678)，得到sign=f89f9594663708c1605f3d736d01d2d4

#### **拼接完整请求：**

```
http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
```

注：也可使用 POST 方式，如 POST 方式传送，Content-Type 请指定为：application/x-www-form-urlencoded

### **输出举例**

#### **正确情况**：

```
{
    "from": "en",
    "to": "zh",
    "trans_result": [
        {
            "src": "apple",
            "dst": "苹果"
        }
    ]
}
```

### **异常情况**：

```
{
    "error_code": "54001",
    "error_msg": "Invalid Sign"
}
```

### **语种列表**

- 通用翻译API支持语种数已达 201 种，部分列表如下。其中，对于常见语种列表内的语种，所有用户均可调用。
- 源语言语种不确定时可设置为 auto，目标语言语种不可设置为 auto。但对于非常用语种，语种自动检测可能存在误差。

#### **常见语种列表**

|    名称    |   代码   |    名称    |   代码   |    名称    |   代码   |
| :--------: | :------: | :--------: | :------: | :--------: | :--------: |
|  自动检测  |   auto   |     中文    |    zh    |    英语    |    en    |
|    粤语    |   yue    |    文言文   |    wyw   |    日语    |    jp    |
|    韩语    |   kor    |     法语    |    fra   |  西班牙语  |    spa    |
|    泰语    |    th    |   阿拉伯语  |    ara   |    俄语    |    ru    |
|  葡萄牙语  |    pt    |     德语    |    de    |  意大利语  |    it    |
|   希腊语   |    el    |    荷兰语   |    nl    |   波兰语   |    pl    |
| 保加利亚语 |   bul    |  爱沙尼亚语 |    est   |   丹麦语   |    dan    |
|   芬兰语   |   fin    |    捷克语   |    cs    | 罗马尼亚语 |    rom    |
|  繁体中文  |   cht    |    越南语   |    vie   |            |           |

## 2 语音识别接口

### 接口API:

    https://api.iflytek.com/voice_translation/translate

#### 接入方式

    语音翻译API通过 HTTP 接口提供语音到文本的翻译服务。用户需要调用此API，传入待翻译的语音文件，即可获得识别后的文本结果。

#### 输出参数

- 返回的结果是json格式，包含以下字段：

|    JSON字段   |     英文全称     |   类型   |        说明        |
| :-----------: | :--------------: | :-------: | :----------------: |
|      sn      |     sentence      |  number   |        第几句       |
|      ls      |   last sentence   |  boolean  |    是否最后一句    |
|      bg      |       begin       |  number   |  保留字段，无需关注 |
|      ed      |        end        |   number   |  保留字段，无需关注 |
|      ws      |       words       |  	array   |        词         |
|      cw      |    chinese word   |  	array   |     中文分词       |
|      w       |        word       |  string   |        单字        |
|      sc      |       score       |  number   |        分数        |

### 结果示例:

- 普通结果示例：

```
{
    "sn": 1,
    "ls": true,
    "bg": 0,
    "ed": 0,
    "ws": [
        {
            "bg": 0,
            "cw": [
                {
                    "w": "今天",
                    "sc": 0
                }
            ]
        },
        {
            "bg": 0,
            "cw": [
                {
                    "w": "的",
                    "sc": 0
                }
            ]
        },
        {
            "bg": 0,
            "cw": [
                {
                    "w": "天气",
                    "sc": 0
                }
            ]
        },
        {
            "bg": 0,
            "cw": [
                {
                    "w": "怎么样",
                    "sc": 0
                }
            ]
        },
        {
            "bg": 0,
            "cw": [
                {
                    "w": "。",
                    "sc": 0
                }
            ]
        }
    ]
}
```

- 多候选结果示例：

```
{
    "sn": 1,
    "ls": false,
    "bg": 0,
    "ed": 0,
    "ws": [
        {
            "bg": 0,
            "cw": [
                {
                    "w": "我想听",
                    "sc": 0
                }
            ]
        },
        {
            "bg": 0,
            "cw": [
                {
                    "w": "拉德斯基进行曲",
                    "sc": 0
                },
                {
                    "w": "拉得斯进行曲",
                    "sc": 0
                }
            ]
        }
    ]
}
```

## 3 图像识别接口

### 接口API:

    ML Kit提供了一个名为TextRecognition的API，用于执行OCR任务。该API属于com.google.mlkit.vision.text包。

#### 接入方式

- Android SDK: 在Android项目中，通过添加ML Kit依赖到build.gradle文件中。
- iOS SDK: 对于iOS项目，通过CocoaPods集成ML Kit。
- Web API: 对于Web应用，可以使用ML Kit的Web API。

#### 输入参数

TextRecognition API接受以下输入参数：

- Image: 需要进行文字识别的图像。可以是Bitmap对象或ImageSource对象。

#### 输出参数

执行OCR后，API将返回一个TextRecognizerResult对象，其中包含以下信息：

- TextBlocks: 一个包含所有识别到的文字块的列表。
- Confidence: 对识别结果的置信度评分。

### 结果示例:

```
{
  "textBlocks": [
    {
      "text": "Hello, World!",
      "confidence": 0.95,
      "boundingBox": {
        "vertices": [
          {"x": 10, "y": 10},
          {"x": 200, "y": 10},
          {"x": 200, "y": 50},
          {"x": 10, "y": 50}
        ]
      }
    }
  ]
}
```
