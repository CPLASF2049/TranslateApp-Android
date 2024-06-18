<h1 align = "center">InnoTranslate 翻译APP界面设计文档</h1> 
   
   <div align = "center">
       <img src="../pics/Text Translation Interface.png" width="20%" />
   </div>
   
## 文字翻译界面 (Text Translation Interface)
- 这个界面允许用户输入他们想要翻译的文本。
- 用户可以选择源语言和目标语言，这通常通过下拉菜单（Spinner）或按钮来实现。
- 有一个翻译按钮，用户点击后，应用会处理翻译请求。
- 翻译结果会显示在一个文本视图（TextView）中，用户可以查看翻译后的文本。

<div align = "center">
       <img src="../pics/Voice Translation Interface.png" width="20%" />
   </div>
   
## 语音翻译界面 (Voice Translation Interface)
- 语音翻译界面允许用户通过语音输入进行翻译，适合需要快速翻译或不方便打字的情况。
- 界面上通常会有一个麦克风按钮，用户点击后可以开始录音。
- 用户说出想要翻译的内容，应用会捕捉语音并转换成文本。
- 转换后进行翻译并展示翻译结果。
- 这个界面可能还包括播放按钮，用于播放翻译后的文本的语音。

<div align = "center">
       <img src="../pics/Camera Translation Interface.png" width="20%" />
   </div>

## 摄像机界面 (Camera Translation Interface)
- 摄像机界面利用设备的摄像头来识别现实世界中的文本，并进行翻译。
- 用户打开这个界面后，可以通过摄像头对准需要翻译的文本。
- 应用使用OCR（光学字符识别）技术来识别图像中的文本。

<div align = "center">
       <img src="../pics/Translation History Interface.png" width="20%" />
   </div>

## 翻译历史界面 (Translation History Interface)
- 翻译历史界面用于记录用户之前的翻译请求和结果。
- 用户可以在这个界面查看过去翻译的文本、源语言、目标语言以及翻译的时间戳。
- 通常以列表的形式展示，每一项可能包括原文、译文和翻译详情的简短预览。

<div align = "center">
       <img src="../pics/User Settings Interface.png" width="20%" />
   </div>

## 用户设置界面 (User Settings Interface)
- 用户设置界面允许用户修改他们的账户信息和应用偏好设置。
- 这个界面可能包含用户资料编辑选项，如用户名、密码、电子邮件地址等。
- 可以提供语言偏好设置，允许用户选择应用的显示语言或翻译时的默认语言。
- 可能包含通知设置，让用户选择是否接收应用更新、翻译结果等通知。
- 用户还可以在这个界面管理隐私设置、同步选项或连接的第三方服务。

<div align = "center">
       <img src="../pics/Privacy Policy Interface.png" width="20%" />
   </div>

## 隐私政策界面 (Privacy Policy Interface)
- 隐私政策界面用于向用户展示应用的隐私政策，解释如何收集、使用和保护用户的个人信息。
- 这个界面通常包含详细的文本，概述数据收集的类型、使用目的、用户权利和数据处理的法律依据。
- 隐私政策界面还可能包含链接到其他相关文档，如使用条款或服务协议。

<div align = "center">
       <img src="../pics/Registration Interface.png" width="20%" />
   </div>

## 注册界面 (Registration Interface)
- 注册界面允许新用户创建账户。
- 包括表单字段，让用户输入必要的信息，如用户名、密码等。
- 有密码强度要求的提示，比如最小长度、包含数字和特殊字符等。
- 注册按钮用于提交表单数据。
- 应该有明确的反馈信息，如注册成功或错误提示。

<div align = "center">
       <img src="../pics/Login Interface.png" width="20%" />
   </div>

## 登录界面 (Login Interface)
- 登录界面允许现有用户通过输入凭证（通常是用户名和密码）进入应用。
- 包括用户名和密码输入字段，以及一个登录按钮（如“登录”或“进入”）。
- 有一个“忘记密码？”链接，引导用户到重置密码界面。
- 应有错误提示，如“用户名或密码错误”。

<div align = "center">
       <img src="../pics/Password Reset Interface.png" width="20%" />
   </div>

## 重置密码界面 (Password Reset Interface)
- 重置密码界面帮助用户在忘记密码时恢复账户访问权限。
- 提交按钮（如“发送重置链接”或“重置密码”）用于提交请求。

<div align = "center">
       <img src="../pics/Guest Mode Interface.png" width="20%" />
   </div>

## 访客模式界面 (Guest Mode Interface)
- 访客模式界面允许未登录的用户体验应用的某些功能。
- 展示了应用的基本功能，但限制了需要登录后才能使用的特性，如语音、拍照翻译等。
- 界面上可能会有提示或链接，鼓励用户注册或登录以解锁完整功能。

<div align = "center">
       <img src="../pics/Feedback Interface.png" width="20%" />
   </div>

## 投诉与建议界面 (Feedback Interface)
- 投诉与建议界面让用户能够向开发者提交他们的反馈、投诉或建议。
- 包括一个表单，用户可以在其中填写反馈的类别、详细描述、联系方式等。
- 包含提交按钮，使用户能够发送他们的反馈。
- 反馈提交后，应提供确认信息，如“感谢您的反馈，我们会尽快处理”。

<div align = "center">
       <img src="../pics/Personal Information Edit Interface.png" width="20%" />
   </div>

## 更改个人信息界面 (Personal Information Edit Interface)
- 更改个人信息界面允许已登录的用户更新他们的账户信息。
- 包括表单字段，用户可以在其中修改个人信息，如姓名、电子邮件地址、密码等。
- 提供保存或更新按钮，用于提交信息更改。
- 应有成功或错误提示，告知用户更改是否成功，或在输入有误时提供反馈。
