![alt](https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/geekrewardplus-2.png)
# 🎁GeekRewardPlus-高级累计奖励系统 V2
- 多模块 多种排行榜
- 超高自由度 GUI 满足你对累计奖励的大部分需求

- **喜欢我的插件:** [无偿赞助](https://afdian.net/a/hsdserver)
- **交流群:** [点击加入](https://jq.qq.com/?_wv=1027&k=Lp3ehyTi)

<br/>

## 💽兼容性
| 说明 |                         作用                          |
| :----: |:---------------------------------------------------:|
| 支持版本 |                    1.12x - 1.20x                    |
| 可选依赖 | PlaceholderAPI、PlayerPoints、CMi、GeekEconomy、XConomy |
| 数据储存 |                    Mysql、Sqlite                     |

<br/>

---

<br/>

## 📒插件介绍

* ♻️GUI交互使用 **Kether** 脚本语言
* 💾多种数据储存模式 **MYSQL / SQLITE**
* ❇️超高自由度的 **GUI** 配置，设置奖励更加方便
* ❇️支持 **累计充值奖励** ，玩家累计获得点券可获得相应礼包
* ❇️支持 **累计金币奖励** ，玩家累计获得金币可获得相应礼包
* ❇️支持 **累计在线奖励** ，玩家累计在线可获得相应礼包
* ❇️三种奖励模式均有独立排行榜变量，可展示 1-99999 名排行榜
* ❇️每种模式均为腐竹提供修改玩家数据的途径
* ❇️使用 **Kether** 100%自定义玩家交互提示

<br/>

---

<br/>

## ❗❗❗指令帮助
| 玩家命令 | 作用说明 |
| :---------- | :---------- |
| /GeekRw 或 /pms | 主命令 |
| /GeekRw open [菜单种类] | 打开对应礼包领取界面 |


| 管理员命令 | 作用说明 |
| :---------- | :---------- |
| /GeekRw 或 /pms | 主命令 |
| /GeekRw data [目标] [种类] [add/take] [值] | 修改指定玩家指定数据 |
| /GeekRw reset [目标] | 重置指定玩家的所有数据 |
| /GeekRw reload | 重新载入 ^(配置文件&语言文件是自动重载)^ |
| /GeekRw migrator [目标数据库] | 迁移 PMReward 数据库中的数据|

```
使用: /rw
        ├── open [<种类>] # 打开指定种类的奖励菜单
        │
        ├── reset # 重置玩家数据
        │   └── [<玩家名称>] # 重置该玩家的所有数据
        │       └──[<种类>]  # 重置这个玩家指定种类的领取数据
        │          └── [<key>] # 重置这个玩家指定 KEY 的领取数据
        │
        ├── data # 设置玩家的数据
        │   └── [<玩家名称>]
        │       └──[<对象类型>]
        │          └── [<操作类型>]
        │              └── [<值>] # 给与或扣除玩家的累计数量
        │
        └── reload # 重载配置
```

## 权限
```
GeekRewardPlus.Command.reload # 重载指令
GeekRewardPlus.Command.reset # 使用数据重置相关指令
GeekRewardPlus.Command.data # 使用数据修改相关指令
```

<br/>

---

## 💡占位符变量
| 变量 | 效果 |
| :---------- | :---------- |
| %GeekReward_points% | 累计充值数量 |
| %GeekReward_money% | 累计获得金币数量 |
| %GeekReward_time% | 累计在线时间戳 |
| %GeekReward_time_format% | 格式化后的累计在线时间 |
| %GeekReward_pointsTop_(amt/name)_(1/9999)% | 累计充值排行榜 |
| %GeekReward_moneyTop_(amt/name)_(1/9999)% | 累计赚钱排行榜 |
| %GeekReward_timeTop_(amt/name)_(1/9999)% | 累计在线排行榜 |

:::tip 版本
%GeekReward_pointsTop_name_1% #将返回累计充值排行单一的玩家名称
:::

---
# 配置

若插件成功安装，你将会在服务端插件目录下看到一个名为 GeekRewardPlus 的目录，其结构如下
ps: 默认配置中均留有注释，可方便的知道其作用。
```
GeekRewardPlus
│  settings.yml ················ 插件配置文件
│  menu.yml ············ 菜单配置文件
│  kether.yml ················ Kether 配置文件
│  
├─data.db ······················· 插件本地数据
│      
├─lang ······················· 语言文件
│      zh_CN.yml
│
├─money ······················· 累计金币相关奖励配置
│      def.yml
├─time ······················· 累计在线相关奖励配置
│      def.yml   
└─points ·················· 累计点券相关配置
       def.yml
```


# 迁移 V1 配置及其数据？
:::tip
目前不支持，未来可能提供迁移方法。
:::

# 🔆更多图片展示

### 排行榜
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/QQ%E5%9B%BE%E7%89%8720220831224156.png" width="100%">

### GUI
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/2D21OWGZ2Q%24J%24%28Q%5BONTDTB5.png" width="30%">
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/94560%25%40C7RPM%5D21LB%60N%5DL8N.png" width="30%">
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/%5BHST0%40NT%2414758_%252~04TZM.png" width="30%">

<img src="https://bstats.org/signatures/bukkit/GeekRewardPlus.svg">