package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.zzhoujay.richtext.RichText;

public class AboutActivity extends AppCompatActivity {

    private TextView mTvMarkdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTvMarkdown = findViewById(R.id.tv_md);

        RichText.fromMarkdown("\n" +
                "# bilibili-tools\n" +
                "\n" +
                "一个基于android开发的哔哩哔哩辅助器\n" +
                "\n" +
                "\n" +
                "\n" +
                "# 说明\n" +
                "***首先非常感谢你能下载并使用这个APP，如果有任何问题都可以向我反馈哦！***\n" +
                "> 由于是第一次写这类APP，难免会有些缺陷，[GITHUB](https://github.com/cwuom/bilibili-tools/)\n" +
                "\n" +
                "# 如何使用？\n" +
                "\n" +
                " - 首先在初始界面选择对应的身份，如果你不想关注我，可以在初始界面选择第二个跳过注册\n" +
                " - 完成验证后，打开B站复制对应的UID或者是视频链接（视频链接就跟分享一个道理）链接可以是短链也可以是长链接\n" +
                " - 如果你觉得手动粘贴太麻烦了，不妨试试点击上面的时间进行快捷粘贴！（粘贴的内容不对可以选择撤销，点两次时间可以选择清空）\n" +
                " - 一些特定功能如自动化，需要获取COOKIE才能进行操作。不过不用担心，你只需要登录，软件在登录完成后会自动获取COOKIE，并且不会上传（而且软件也没有服务器）\n" +
                " - 如果你觉得背景太难看，可以试试点击下面的圆点，自动更换背景。（退出后重新启动仍然生效）\n" +
                " - ...\n" +
                "\n" +
                "# 我能用它干什么？\n" +
                "\n" +
                " > **视频解析**\n" +
                " \n" +
                "\n" +
                " - **查看视频的播放量、弹幕、回复、收藏、硬币、分享、点赞 等数据（精确到个位）**\n" +
                " - **复制视频的长链接（重定向）**\n" +
                " - **复制视频的BV号**\n" +
                " - **复制视频的标题**\n" +
                " - **复制视频的AV号**\n" +
                " - **查看视频的白嫖率（供参考）**\n" +
                " > **下载视频**\n" +
                " \n" +
                " - **支持下载1080P和720P的视频**\n" +
                " > **弹幕解析**\n" +
                " \n" +
                " - **查看弹幕词云**\n" +
                " - **下载弹幕文件**\n" +
                " \n" +
                " > **UID解析**\n" +
                " - **复制头像链接、主页链接、昵称等**\n" +
                " - **查看个人数据（性别、UID长度、生日、大会员到期时间、房间号）**\n" +
                " - **快捷解析TA的主页视频**\n" +
                " - **计算UP主的白嫖率**\n" +
                " - **查看UP主的总数据（视频播放、专栏播放、视频点赞数量）**\n" +
                "\n" +
                "> **自动化**\n" +
                "\n" +
                " - **自动点赞/取消点赞 UP主的每一个视频**\n" +
                " - **自动投币**\n" +
                " - **自动收藏/取消收藏UP主的每一个视频（支持指定收藏夹）**\n" +
                " \n" +
                "> **其他功能**\n" +
                " - **大图查看视频的封面，UP主的头像**\n" +
                " - **支持下载封面和对方的头像**\n" +
                " - **解析自己（支持查看自己何时加入B站）**\n" +
                " - ...\n" +
                "\n" +
                "\n" +
                " > *实验性功能（不保证效果）*\n" +
                " \n" +
                " - 给指定视频刷播放量\n" +
                "\n" +
                "\n" +
                "# 联系方式\n" +
                "\n" +
                "> **WeChat: cwuomcwuom00**\n" +
                "\n" +
                "> **QQ: 2594748568**\n" +
                "\n" +
                "> 群里还没人，快[点击加入](https://jq.qq.com/?_wv=1027&k=5qSAUYgk)群聊抢沙发吧！\n" +
                "\n" +
                "# FAQ\n" +
                "> 我在使用过程中软件界面(layout)显示异常应该怎么办？\n" +
                "\n" +
                " - 可以尝试使用默认字体大小\n" +
                " - 使用全面屏机型，测试设备: vivo x27\n" +
                " \n" +
                " > 当我使用视频解析时，什么都没加载出来怎么办？\n" +
                " - 遇到这种情况一般只能等待几分钟或者几小时，因为你的IP可能已经被B站风控了。\n" +
                " - 可以正常使用后建议不要使用UID解析中的白嫖率计算功能，不建议解析拥有大量视频的用户（软件会提示是否继续）\n" +
                "\n" +
                "> 我遇到的问题不在这里怎么办？\n" +
                "\n" +
                " - [可以点击这里在线反馈](https://github.com/cwuom/bilibili-tools/issues?q=)\n").bind(AboutActivity.this).into(mTvMarkdown);
    }
}