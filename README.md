#### MyRimworld

###### 项目说明：

这个项目是使用Java开发一个融合多种不同的游戏玩法的游戏。

目前想法是融合《Rimworld》、《Minecraft》

---



###### 基本游戏设定：

- 上帝视角、2D平面游戏。
- 无边界地图、地图根据算法无限生成。
- 前期使用简单图标甚至文本来代替场景及角色显示，增加开发速度。
- 根据算法自动生成势力。
- 游戏开始时会生成大量的人类，分散在初始地图的各个部位，这些人类属于这个世界的原居民。
- 除了生成的原居民外还会生成几个人类隶属于玩家，这些就是玩家可操控的角色。
- 所有生物死亡后都无法复活，玩家操控的角色也不例外，当玩家没有任何可操控的角色后游戏就结束了。
- 10个游戏世界单位等于现实一米

坐标系：

<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
  <!-- X Axis -->
  <line x1="50" y1="10" x2="50" y2="90" stroke="black"/>
  <text x="55" y="15">Y</text>
  <text x="46" y="4">x</text>
  <!-- Y Axis -->
  <line x1="10" y1="50" x2="90" y2="50" stroke="black"/>
  <text x="85" y="45">X</text>
  <!-- Origin Point -->
  <circle cx="50" cy="50" r="2" fill="black"/>
</svg>


---



###### 初始概念设定：

- 势力：根据人口规模来划分势力类型。玩家也可以创建势力。
  - 村庄：
  - 城镇：
  - 国家：
- 生物：所有种族都属于生物
  - 人类：人类会根据一些策略自动活动。人类和人类之间会存在互动，每次互动根据互动类型增加或减少双方好感度。
    - 普通人：加入了势力的人类。
    - 野人：没有加入任何势力的人类。
    - 伙伴成员：和普通人类差不多，但是玩家可以操控。游戏开始时会有几个默认的伙伴，玩家也可以通过一些手段招募其他人类成为伙伴。伙伴诞生的子嗣同样是伙伴。

  - 动物：动物可以被驯服，驯服后的动物可以执行一些简单的指令，比如攻击
    - 饲养：有饲主的动物。
    - 野生：游戏会根据策略自动刷新的野生生物。
- 时间设定：游戏内一天时间为现实世界的24分钟，每120个游戏天为一年，每30天为一个季度。
- 加速设定：加速是加快游戏时间流速的的设定，可以快速度过某个时间段，加速倍率暂定为：暂停、常速、二倍速、四倍速、十倍速
- 命令：伙伴成员会根据命令发起顺序执行相应的命令。
  - 控制伙伴时的状态：当有控制伙伴时操作模式会改变，而且会根据控制数量来使用不同的模式。
    - 单人控制：只控制一个伙伴的情况下会变成单人控制。
    - 多人控制：控制多个伙伴的情况下变成多人控制。

  - 常规模式：
    - 右键面板：常用命令放在右键面板。鼠标放在物体上使用右键打开右键面板。

  - 攻击命令：对可摧毁的建筑物或生物使用攻击命令，玩家的人类会自动攻击相应目标。
- 关系设定：
  - 人类与人类：
    - 父亲：
    - 母亲：
    - 女儿：
    - 儿子：
    - 女朋友：
    - 男朋友：
    - 
- 好感度阶段设定：
  - 你死我活 b3：好感度：小于等于 -200。说明：当两个人见面时有较大概率打架。
  - 仇人 b2：好感度：小于等于-100，大于-200。
  - 讨厌 b1：好感度：小于0，大于-100。
  - 陌生人 0：`==0`
  - 相识 h1：好感度：>。
  - 熟人 h2：好感度：小于等于150，大于50。
  - 朋友 h3：好感度：大于150。
  - 恋人 恋人：条件：异性，好感度大于150，表白成功。
  - 伴侣 伴侣：条件：异性，结婚。
- 性格设定：
  - 人类：
  - 动物：
- 行为设定：
- 背包设定：所有生物都可以随身带点东西，通过重量来限制每个生物可以携带东西的总重量。
- 年龄阶段：不同的年龄阶段属性会乘以相应的系数，目前年龄阶段分为
  - 幼年：年龄范围：0.00 - 0.08
  - 少年：年龄范围：0.08 - 0.14
  - 成年：年龄范围：0.14 - 0.65
  - 老年：年龄范围：0.65 - 1.00
- 状态：BUFF
- 工作：与“环世界”一致
- 行为时间段：与“环世界”一致，定义每生物的每个时间段，生物会在指定的时间段进行相应的行为。
  - 休息：
  - 工作：
  - 娱乐：
  - 自由活动：
