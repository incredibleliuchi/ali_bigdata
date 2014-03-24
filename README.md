ali_bigdata
===========

ali_bigdata_competition


Method & Effectiveness

1.Naive Method One
Record all the products that a person click/collect/add to shopping cart and then buy within 30 days as probably products.
Check all the products that a person click/collect/add to shopping cart in the last month and put all the probably products among them into result set.

Effectiveness: 3 month as known / 1 month as unknown
Precision: 1.52%
Recall:    9.94%
F-value:   2.64%

Official Test:
Precision: 2.98%
Recall:    8.76%
F-value:   4.44%


2.Naive Method Two

认为某人购买某个产品由两方面因素决定
1.这个人是不是倾向于在某种前序行为之后进行购买行为
2.这个物品是不是倾向于在某种前序行为之后被购买

使用转化率，来描述某个前序行为之后的30天之内发生购买行为的概率
共有4种转化率：
点击转化率
收藏转化率
加入购物车转化率
购买转化率

用着4个指标，使用已知数据，分别对所有物品和人进行描述

之后，对于最后一个月的所有行为，根据行为人和行为物品以及行为内容，根据转化率值产生一个评分
设置一个阈值，认为高于阈值的人/物品对，将在下个月发生购买行为

Effectiveness: 3 month as known / 1 month as unknown
Precision: 4.41%
Recall:    7.39%
F-value:   5.52%

Official Test:
Precision: 6.43%
Recall:    4.65%
F-value:   5.40%
