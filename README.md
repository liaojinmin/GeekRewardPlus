![alt](https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/geekrewardplus.png)
# 🎁GeekRewardPlus-高级累计奖励系统 ^1.1^
- 地表超强累计系统
- 多模块 多种排行榜
- 超高自由度 GUI 满足你对累计奖励的大部分需求

- **喜欢我的插件:** [无偿赞助](https://afdian.net/a/hsdserver)
- **交流群:** [点击加入](https://jq.qq.com/?_wv=1027&k=Lp3ehyTi)

<br/>

## 💽兼容性
| 说明 | 作用 |
| :----: | :----: |
| 支持版本 | 1.12x - 1.19x |
| 可选依赖 | PlaceholderAPI、PlayerPoints、CMi、PM |
| 可迁移的插件 | PMReward |
| 数据储存 | Mysql、Sqlite |

<br/>

---

<br/>

## 📒插件介绍
* **🎉100%支持迁移 [PMReward](https://www.mcbbs.net/thread-894209-1-1.html) 插件数据**

* ♻️GUI交互使用 **Kether** 脚本语言
* 💾多种数据储存模式 **MYSQL / SQLITE**
* ❇️超高自由度的 **GUI** 配置，设置奖励更加方便
* ❇️支持 **累计充值奖励** ，玩家累计获得点券可获得相应礼包
* ❇️支持 **累计获得金币奖励** ，玩家累计获得金币可获得相应礼包
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

<br/>

---

## 🎛️权限帮助
| 权限 | 作用 |
| :---------- | :---------- |
| GeekRewardPlus.admin | （^迁移、重载、重置、设置）^ 权限 |
| GeekRewardPlus.menu | 打开菜单权限 |

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

> 列如:
> %GeekReward_pointsTop_name_1% #将返回累计充值排行单一的玩家名称
{.is-info}

<br/>

---

# 🔊如何修改奖励及其需求？
在 **plugins/GeekRewardPlus/menu** 文件夹下修改对应配置文件

## 累计在线奖励菜单
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/PT1X%7DC4%60%5BHXK%281NANASEDV8.png" width="60%">

``` YAML
TITLE: '&0累计在线时长礼包'

TYPE: OnlineTime
Layout:
  - '#########'
  - '#A B C D#'
  - '#########'

Icons:
  A:
    display:
    # 展示的材质
      mats: BOOK
      name: "&e累计在线 &f一小时 &e礼包"
      # 礼包唯一ID 
      packID: '一小时礼包'
      # 领取该礼包需要的条件
      # 设置格式 ??d ??h ??m ??s   (天、小时、分钟、秒)
      isValue: 1h # 这里需要玩家累计在线 1小时
      #isValue: 1d 18h  #这样表示 需要累计在线一天十八小时才可以领取
      lore:
        - "&f礼包内容:"
        - "&f - 100 金币"
        - "&f当前在线 &A{time}"
        - "&f领取状态: {state}"
     # 条件及其所有动作语句 参考 Kether
    Require:
    # 条件默认会判断这个礼包的 isValue 值 搭配下面的条件组可实现多种交互
      condition: 'not timePack *一小时礼包'
      action: |-
        command "money give {player_name} 100" as console
        sound ENTITY_EXPERIENCE_ORB_PICKUP by 1 1
      deny: |-
        sound BLOCK_NOTE_BLOCK_DIDGERIDOO by 1 1
        if timePack *一小时礼包
           then tell "你已经领取过这个礼包"
        else
           tell "你的在线时长不够!"
  B:
    display:
      mats: BOOK
      name: "&e累计在线 &f三小时 &e礼包"
      packID: '三小时礼包'
      isValue: 3h
      lore:
        - "&f礼包内容:"
        - "&f - 980 金币"
        - "&f当前在线 &A{time}"
        - "&f领取状态: {state}"
    Require:
      condition: 'not timePack *三小时礼包'
      action: |-
        command "money give {player_name} 9800" as console
        sound ENTITY_EXPERIENCE_ORB_PICKUP by 1 1
      deny: |-
        sound BLOCK_NOTE_BLOCK_DIDGERIDOO by 1 1
        if timePack *三小时礼包
           then tell "你已经领取过这个礼包"
        else
           tell "你的在线时长不够!"
  '#':
    display:
      mats: BLACK_STAINED_GLASS_PANE
      name: '&b&l 挡板'
```
## 2.累计充值菜单
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/VB7P2%405%5BAQHCMJN%24H38B%24FF.png" width="100%">

``` YAML
TITLE: '&e累计充值 &e点卷礼包'

TYPE: PlayerPoints
Layout:
  - '#########'
  - '#A B C D#'
  - '#########'

Icons:
  A:
    display:
      mats: BOOK
      name: "&e累计充值 &c100 &e点卷礼包"
      # 奖励唯一ID
      packID: 10
      # 需求值
      isValue: 100 #这里表示玩家需要获得100 点券才可以领取
      lore:
        - "&f礼包内容:"
        - "&f - 1000 金币"
        - "&f需要充值 &c100 &f点卷才可领取"
        - "&f当前充值 &A{points}"
        - "&f领取状态: {state}"
    Require:
    # 条件判断默认会判断该礼包的 isValue 值玩家是否达到，搭配一下条件组可实现多种交互模式
    # not pointsPack *10 这里的 10 表示这个礼包的唯一ID
      condition: 'not pointsPack *10' # 玩家需要没领取过这个礼包才可以领取 
      action: |-
        command "money give {player_name} 1000" as console
        sound ENTITY_EXPERIENCE_ORB_PICKUP by 1 1
      deny: |-
        sound BLOCK_NOTE_BLOCK_DIDGERIDOO by 1 1
        if pointsPack *10
           then tell "你已经领取过这个礼包"
        else
           tell "你的充值数量不够!"
  B:
    display:
      mats: BOOK
      name: "&e累计充值 &c300 &e点卷礼包"
      packID: "累计300"
      isValue: 300
      lore:
        - "&f礼包内容:"
        - "&f - 3000 金币"
        - "&f需要充值 &c300 &f点卷才可领取"
        - "&f当前充值 &A{points}"
        - "&f领取状态: {state}"
    Require:
      condition: 'not pointsPack *累计300'
      action: |-
        command "money give {player_name} 3000" as console
        sound ENTITY_EXPERIENCE_ORB_PICKUP by 1 1
      deny: |-
        sound BLOCK_NOTE_BLOCK_DIDGERIDOO by 1 1
        if pointsPack *累计300
           then tell "你已经领取过这个礼包"
        else
           tell "你的充值数量不够!"
  '#':
    display:
      mats: BLACK_STAINED_GLASS_PANE
      name: '&b&l 挡板'
```


<br/>

---

# ❗ Kether脚本
| 内置Kether脚本 | 作用 |
| :---------- | :---------- |
| moneyPack [action] | 判断玩家的累计金币礼包ID |
| pointsPack [action] | 判断玩家的累计充值礼包ID |
| timePack [action] | 判断玩家的累计在线礼包ID |

> 【**timePack 三小时礼包**】 判断玩家是否领取过 （三小时礼包）在线礼包，如果领取过返回 True
> 【**pointsPack 充值100**】 判断玩家是否领取过 （充值100）充值礼包，如果领取过返回 True
> 【**moneyPack 100**】 判断玩家是否领取过 （100）获取金币礼包，如果领取过返回 True
{.is-success}

> 更多 **Kether** 脚本表达式  [点击前往](https://kether.tabooproject.org/list.html)
{.is-info}

<br/>

---

# 🔆更多图片展示
### 排行榜
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/QQ%E5%9B%BE%E7%89%8720220831224156.png" width="100%">
<br/>

### GUI
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/2D21OWGZ2Q%24J%24%28Q%5BONTDTB5.png" width="30%">
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/94560%25%40C7RPM%5D21LB%60N%5DL8N.png" width="30%">
<img src="https://web-1301331373.cos.ap-guangzhou.myqcloud.com/docs/%5BHST0%40NT%2414758_%252~04TZM.png" width="30%">

---

<img src="https://bstats.org/signatures/bukkit/GeekRewardPlus.svg">

---

# ⚡更新日志
> 202009012140: 版本号 1.1
> > 1. **发布:** 发布插件
       > {.is-info}