- 行为顺序：优先级从上到下，正常情况下，生物都会处于行为中，
  - 休息区间：当时间段处于休息区间时，相应的生物会休息。
    - 睡觉：只有睡觉一种行为会发生。

  - 工作：工作区间，理论上是工作，但是当没有任何工作的时间会执行其他事情，工作也分为了两种：命令、普通工作。行为顺序如下：
    1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
    2. 命令工作：只有吃饱后才有力气干活。
    3. 普通工作：有工作时，会自动工作。
    4. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
    5. 娱乐：概率发生，随机和各种娱乐设施互动。
    6. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
    7. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。

  - 娱乐：娱乐时间段，生物不会干活。行为顺序如下：
    1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
    2. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
    3. 娱乐：概率发生，较大概率，随机和各种娱乐设施互动。
    4. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
    5. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。

  - 自由活动：自由活动区间比较难定义。理论上所有行为都可能发生。
    1. 吃东西：当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
    2. 睡觉：当休息值小于20%时会睡觉。
    3. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
    4. 娱乐：概率发生，随机和各种娱乐设施互动。
    5. 命令工作：有命令工作时，会执行相应的命令。
    6. 普通工作：有工作时，会自动工作。
    7. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
    8. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
- 行为设定：
  - 吃东西：生物会自动寻找并吃掉相应的食物，一直到饱食度达到100%。
  - 互动：生物会主动根据策略寻找附近的生物互动，互动类型定义如下：
    - 打招呼：生物会找到目标生物，然后移动到目标生物身边，目标生物要求如下：
      - 不处于睡觉行为下
- 招募：玩家可以招募其他人或动物成为伙伴，
  - 关系：如果前往招募的伙伴成员与招募对象存在一定的关系则招募成功率会增加
  - 连带招募：招募目标对象后有可能会带着与目标对象关系亲密的对象。
- 殖民者：玩家可以控制的角色，不同的游戏开局默认殖民者数量可能不同。
- 原居民：游戏自动生成的世界原居民，玩家可以通过一些手段招募原居民。有些原居民可能具有攻击性。
- 殖民者自由活动：殖民者自动根据策略或相应算法自由活动。
- 控制殖民者：当玩家控制殖民者时可以干预殖民者的所有动作。
- 殖民者的背包：每个殖民者都有一定的空间可以携带东西，携带东西数量由重量来控制，殖民者可携带的通过一些策略算法计算
- 殖民者工作：当玩家发起工作指令时，会有相应的殖民者前往完成指令。如果指令需要特殊的工具才能进行的话，殖民者会自动前往工具架或仓库装备相应的工具
- 生物：殖民者、原居民以及所有无智慧生物都属于生物。生物基础属性有生命值、饱食度并且根据不同的种族还有一些种族特定的属性。
- 你死我活：好感度：小于等于 -200。说明：当两个人见面时有较大概率打架。
- 仇人：好感度：小于等于 -100，大于-200。
- 讨厌：好感度：小于0，大于-100。
- 陌生人：好感度：小于等于5，大于等于0。
- 相识：好感度：大于5，小于等于50。
- 熟人：好感度：小于等于150，大于50。
- 朋友：好感度：大于150。
- 恋人：条件：异性，好感度大于150，表白成功。
- 伴侣：条件：异性，结婚。



---



###### 属性设定：

- 生物：生命值、年龄、寿命、性别
  - 人类：心情值、疲劳值
  - 动物
    - 猪
    - 鸡
    - 羊
    - 牛
    - 狼



---



###### 进度：

- [x] 基础框架 - 使用开源框架
  - [x] 游戏框架
    - [x] 摄像机
    - [x] UI框架
  
- [ ] UI

  - [ ] 系统界面
    - [x] 开始新游戏
    - [x] 载入存档
    - [x] 设置面板
  - [ ] 游戏UI
    - [x] 时间显示
    - [x] 时间缩放控件
  - [ ] 底部工具栏
  - [ ] 顶部工具栏
- [ ] AI系统
- [ ] 指令系统
- [x] 摄像机移动系统
  - [x] WASD移动
  - [x] 鼠标移动
  - [x] 滚轮缩放

- [ ] 自动生成地图
  - [x] 地图区块
    - [x] 区块自动卸/装载

  - [x] 后台线程生成地图
  - [x] 柏林噪声算法
  - [ ] 预定义建筑物
  - [ ] 生物群系

- [ ] 时间系统
  - [x] 虚拟游戏时间
  - [ ] 时间天气显示

- [ ] 季节系统
- [ ] 移动系统
  - [ ] 通过性设定
  - [ ] 路障检测
  - [ ] 路线规划

- [ ] 生物系统
- [ ] 建筑系统









