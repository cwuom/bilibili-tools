
# bilibili-tools

一个基于android开发的哔哩哔哩辅助器

# 说明
***首先非常感谢你能下载并使用这个APP，如果有任何问题都可以向我反馈哦！***
>由于是第一次写这类APP，难免会有些缺陷，[点击这里下载APP](https://gitee.com/cwuom/bilibili-tools-update/blob/test/app-release.apk)

# 此APP已在9月停止更新
> 当你看到这句话时，我很遗憾的告诉你，此项目已经停止维护和更新 （源码丢失）

# 如何使用？
 - 首先在初始界面选择对应的身份，如果你不想关注我，可以在初始界面选择第二个跳过注册
 - 完成验证后，打开B站复制对应的UID或者是视频链接（视频链接就跟分享一个道理）链接可以是短链也可以是长链接
 - 如果你觉得手动粘贴太麻烦了，不妨试试点击上面的时间进行快捷粘贴！（粘贴的内容不对可以选择撤销，点两次时间可以选择清空）
 - 一些特定功能如自动化，需要获取COOKIE才能进行操作。不过不用担心，你只需要登录，软件在登录完成后会自动获取COOKIE，并且不会上传（而且软件也没有服务器）
 - 如果你觉得背景太难看，可以试试点击下面的圆点，自动更换背景。（退出后重新启动仍然生效）
 - ...

# 我能用它干什么？

 >**视频解析**
 

 - **查看视频的播放量、弹幕、回复、收藏、硬币、分享、点赞 等数据（精确到个位）**
 - **复制视频的长链接（重定向）**
 - **复制视频的BV号**
 - **复制视频的标题**
 - **复制视频的AV号**
 - **查看视频的白嫖率（供参考）**
 >**下载视频**
 
 - **支持下载1080P和720P的视频**
 >**弹幕解析**
 
 - **查看弹幕词云**
 - **下载弹幕文件**
 
 >**UID解析**
 - **复制头像链接、主页链接、昵称等**
 - **查看个人数据（性别、UID长度、生日、大会员到期时间、房间号）**
 - **快捷解析TA的主页视频**
 - **计算UP主的白嫖率**
 - **查看UP主的总数据（视频播放、专栏播放、视频点赞数量）**

> **自动化**

 - **自动点赞/取消点赞 UP主的每一个视频**
 - **自动投币**
 - **自动收藏/取消收藏UP主的每一个视频（支持指定收藏夹）**
 
>**其他功能**
 - **大图查看视频的封面，UP主的头像**
 - **支持下载封面和对方的头像**
 - **解析自己（支持查看自己何时加入B站）**
 - ...
 
 >**收藏夹失效视频溯源功能**
 - **查看失效视频的发布者UID,发布者昵称,视频简介,收藏时间,视频发布时间**
 - **支持复制失效视频简介、UID**


 > *实验性功能（不保证效果）*
 
 - 给指定视频刷播放量


# 更新日志
> V1.0
 - 新增基础功能，梦的开始

> V1.5
- 修复若干BUG

> V2.0
- 启用长按底部按钮展开菜单
- 新增解析自己功能
- 新增软件内更新检测
- 更新UI

> V2.1
- 新增自定义背景图片（长按背景）

> V2.2
- 新增触感反馈

> V2.3
- 更改界面样式，优化部分细节
- 增加新手引导

> V2.4
- 新增收藏夹失效视频溯源功能（发布者UID,发布者昵称,视频简介,收藏时间,视频发布时间） -> 支持复制
- 新增选项目前放在主界面底部解析按钮（长按展开菜单）


# 我需要注意什么？

 - **部分功能频繁使用会导致IP被短暂风控（APP不受影响）**
 - **免费软件，禁止倒卖**


# 联系方式

> **WeChat: cwuomcwuom00**

>**QQ: 2594748568**

> 群里还没人，快[点击加入](https://jq.qq.com/?_wv=1027&k=5qSAUYgk)群聊抢沙发吧！


# 项目用到的组件（主要组件）
```` java
implementation 'com.github.li-xiaojun:XPopup:2.8.6'  
implementation 'androidx.appcompat:appcompat:1.3.1'  
implementation 'com.google.android.material:material:1.4.0'  
implementation 'androidx.recyclerview:recyclerview:1.2.1'  

implementation 'com.github.bumptech.glide:glide:4.13.2'  
implementation 'com.google.android.gms:play-services-tasks:17.2.1'  
annotationProcessor 'com.github.bumptech.glide:compiler:4.13.2'  

implementation 'org.jsoup:jsoup:1.12.1'  

implementation 'com.squareup.okhttp3:okhttp:4.4.1'  

implementation 'com.github.mmin18:realtimeblurview:1.2.1' // 遮罩模糊  

implementation 'nl.bryanderidder:themed-toggle-button-group:1.4.1'  
//    implementation 'com.github.Chrisvin:FlipTabs:v1.5' 有BUG，废除  

implementation 'com.github.tibolte:elasticdownload:1.0.+' // 文件下载  

implementation 'com.liulishuo.filedownloader:library:1.7.7' // 文件下载  

implementation 'io.github.bitvale:switcher:1.1.2' // 开关按钮  

// 这个UI不错 值得支持  
implementation "com.github.kongzue.DialogX:DialogXIOSStyle:${dialogx_version}"  
implementation "com.github.kongzue.DialogX:DialogX:${dialogx_version}"  
implementation "com.github.kongzue.DialogX:DialogXKongzueStyle:${dialogx_version}"  
implementation "com.github.kongzue.DialogX:DialogXMIUIStyle:${dialogx_version}"  
implementation "com.github.kongzue.DialogX:DialogXMaterialYou:${dialogx_version}"  

implementation "com.ncorti:slidetoact:0.9.0"  

implementation 'androidx.appcompat:appcompat:1.3.0'  
implementation 'com.google.android.material:material:1.4.0'  

implementation 'com.github.GwonHyeok:StickySwitch:0.0.16'  

implementation 'com.etebarian:meow-bottom-navigation:1.3.1'  

implementation 'com.github.DanielMartinus:Stepper-Touch:1.0.1'  

implementation 'com.github.likaiyuan559:TouchEffects:0.4.1'  

implementation 'androidx.constraintlayout:constraintlayout:2.0.4'  
testImplementation 'junit:junit:4.13.2'  
androidTestImplementation 'androidx.test.ext:junit:1.1.3'  
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'  


implementation 'com.github.thomhurst:ExpandableHintText:1.0.7'  

implementation 'com.zzhoujay.richtext:richtext:3.0.7' // MD展示  

// PictureSelector   
implementation 'io.github.lucksiege:pictureselector:v3.10.6'  
implementation 'io.github.lucksiege:compress:v3.10.6'  
implementation 'io.github.lucksiege:ucrop:v3.10.6'  
implementation 'io.github.lucksiege:camerax:v3.10.6'

````

# FAQ
> 我在使用过程中软件界面(layout)显示异常应该怎么办？

 - 可以尝试使用默认字体大小
 - 使用全面屏机型，测试设备: vivo x27
 
 > 当我使用视频解析时，什么都没加载出来怎么办？
 - 遇到这种情况一般只能等待几分钟或者几小时，因为你的IP可能已经被B站风控了。
 - 可以正常使用后建议不要使用UID解析中的白嫖率计算功能，不建议解析拥有大量视频的用户（软件会提示是否继续）

> 我遇到的问题不在这里怎么办？

 - [可以点击这里在线反馈](https://github.com/cwuom/bilibili-tools/issues?q=)
