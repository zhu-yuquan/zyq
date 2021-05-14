# noinspection SqlNoDataSourceInspectionForFile

-- 表名前缀：pro -- 产品product；
-- 表名前缀：stl -- 结算settle；
-- 表名前缀：pol -- 保单policy；
-- 表名前缀：act -- 账户account；
-- 表名前缀：chl -- 下游渠道channel；
-- 表名前缀：rel -- 关联表relation；
-- 表名前缀：bal -- 基本法basiclaw；
-- 表名前缀：rcd -- 历史记录record；
-- 表名前缀：psw -- 上游通道passageway；
-- 表名前缀：ass -- 助手assistant；
-- 表名前缀：ins -- 保险公司insurer；
-- 表名前缀：jhs -- 计划书；
-- 表名前缀：ptn -- 业务员partner；
-- 表名前缀：ord -- 订单order；
-- 表名前缀：pmt -- 展业promote

CREATE TABLE `ins_base_info`
(
  `id`                         int(11)      NOT NULL AUTO_INCREMENT,
  `insurer_no`                 varchar(64)  NOT NULL COMMENT '保险公司编号',
  `full_name`                  varchar(128) NOT NULL COMMENT '保险公司名称',
  `short_name`                 varchar(128) DEFAULT NULL COMMENT '保司简称',
  `insurer_intro`              varchar(512) DEFAULT NULL COMMENT '保险公司介绍',
  `customer_service_telephone` varchar(16)  DEFAULT NULL COMMENT '客服电话',
  `address_code`               varchar(16)  DEFAULT NULL COMMENT '地址编码',
  `address_detail`             varchar(256) DEFAULT NULL COMMENT '详细地址',
  `create_time`                datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`                datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保险公司基本信息表';

CREATE TABLE `pro_base_info`
(
  `id`                 int(11)     NOT NULL AUTO_INCREMENT,
  `insurer_id`         int(11)     NOT NULL COMMENT '所属保险公司ID',
  `code`               varchar(32) DEFAULT NULL COMMENT '保司产品代码',
  `plat_code`          varchar(32) NOT NULL COMMENT '公司平台的产品编码',
  `name`               varchar(64) NOT NULL COMMENT '产品名称',
  `period_flag`        tinyint(2)  DEFAULT NULL COMMENT '长短险标识：0--长险；1--短险；',
  `observation_period` int(11)     DEFAULT 90 COMMENT '等待期',
  `hesitation_period`  int(11)     DEFAULT 15 COMMENT '犹豫期',
  `create_time`        datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`        datetime    DEFAULT current_timestamp COMMENT '更新时间',
  primary key (`id`)
) ROW_FORMAT = DYNAMIC COMMENT = '产品基本信息表';

CREATE TABLE `pro_sub_plan`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `product_id`     int(11)     NOT NULL COMMENT '产品ID',
  `plan_code`      varchar(32) DEFAULT NULL COMMENT '保司产品计划编码',
  `plat_plan_code` varchar(32) NOT NULL COMMENT '公司平台的产品计划编码',
  `plan_name`      varchar(64) NOT NULL COMMENT '产品计划名称',
  `display_name`   varchar(64) DEFAULT NULL COMMENT '展示名称',
  `create_time`    datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime    DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品计划表';

CREATE TABLE `pro_risk`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `product_id`     int(11)     NOT NULL COMMENT '产品ID',
  `plan_id`        int(11)     DEFAULT NULL COMMENT '产品计划ID',
  `risk_code`      varchar(32) DEFAULT NULL COMMENT '保司险种代码',
  `plat_risk_code` varchar(32) NOT NULL COMMENT '公司平台的险种编码',
  `risk_name`      varchar(64) NOT NULL COMMENT '险种名称',
  `main_flag`      tinyint(2)  NOT NULL COMMENT '基本险标识：0 -- 主险；1 -- 附加险；2 -- 附加主险；',
  `create_time`    datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime    DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品险种表';

CREATE TABLE `pro_clause`
(
  `id`             int NOT NULL AUTO_INCREMENT,
  `product_id`     int NOT NULL COMMENT '产品ID',
  `clause_name`    varchar(256) DEFAULT NULL COMMENT '条款名称',
  `clause_url`     varchar(256) DEFAULT NULL COMMENT '条款链接',
  `sort_index`     int          DEFAULT NULL COMMENT '排序标识',
  `must_read_flag` tinyint      DEFAULT '0' COMMENT '文件是否必读：0--非必读；1--必读；',
  `image_url`      varchar(256) DEFAULT NULL COMMENT '必读文件图片地址',
  `create_time`    datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保险条款及告知表';

-- 险种分类、使用人群，都放这个分类里面，用树级关系展示；
-- 适用人群：1成人，2少儿，3父母，4女性
-- 险种分类：1寿险，2年金险，3重疾险，4医疗险，5意外险
CREATE TABLE `ass_category`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `parent_id`      int(11)  DEFAULT NULL COMMENT '上级分类ID',
  `category_code`  varchar(64) NOT NULL COMMENT '分类唯一编号',
  `category_type`  tinyint(2)  NOT NULL COMMENT '分类类型：0 -- 险种分类；1 -- 人群分类；',
  `category_name`  varchar(64) NOT NULL COMMENT '分类名称',
  `category_value` varchar(64) NOT NULL COMMENT '分类取值',
  `sort_index`     tinyint  DEFAULT NULL COMMENT '排序',
  `create_time`    datetime DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='分类表';

CREATE TABLE `rel_product_category`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `product_id`  int(11) NOT NULL COMMENT '产品ID',
  `category_id` int(11) NOT NULL COMMENT '产品分类ID',
  `create_time` datetime DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` datetime DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品分类关联表';

CREATE TABLE `pro_shelf_info`
(
  `id`              int(11)        NOT NULL AUTO_INCREMENT,
  `product_id`      int(11)        NOT NULL COMMENT '产品ID',
  `plat_code`       varchar(32) DEFAULT NULL COMMENT '公司平台的产品编码',
  `intro`           varchar(256)   NOT NULL COMMENT '产品简介',
  `logo_url`        varchar(256)   NOT NULL COMMENT '产品logo的URL',
  `reference_price` decimal(10, 2) NOT NULL COMMENT '参考售价',
  `create_time`     datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`     datetime    DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品货架信息表';

CREATE TABLE `pro_factor`
(
  `id`            int(11)     NOT NULL AUTO_INCREMENT,
  `risk_id`       int(11)     NOT NULL COMMENT '险种ID',
  `display_name`  varchar(64) NOT NULL COMMENT '展示名称',
  `field_name`    varchar(64) NOT NULL COMMENT '内部字段名',
  `default_value` varchar(64)  DEFAULT NULL COMMENT '缺省值',
  `value_rule`    varchar(256) DEFAULT NULL COMMENT '取值规则：参考【因子取值规则】',
  `purpose_flag`  varchar(256) DEFAULT NULL COMMENT '用途标识，多个英文逗号分隔：0 -- 前端不展示该因子；1 -- 现价因子；2 -- 费率因子；',
  `remark`        varchar(256) DEFAULT NULL COMMENT '',
  `sort_index`    int          DEFAULT NULL COMMENT '显示排序',
  `create_time`   datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`   datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='险种因子表';
-- 【因子取值规则】:
-- （*）=main_risk_premium，等于主险保费；
-- （*）=main_risk_amount，等于主险保额；
-- （*）=数值+单位 -- 固定取等号后面的值+单位；举例：=10Y
-- （*）=数值 --  固定取等号后面的值；举例：=100.12
-- （*）-数值+单位 -- 取减号后面的值再减1，然后加上单位；举例：-10Y 表示取值 9Y
-- （*）-数值 -- 取减号后面的值再减1；举例：-99 表示取值 98
-- （*）[min:step:max] -- 取值从min最小值按步长step增加到max最大值；
-- （*）[min:step:) -- 取值从min最小值按步长step增加到无限大；
-- （*）[min,max] -- 取值范围从min最小值到max最大值；
-- （*）yyyy-mm-dd -- 取值年月日格式，如2021-05-07；
-- （*）HH:MM:SS -- 取值时分秒格式，如17:23:57；
-- （*）yyyy-mm-dd HH:MM:SS -- 取值年月日时分秒格式，如2021-05-07 17:23:57；
-- （*）text_display_only -- 仅仅展示文本，不可修改值；
-- （*）附件险或者附加主险取值：0 -- 不投保；平台险种code编码 -- 投保该险种；


-- '缴费因子：缴费期间值+缴费期间单位：C--一次交清; M--月;Q--季度；Y--年; D--日；A--岁；',
-- '保障因子：保障期间值+保障期间单位：M--月; Y--年; D -- 日；A--岁；O -- 终身；',
-- '领取因子：领取时长值+领取时长单位：M--月；Q--季度；HY--半年；Y--年；A--岁',
CREATE TABLE `pro_factor_value`
(
  `id`           int(11)     NOT NULL AUTO_INCREMENT,
  `factor_id`    int(11)     NOT NULL COMMENT '险种因子ID',
  `display_name` varchar(64) NOT NULL COMMENT '展示名称',
  `factor_value` varchar(64)  DEFAULT NULL COMMENT '取值',
  `remark`       varchar(256) DEFAULT NULL COMMENT '备注',
  `sort_index`   int          DEFAULT NULL COMMENT '显示排序',
  `create_time`  datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`  datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='险种因子取值表';

-- xxx使用保司编号
CREATE TABLE `pro_rate_xxx`
(
  `id`                     int(11) NOT NULL AUTO_INCREMENT,
  `plat_code`              varchar(32)    DEFAULT NULL COMMENT '公司平台的产品编码',
  `plat_plan_code`         varchar(32)    DEFAULT NULL COMMENT '公司平台的产品计划编码',
  `plat_risk_code`         varchar(512)   DEFAULT '' COMMENT '公司平台的多个险种编码组合，按Comparator规则排序，英文逗号分隔；',
  `min_age`                int            DEFAULT NULL COMMENT '年龄段下限',
  `max_age`                int            DEFAULT NULL COMMENT '年龄段上限',
  `gender`                 varchar(2)     DEFAULT NULL COMMENT '性别：0--男；1--女；2--未知；9--未说明；',
  `had_social_security`    varchar(2)     DEFAULT NULL COMMENT '是否有社保：0--无；1--有；',
  `personal_or_family`     varchar(2)     DEFAULT NULL COMMENT '个人单或家庭单：1--个人单；2--家庭单；',
  `pay_period`             varchar(8)     DEFAULT NULL COMMENT '缴费因子：缴费期间值+缴费期间单位：C--一次交清; M--月;Q--季度；Y--年; D--日；A--岁；',
  `insure_period`          varchar(8)     DEFAULT NULL COMMENT '保障因子：保障期间值+保障期间单位：M--月; Y--年; D -- 日；A--岁；O -- 终身；',
  `draw_period`            varchar(8)     DEFAULT NULL COMMENT '领取因子：领取时长值+领取时长单位：M--月；Q--季度；HY--半年；Y--年；A--岁',
  `draw_start_age`         int(10)        DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`      varchar(2)     DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',
  `extend_json`            text           DEFAULT NULL COMMENT 'json格式的扩展字段',
  `amount_per_1000premium` decimal(10, 3) DEFAULT NULL COMMENT '每千元保费对应保额',
  `premium_per_1000amount` decimal(10, 3) DEFAULT NULL COMMENT '每千元保额对应保费',
  `create_time`            datetime       DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`            datetime       DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='xxx产品费率表';

-- xxx使用产品编号
CREATE TABLE `pro_cash_value_xxx`
(
  `id`                         int(11) NOT NULL AUTO_INCREMENT,
  `plat_code`                  varchar(32)    DEFAULT NULL COMMENT '公司平台的产品编码',
  `plat_plan_code`             varchar(32)    DEFAULT NULL COMMENT '公司平台的产品计划编码',
  `plat_risk_code`             varchar(512)   DEFAULT '' COMMENT '公司平台的多个险种编码组合，按Comparator规则排序，英文逗号分隔；',
  `age`                        int            DEFAULT NULL COMMENT '年龄',
  `policy_end_no`              int            DEFAULT NULL COMMENT '保单年度末',
  `gender`                     varchar(2)     DEFAULT NULL COMMENT '性别：0--男；1--女；2--未知；9--未说明；',
  `had_social_security`        varchar(2)     DEFAULT NULL COMMENT '是否有社保：0--无；1--有；',
  `personal_or_family`         varchar(2)     DEFAULT NULL COMMENT '个人单或家庭单：1--个人单；2--家庭单；',
  `pay_period`                 varchar(8)     DEFAULT NULL COMMENT '缴费因子：缴费期间值+缴费期间单位：C--一次交清; M--月;Q--季度；Y--年; D--日；A--岁；',
  `insure_period`              varchar(8)     DEFAULT NULL COMMENT '保障因子：保障期间值+保障期间单位：M--月; Y--年; D -- 日；A--岁；O -- 终身；',
  `draw_period`                varchar(8)     DEFAULT NULL COMMENT '领取因子：领取时长值+领取时长单位：M--月；Q--季度；HY--半年；Y--年；A--岁',
  `draw_start_age`             int(10)        DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`          varchar(2)     DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',
  `extend_json`                text           DEFAULT NULL COMMENT 'json格式的扩展字段',
  `cash_value_per_1000premium` decimal(10, 3) DEFAULT NULL COMMENT '每千元保费对应现金价值',
  `cash_value_per_1000amount`  decimal(10, 3) DEFAULT NULL COMMENT '每千元保额对应现金价值',
  `create_time`                datetime       DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`                datetime       DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='xxx产品现价表';

CREATE TABLE `pro_duty`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `risk_id`        int(11)     NOT NULL COMMENT '险种ID',
  `duty_code`      varchar(32)  DEFAULT NULL COMMENT '保司责任代码',
  `plat_duty_code` varchar(32) NOT NULL COMMENT '公司平台的责任编码',
  `duty_name`      varchar(64) NOT NULL COMMENT '责任名称',
  `promise_type`   int         NOT NULL COMMENT '保障类型:0--其他责任；1--重疾责任；2--轻症责任；3--中症责任；4--身故或全残责任；5--疾病终末期责任；6--恶性肿瘤责任；7--健康服务责任；8--保费豁免责任；9--特殊重疾责任；10--意外责任；11--猝死或急性病身故责任；12--意外医疗责任；13--意外住院津贴；14--一般医疗责任；15--特定医疗责任；16--住院津贴责任；17--门急诊医疗责任；18--特殊医疗责任；19--年金领取责任；',
  `remark`         varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time`    datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='险别责任表';

CREATE TABLE `pro_disease`
(
  `id`           int(11)      NOT NULL AUTO_INCREMENT,
  `duty_id`      int(11)      NOT NULL COMMENT '险别责任ID',
  `disease_name` varchar(64)  NOT NULL COMMENT '病种名称',
  `disease_desc` varchar(512) NOT NULL COMMENT '病种描述',
  `create_time`  datetime DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`  datetime DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品病种表';

CREATE TABLE `pro_duty_detail`
(
  `id`          int     NOT NULL AUTO_INCREMENT,
  `duty_id`     int(11) NOT NULL COMMENT '险别责任ID',
  `name`        varchar(64)   DEFAULT NULL COMMENT '名称',
  `description` varchar(256)  DEFAULT NULL COMMENT '内容描述',
  `expression`  varchar(1024) DEFAULT NULL COMMENT '计算规则json表达式',
  `remark`      varchar(256)  DEFAULT NULL COMMENT '备注',
  `create_time` datetime      DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime      DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='险别责任子项表';

CREATE TABLE `rel_product_status`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `product_id`  int(11) NOT NULL COMMENT '产品ID',
  `jhs_status`  tinyint(2) DEFAULT NULL COMMENT '计划书状态：0--上架计划书；1--下架计划书；',
  `create_time` datetime   DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` datetime   DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='产品状态关联表';

CREATE TABLE `jhs_user`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `source_type` tinyint NOT NULL COMMENT '用户来源:0--微信OpenID；',
  `openid`      varchar(64) DEFAULT NULL COMMENT '微信OpenID，唯一标识',
  `create_time` datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` datetime    DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='计划书用户表';

CREATE TABLE `jhs_list`
(
  `id`          int(11)     NOT NULL AUTO_INCREMENT,
  `jhs_user_id` int(11)     NOT NULL COMMENT '用户id',
  `jhs_name`    varchar(64) NOT NULL COMMENT '计划书名称',
  `jhs_type`    tinyint(1)  NOT NULL COMMENT '计划书类型：1--模板计划书；2--家庭计划书；3--产品计划书；',
  `remark`      varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` datetime     DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ROW_FORMAT = DYNAMIC COMMENT ='计划书历史列表';

CREATE TABLE `jhs_insured`
(
  `id`           int(11) NOT NULL AUTO_INCREMENT,
  `jhs_id`       int(11) NOT NULL COMMENT '计划书id',
  `name`         varchar(64) DEFAULT NULL COMMENT '姓名',
  `gender`       varchar(2)  DEFAULT NULL COMMENT '性别：0--男；1--女；2--未知；9--未说明；',
  `birthday`     varchar(16) DEFAULT NULL COMMENT '生日',
  `age`          int         DEFAULT NULL COMMENT '年龄',
  `relation`     varchar(2)  DEFAULT NULL COMMENT '与本人的关系：00--本人；01--配偶；02--子女;03--父母；',
  `member_index` int         DEFAULT NULL COMMENT '家庭成员下标',
  `create_time`  datetime    DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`  datetime    DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='计划书被保人信息表';

CREATE TABLE `jhs_insured_product`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `jhs_id`         int(11)     NOT NULL COMMENT '计划书id',
  `jhs_insured_id` int(11)        DEFAULT NULL COMMENT '计划书被保人表ID',
  `plat_code`      varchar(32)    DEFAULT NULL COMMENT '公司平台的产品编码',
  `plat_plan_code` varchar(32)    DEFAULT NULL COMMENT '公司平台的产品计划编码',
  `plat_risk_code` varchar(512)   DEFAULT NULL COMMENT '公司平台的多个险种编码组合，按Comparator规则排序，英文逗号分隔；',
  `product_name`   varchar(64) NOT NULL COMMENT '产品名称',
  `period_flag`    tinyint(2)     DEFAULT NULL COMMENT '长短险标识：0--长险；1--短险；',
  `pay_period`     varchar(8)     DEFAULT NULL COMMENT '缴费因子：缴费期间值+缴费期间单位：C--一次交清; M--月;Q--季度；Y--年; D--日；A--岁；',
  `insure_period`  varchar(8)     DEFAULT NULL COMMENT '保障因子：保障期间值+保障期间单位：M--月; Y--年; D -- 日；A--岁；O -- 终身；',
  `draw_period`    varchar(8)     DEFAULT NULL COMMENT '领取因子：领取时长值+领取时长单位：M--月；Q--季度；HY--半年；Y--年；A--岁',
  `draw_start_age` int(10)        DEFAULT NULL COMMENT '开始领取年龄',
  `prem`           decimal(10, 2) DEFAULT NULL COMMENT '保费',
  `amount`         decimal(10, 2) DEFAULT NULL COMMENT '保额',
  `trial_json`     text           DEFAULT NULL COMMENT 'json格式的试算保障方案，包含了用户选择的各个产品因子，要有险种',
  `duty_display`   text           DEFAULT NULL COMMENT '计划书责任结果展示，用于保存计算合并后的责任细节，避免再次计算',
  `create_time`    datetime       DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime       DEFAULT current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='计划书投保产品表';

CREATE TABLE `chl_base_info`
(
  `id`                 int(11)     NOT NULL AUTO_INCREMENT,
  `channel_no`         varchar(64) NOT NULL COMMENT '渠道编号',
  `full_name`          varchar(64) NOT NULL COMMENT '渠道全名称',
  `short_name`         varchar(32) NOT NULL COMMENT '渠道简称',
  `channel_type`       tinyint(2)   DEFAULT 0 COMMENT '渠道类型：0 -- 个人渠道；1 -- 合作渠道；2 -- 团队渠道；',
  `has_agreement_flag` tinyint(2)   DEFAULT 0 COMMENT '是否有协议：0 -- 否；1 -- 是；',
  `contact_man`        varchar(64) NOT NULL COMMENT '联系人',
  `contact_phone`      varchar(16) NOT NULL COMMENT '联系电话',
  `address_code`       varchar(8)  NOT NULL COMMENT '所属地区的地址编码',
  `address_detail`     varchar(256) DEFAULT NULL COMMENT '所属地区的详细地址',
  `bd_user_id`         int(11)     NULL COMMENT '市场BD的用户id',
  `channel_status`     tinyint(2)   DEFAULT 0 COMMENT '渠道状态：0 -- 待签约；1 -- 已签约；',
  `remark`             varchar(512) DEFAULT NULL COMMENT '备注',
  `create_time`        datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道表';

CREATE TABLE `chl_agreement`
(
  `id`               int(11)     NOT NULL AUTO_INCREMENT,
  `agreement_no`     varchar(64) NOT NULL COMMENT '协议编号',
  `old_agreement_no` varchar(64) NULL COMMENT '续签时的上个协议编号',
  `channel_no`       varchar(64) NOT NULL COMMENT '渠道编号',
  `full_name`        varchar(64) NOT NULL COMMENT '渠道全名称',
  `contract_date`    datetime    NULL COMMENT '签约日期',
  `start_date`       datetime    NULL COMMENT '生效日期',
  `end_date`         datetime    NULL COMMENT '结束日期',
  `vat`              varchar(64) NULL COMMENT '增值税：6%、3%、1%、无票、普票',
  `agreement_status` tinyint(2)   DEFAULT 0 COMMENT '合同状态：0 -- 有效；1 -- 即将到期；2 -- 已到期；',
  `operName`         varchar(8)  NULL COMMENT '操作人',
  `remark`           varchar(512) DEFAULT NULL COMMENT '合同备注',
  `create_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道协议表';

CREATE TABLE `file_info`
(
  `id`           int(11) NOT NULL AUTO_INCREMENT,
  `md5`          varchar(64) COMMENT '文件内容md5',
  `contentType`  varchar(128) COMMENT '内容类型',
  `display_name` varchar(256) COMMENT '文件展示名称',
  `file_size`    int COMMENT '文件字节大小',
  `file_path`    varchar(256) COMMENT '文件物理路径',
  `file_url`     varchar(1024) COMMENT '文件URL路径',
  `file_type`    int     NOT NULL COMMENT '文件类型：0 -- 渠道合同协议文件；',
  `relation_id`  int(11) NULL COMMENT '关联id，依赖于文件类型：0 -- 对应chl_agreement表ID；',
  `create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='文件信息';

CREATE TABLE `rel_chanel_product`
(
  `id`            int(11) NOT NULL AUTO_INCREMENT,
  `product_id`    int(11) NOT NULL COMMENT '产品ID',
  `channel_id`    int(11) NOT NULL COMMENT '渠道ID',
  `passageway_id` int(11) NOT NULL COMMENT '通道ID',
  `create_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道产品关联表';

CREATE TABLE `psw_base_info`
(
  `id`              int(11)     NOT NULL AUTO_INCREMENT,
  `passageway_no`   varchar(64) NOT NULL COMMENT '通道编号',
  `full_name`       varchar(64) NOT NULL COMMENT '通道名称',
  `short_name`      varchar(20) NOT NULL COMMENT '通道简称',
  `contact_man`     varchar(64) NOT NULL COMMENT '联系人',
  `contact_phone`   varchar(16) NOT NULL COMMENT '联系电话',
  `address_code`    varchar(8)   DEFAULT NULL COMMENT '地址编码',
  `address_detail`  varchar(256) DEFAULT NULL COMMENT '详细地址',
  `has_agreement`   tinyint(2)   DEFAULT 1 COMMENT '是否有协议：0 -- 否；1 -- 是；',
  `contract_status` tinyint(2)   DEFAULT 0 COMMENT '签约状态：0 -- 待签约；1 -- 已签约；2 -- 已解约；',
  `remark`          varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='通道基本信息表';

CREATE TABLE `psw_agreement`
(
  `id`               int(11)     NOT NULL AUTO_INCREMENT,
  `agreement_no`     varchar(64) NOT NULL COMMENT '协议编号',
  `old_agreement_no` varchar(64) NULL COMMENT '续签时的上个协议编号',
  `passageway_no`    varchar(64) NOT NULL COMMENT '通道编号',
  `full_name`        varchar(64) NOT NULL COMMENT '通道全名称',
  `contract_date`    datetime    NULL COMMENT '签约日期',
  `start_date`       datetime    NULL COMMENT '生效日期',
  `end_date`         datetime    NULL COMMENT '结束日期',
  `agreement_status` tinyint(2)   DEFAULT 0 COMMENT '合同状态：0 -- 有效；1 -- 即将到期；2 -- 已到期；',
  `operName`         varchar(8)  NULL COMMENT '维护人',
  `remark`           varchar(512) DEFAULT NULL COMMENT '合同备注',
  `create_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='通道协议表';

CREATE TABLE `rel_passageway_product`
(
  `id`            int(11) NOT NULL AUTO_INCREMENT,
  `product_id`    int(11) NOT NULL COMMENT '产品ID',
  `passageway_id` int(11) NOT NULL COMMENT '通道ID',
  `create_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='通道产品关联表';

CREATE TABLE `ptn_info`
(
  `id`               int(11)     NOT NULL AUTO_INCREMENT,
  `partner_no`       varchar(64) NOT NULL COMMENT '会号编号',
  `real_name`        varchar(64)  DEFAULT NULL COMMENT '姓名',
  `nick_name`        varchar(64)  DEFAULT NULL COMMENT '昵称',
  `gender`           tinyint(2)   DEFAULT NULL COMMENT '性别：0--男；1--女；2--未知；9--未说明；',
  `card_type`        varchar(8)   DEFAULT NULL COMMENT '证件类型：01 -- 身份证；02 -- 户口本；03 -- 驾照；04 -- 军人证（军官证）；05 -- 士兵证；10 -- 台胞证；11 -- 回乡证；12 -- 出生证；99 -- 其它；A -- 护照；B -- 学生证；C -- 工作证；D -- 无证件；E -- 临时身份证；F -- 警官证；',
  `card_no`          varchar(64)  DEFAULT NULL COMMENT '证件号码',
  `valid_date`       datetime     DEFAULT NULL COMMENT '证件有效开始日期',
  `invalid_date`     datetime     DEFAULT NULL COMMENT '证件有效终止日期',
  `real_auth_status` tinyint(2)   DEFAULT NULL COMMENT '实名认证状态：0--未认证；1--已认证；',
  `real_auth_time`   datetime     DEFAULT NULL COMMENT '实名认证时间',
  `birthday`         datetime     DEFAULT NULL COMMENT '生日',
  `mobile`           varchar(16)  DEFAULT NULL COMMENT '手机号码',
  `email`            varchar(64)  DEFAULT NULL COMMENT '电子邮箱',
  `address_code`     varchar(8)   DEFAULT NULL COMMENT '地址编码',
  `address_detail`   varchar(256) DEFAULT NULL COMMENT '详细地址',
  `education`        varchar(16)  DEFAULT NULL COMMENT '学历',
  `cert_auth_status` tinyint(2)   DEFAULT NULL COMMENT '执业证认证状态：0--未认证；1--已认证；',
  `cert_auth_time`   datetime     DEFAULT NULL COMMENT '执业证认证时间',
  `certificate_no`   varchar(50)  DEFAULT NULL COMMENT '执业证号',
  `cert_company`     varchar(100) DEFAULT NULL COMMENT '执业证所属单位',
  `cert_area`        varchar(50)  DEFAULT NULL COMMENT '执业区域',
  `user_name`        varchar(64)  DEFAULT NULL COMMENT '用户名',
  `password`         varchar(256) DEFAULT NULL COMMENT '哈希密码',
  `salt`             varchar(16)  DEFAULT '198303' COMMENT '加密盐值',
  `wechat`           varchar(64)  DEFAULT NULL COMMENT '微信号',
  `open_id`          varchar(64)  DEFAULT NULL COMMENT '微信OpenID',
  `alipay`           varchar(64)  DEFAULT NULL COMMENT '支付宝账号',
  `agent_no`         varchar(64)  DEFAULT NULL COMMENT '银保监会报备代理人工号',
  `initials`         varchar(2)   DEFAULT NULL COMMENT '姓名首字母',
  `create_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='业务员信息表';

CREATE TABLE `ptn_platform`
(
  `id`                int(11) NOT NULL AUTO_INCREMENT,
  `partner_id`        int(11) NOT NULL COMMENT '会员ID',
  `parent_partner_id` int(11)     DEFAULT NULL COMMENT '上级会员ID',
  `platform_type`     tinyint(3)  DEFAULT '0' COMMENT '平台类型：-1 -- 未定义；0 -- 宁康保会员；',
  `platform_name`     varchar(64) DEFAULT NULL COMMENT '平台名称',
  `channel_id`        int(11) NOT NULL COMMENT '渠道ID',
  `grade_name`        varchar(64) DEFAULT NULL COMMENT '职级名称，职级代表此人拥有的权益',
  `post_name`         varchar(64) DEFAULT NULL COMMENT '职务名称，职务代表此人做事的范围',
  `contract_status`   tinyint(2)  DEFAULT 0 COMMENT '签约状态：0 -- 待签约；1 -- 已签约；2 -- 已离职；',
  `create_time`       datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`       datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='业务员销售平台表';

CREATE TABLE `act_base_info`
(
  `id`               int(11)      NOT NULL AUTO_INCREMENT,
  `owner_type`       int(4)         DEFAULT 0 COMMENT '账户拥有者类型：0 -- 宁康保会员账户；',
  `owner_id`         int(11)      NOT NULL COMMENT '账户拥有者ID，依赖owner_type值：owner_type为0 -- 业务员销售平台表ID；',
  `account_type`     int(4)         DEFAULT 0 COMMENT '账户类型：0 -- 保单结算类账户;',
  `balance`          decimal(14, 2) DEFAULT '0.00' COMMENT '账户余额（单位--元）',
  `account_name`     varchar(64)    DEFAULT NULL COMMENT '银行账户名称',
  `bank_account`     varchar(64)    DEFAULT NULL COMMENT '银行卡号',
  `bank_name`        varchar(64)    DEFAULT NULL COMMENT '银行名称',
  `bank_branch_name` varchar(256)   DEFAULT NULL COMMENT '银行开户行支行名称',
  `remark`           varchar(512) NULL COMMENT '备注',
  `create_time`      datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='账户基本信息表';

CREATE TABLE `act_detail`
(
  `id`          int(11)        NOT NULL AUTO_INCREMENT,
  `account_id`  int(11)        NOT NULL COMMENT '账户ID',
  `biz_type`    int(11)        NOT NULL COMMENT '外部业务类型 :{1--佣金:[100--会员出单佣金；]}; ',
  `biz_id`      varchar(64)    NOT NULL COMMENT '外部业务ID，对应外部业务类型：100--保单号；',
  `amount`      decimal(14, 2) NOT NULL COMMENT '流水发生额',
  `direction`   tinyint(2)     NOT NULL COMMENT '进出账户方向：1--进;2--出;',
  `balance`     decimal(14, 2) NOT NULL COMMENT '交易发生后账户余额',
  `remark`      varchar(512)   NOT NULL COMMENT '交易事项摘要备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='账户明细表';

CREATE TABLE `bal_channel`
(
  `id`            int(11)     NOT NULL AUTO_INCREMENT,
  `channel_id`    int(11)     NOT NULL COMMENT '渠道ID',
  `channel_no`    varchar(64) NOT NULL COMMENT '渠道编号',
  `basiclaw_no`   varchar(64) NOT NULL COMMENT '基本法编号',
  `basiclaw_name` varchar(64) DEFAULT NULL COMMENT '基本法名称',
  `settle_mode`   varchar(4)  DEFAULT NULL COMMENT '结算模式：M--月；S--季度；HY--半年；Y--年；',
  `create_time`   datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道基本法表';

CREATE TABLE `bal_grade`
(
  `id`          int(11)     NOT NULL AUTO_INCREMENT,
  `basiclaw_id` int(11)     NOT NULL COMMENT '渠道基本法表ID',
  `grade_no`    varchar(64) NOT NULL COMMENT '职级编号',
  `grade_value` int(11)     DEFAULT NULL COMMENT '职级值',
  `grade_name`  varchar(64) DEFAULT NULL COMMENT '职级名称，职级代表此人拥有的权益',
  `post_name`   varchar(64) DEFAULT NULL COMMENT '职务名称，职务代表此人做事的范围',
  `create_time` datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道基本法职级表';

CREATE TABLE `bal_fee_rate`
(
  `id`               int(11) NOT NULL AUTO_INCREMENT,
  `grade_id`         int(11) NOT NULL DEFAULT 0 COMMENT '渠道基本法职级表ID',
  `index_type`       tinyint(2)       DEFAULT 0 COMMENT '指标类型：0 -- 标保达到指标值；1 -- 保费达到指标值；2 -- 保单件数达到指标值；',
  `settle_formula`   tinyint(2)       DEFAULT 0 COMMENT '结算公式：0 -- 结算金额=标保*佣金费率；1 -- 结算金额=保费*佣金费率；2 -- 结算金额=保单件数*单件奖金；',
  `min_index_value`  decimal(14, 4)   DEFAULT '0.0000' COMMENT '指标值最小值',
  `max_index_value`  decimal(14, 4)   DEFAULT '0.0000' COMMENT '指标值最大值',
  `index_rate_value` decimal(14, 4)   DEFAULT '0.0000' COMMENT '指标对应费率',
  `index_bonus`      decimal(14, 4)   DEFAULT '0.0000' COMMENT '指标奖金（单位--元/指标单位）',
  `batch_no`         bigint(63)       DEFAULT NULL COMMENT '批次号，生成规则：年月日时分秒',
  `create_time`      datetime         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='渠道基本法费率表';

CREATE TABLE `stl_partner_fee`
(
  `id`               int(11)     NOT NULL AUTO_INCREMENT,
  `partner_id`       int(11)     NOT NULL COMMENT '会员基本信息表ID',
  `basiclaw_id`      int(11)     NOT NULL COMMENT '渠道基本法表ID',
  `basiclaw_no`      varchar(64) NOT NULL COMMENT '基本法编号',
  `grade_id`         int(11)     NOT NULL DEFAULT '0' COMMENT '渠道基本法职级表ID',
  `grade_no`         varchar(64) NOT NULL COMMENT '职级编号',
  `basiclaw_rate_id` int(11)     NOT NULL COMMENT '基本法费率表ID',
  `index_value`      decimal(14, 2)       DEFAULT '0.00' COMMENT '本次指标值',
  `index_rate_value` decimal(14, 4)       DEFAULT '0.0000' COMMENT '本次佣金费率',
  `index_bonus`      decimal(14, 4)       DEFAULT '0.0000' COMMENT '本次指标奖金（单位--元/指标单位）',
  `settle_fee`       decimal(14, 2)       DEFAULT '0.00' COMMENT '结算费用',
  `actual_fee`       decimal(14, 2)       DEFAULT '0.00' COMMENT '实际结算费用',
  `has_settled`      tinyint(2)           DEFAULT NULL COMMENT '是否已结算？ 0 -- 未结算；1 -- 已结算',
  `settled_time`     datetime             DEFAULT NULL COMMENT '已结算时间',
  `has_paid`         tinyint(2)           DEFAULT NULL COMMENT '是否已付款？ 0 -- 未付款；1 -- 已结付款',
  `paid_time`        datetime             DEFAULT NULL COMMENT '已结付款时间',
  `calc_from_time`   datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '计算开始时间',
  `calc_to_time`     datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '计算结束时间',
  `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='基本法会员结算记录表';

CREATE TABLE `rel_partnerfee_policy`
(
  `id`            int(11)      NOT NULL AUTO_INCREMENT,
  `partnerfee_id` int(11)      NOT NULL COMMENT '基本法会员结算记录表ID',
  `policy_id`     int(11)      NOT NULL COMMENT '保单ID',
  `contract_no`   varchar(128) NOT NULL COMMENT '保单号',
  `partner_id`    int(11)      NOT NULL COMMENT '会员基本信息表ID',
  `channel_id`    int(11)      NOT NULL COMMENT '所属渠道商ID',
  `create_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='会员佣金结算保单关联表';

-- 缴费期限用区间[min_payment_period,max_payment_period]表示，如果是常数比如5年缴费期限则用区间[5,5]表示；
-- 如果产品没有折标系数，则默认是1.0计算，即不折标使用原保费；短险通常不折标；
CREATE TABLE `pro_stdprem_factor`
(
  `id`             int(11)    NOT NULL AUTO_INCREMENT,
  `product_id`     int(11)    NOT NULL COMMENT '产品ID',
  `risk_id`        int(11)             DEFAULT NULL COMMENT '险种ID',
  `obj_type`       tinyint(4) NOT NULL DEFAULT 0 COMMENT '折标对象类型：0--未指定；1--上游通道给的折标标准；2--给下游渠道的通用折标标准；3--给指定下游渠道的折标标准；',
  `obj_id`         int(11)             DEFAULT NULL COMMENT '对象ID，依赖折标对象类型存储：类型0--NULL；类型1--通道ID；类型2--NULL；类型3--渠道ID；',
  `min_pay_period` int(11)             DEFAULT NULL COMMENT '最小缴费期限（单位--年）',
  `max_pay_period` int(11)             DEFAULT NULL COMMENT '最大缴费期限（单位--年）',
  `stdprem_factor` decimal(14, 4)      DEFAULT '1.0000' COMMENT '标保折标系数：100%--1.0；75%--0.75；50%--0.5',
  `operName`       varchar(8) NULL COMMENT '维护人',
  `batch_no`       bigint(63)          DEFAULT NULL COMMENT '批次号，生成规则：年月日时分秒',
  `remark`         varchar(512)        DEFAULT NULL COMMENT '备注',
  `create_time`    datetime            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    datetime            DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='标准保费折标系数表';

-- 兼容通道、渠道的结算模式
-- 保单的结算费率，使用[保单投保时间]对应于时间区间 -- [生效开始时间,生效截止时间]
-- 结算金额=规模保费*基础结算费率
CREATE TABLE `stl_normal_rule`
(
  `id`                 int(11)                     NOT NULL AUTO_INCREMENT,
  `obj_type`           tinyint(2)     DEFAULT NULL COMMENT '结算对象类型：0--通道；1--渠道',
  `obj_id`             int(11)        DEFAULT NULL COMMENT '对象ID，依赖结算对象类型存储：类型0--通道ID，如果为NULL表示所有通道；类型1--渠道ID，如果为NULL表示所有渠道；',
  `product_id`         int(11)        DEFAULT NULL COMMENT '产品ID',
  `plan_id`            int(11)        DEFAULT NULL COMMENT '产品计划ID',
  `risk_id`            int(11)        DEFAULT NULL COMMENT '险种ID',
  `valid_date`         datetime                    NOT NULL COMMENT '生效开始时间',
  `invalid_date`       datetime                    NOT NULL COMMENT '生效截止时间',
  `personal_or_family` tinyint(2)     DEFAULT NULL COMMENT '保单是个人单还是家庭单：1--个人；2--家庭；',
  `year_no`            int(11)        DEFAULT NULL COMMENT '保单年度',
  `pay_intv`           varchar(24)    DEFAULT NULL COMMENT '缴费频次：0--一次交清；1--月交；3--季交；6--半年交；12--年交',
  `pay_period`         int(4)         DEFAULT NULL COMMENT '缴费期间',
  `pay_period_unit`    varchar(24)    DEFAULT NULL NULL COMMENT '缴费期间单位：M--月；S--季度；HY--半年；Y--年；A--缴至',
  `insure_period`      int(10)        DEFAULT NULL COMMENT '保障期间',
  `insure_period_unit` varchar(24)    DEFAULT NULL COMMENT '保障期间单位：M--月；S--季度；HY--半年；Y--年；A--保至',
  `draw_period`        int(10)        DEFAULT NULL COMMENT '领取时长',
  `draw_period_unit`   varchar(24)    DEFAULT NULL COMMENT '领取年限单位：M--月；S--季度；HY--半年；Y--年；A--领取至',
  `draw_start_age`     int(10)        DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`  tinyint(2)     DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',
  `base_rate`          decimal(14, 4) DEFAULT '0.0000' COMMENT '基础结算费率',
  `settle_time`        datetime       DEFAULT NULL COMMENT '结算时间/结算周期',
  `settle_deadline`    datetime       DEFAULT NULL COMMENT '结算有效截止时间',
  `batch_no`           bigint(63)     DEFAULT NULL COMMENT '批次号，生成规则：年月日时分秒',
  `create_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='结算基础费率规则表';

CREATE TABLE `stl_activity_rule`
(
  `id`                 int(11)                     NOT NULL AUTO_INCREMENT,
  `activity_id`        int(11)        DEFAULT NULL COMMENT '活动表ID',
  `product_id`         int(11)        DEFAULT NULL COMMENT '产品ID',
  `plan_id`            int(11)        DEFAULT NULL COMMENT '产品计划ID',
  `risk_id`            int(11)        DEFAULT NULL COMMENT '险种ID',
  `personal_or_family` tinyint(2)     DEFAULT NULL COMMENT '保单是个人单还是家庭单：1--个人；2--家庭；',
  `year_no`            int(11)        DEFAULT NULL COMMENT '保单年度',
  `pay_intv`           varchar(24)    DEFAULT NULL COMMENT '缴费频次：0--一次交清；1--月交；3--季交；6--半年交；12--年交',
  `pay_period`         int(4)         DEFAULT NULL COMMENT '缴费期间',
  `pay_period_unit`    varchar(24)    DEFAULT NULL NULL COMMENT '缴费期间单位：M--月；S--季度；HY--半年；Y--年；A--缴至',
  `insure_period`      int(10)        DEFAULT NULL COMMENT '保障期间',
  `insure_period_unit` varchar(24)    DEFAULT NULL COMMENT '保障期间单位：M--月；S--季度；HY--半年；Y--年；A--保至',
  `draw_period`        int(10)        DEFAULT NULL COMMENT '领取时长',
  `draw_period_unit`   varchar(24)    DEFAULT NULL COMMENT '领取年限单位：M--月；S--季度；HY--半年；Y--年；A--领取至',
  `draw_start_age`     int(10)        DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`  tinyint(2)     DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',
  `index_type`         tinyint(2)     DEFAULT 0 COMMENT '指标类型：0 -- 固定活动费率；1 -- 标保达到指标值；2 -- 保费达到指标值；3 -- 保单件数达到指标值；',
  `min_index_value`    decimal(14, 4) DEFAULT '0.0000' COMMENT '指标值最小值',
  `max_index_value`    decimal(14, 4) DEFAULT '0.0000' COMMENT '指标值最大值',
  `index_rate_value`   decimal(14, 4) DEFAULT '0.0000' COMMENT '指标对应费率',
  `index_bonus`        decimal(14, 4) DEFAULT '0.0000' COMMENT '指标奖金（单位--元）',
  `rule_value`         int(11)        DEFAULT NULL COMMENT '产品因子规则值，相同规则值表示不同产品因子使用了相同的指标值',
  `batch_no`           bigint(63)     DEFAULT NULL COMMENT '批次号，生成规则：年月日时分秒',
  `create_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='结算活动费率规则表';

CREATE TABLE `stl_activity`
(
  `id`                int(11)  NOT NULL AUTO_INCREMENT,
  `activity_name`     varchar(128) DEFAULT NULL COMMENT '活动名称',
  `activity_code`     varchar(64)  DEFAULT NULL COMMENT '活动编码',
  `product_id`        int(11)      DEFAULT NULL COMMENT '产品ID',
  `start_date`        datetime NOT NULL COMMENT '开始时间',
  `end_date`          datetime NOT NULL COMMENT '结束时间',
  `settle_mode`       varchar(4)   DEFAULT NULL COMMENT '结算模式：M--月；S--季度；HY--半年；Y--年；O--一次性',
  `activity_deadline` datetime     DEFAULT NULL COMMENT '活动有效截止时间',
  `create_time`       datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`       datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='活动表';

CREATE TABLE `rel_activity_obj`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11)    DEFAULT NULL COMMENT '活动表ID',
  `obj_type`    tinyint(2) DEFAULT NULL COMMENT '结算对象类型：0--通道；1--渠道',
  `obj_id`      int(11)    DEFAULT NULL COMMENT '对象ID，依赖结算对象类型存储：类型0--通道ID，如果为NULL表示所有通道；类型1--渠道ID，如果为NULL表示所有渠道；',
  `create_time` datetime   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime   DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='活动对象关联表';

-- 费用调整率（财务逻辑：以下游提供6%专票的比率为基准，3%专票是它的97%，1%专票是它的95%，普票是它的94%，无票是它的92%）
-- 结算费用 = 期交保费*渠道推广费率*费用调整率
CREATE TABLE `stl_policy_fee`
(
  `id`               int(11)      NOT NULL AUTO_INCREMENT,
  `settle_type`      tinyint(2)   NOT NULL COMMENT '结算类型：0--跟通道结算；1--跟渠道结算；',
  `policy_id`        int(11)      NOT NULL COMMENT '保单ID',
  `contract_no`      varchar(128) NOT NULL COMMENT '保单号',
  `risk_code`        varchar(32)    DEFAULT NULL COMMENT '险种代码',
  `rate_type`        tinyint(2)   NOT NULL COMMENT '费率类型：0--基础费率；1--活动费率；',
  `settle_rule_id`   int(11)      NOT NULL COMMENT '结算规则表ID，依赖结算类型和费率类型存储：结算基础费率规则表、结算活动费率规则表的ID',
  `activity_id`      int(11)        DEFAULT NULL COMMENT '活动表ID',
  `year_no`          int(11)        DEFAULT NULL COMMENT '保单年度',
  `has_settled`      tinyint(2)     DEFAULT NULL COMMENT '是否已结算？ 0 -- 未结算；1 -- 已结算',
  `settled_time`     datetime       DEFAULT NULL COMMENT '已结算时间',
  `has_paid`         tinyint(2)     DEFAULT NULL COMMENT '是否已付款？ 0 -- 未付款；1 -- 已结付款',
  `paid_time`        datetime       DEFAULT NULL COMMENT '已结付款时间',
  `invoice_type`     tinyint(2)     DEFAULT NULL COMMENT '发票类型？ 0 -- 无票;1 -- 增值税普通发票；2 -- 增值税专用发票6%；2 -- 增值税专用发票3%；2 -- 增值税专用发票1%；',
  `index_value`      decimal(14, 2) DEFAULT '0.00' COMMENT '本次指标值',
  `index_rate_value` decimal(14, 4) DEFAULT '0.0000' COMMENT '本次费率',
  `adjusted_rate`    decimal(14, 4) DEFAULT '0.00' COMMENT '发票税率调整率',
  `index_bonus`      decimal(14, 4) DEFAULT '0.0000' COMMENT '本次指标奖金（单位--元/指标单位）',
  `settle_fee`       decimal(14, 2) DEFAULT '0.00' COMMENT '结算费用',
  `actual_fee`       decimal(14, 2) DEFAULT '0.00' COMMENT '实际结算费用',
  `create_time`      datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保单结算记录表';

CREATE TABLE `pol_base_info`
(
  `id`                 int(11)        NOT NULL AUTO_INCREMENT,
  `order_id`           int(11)        NOT NULL COMMENT '订单ID',
  `contract_no`        varchar(128)   NOT NULL COMMENT '保单号',
  `proposal_no`        varchar(128)            DEFAULT NULL COMMENT '投保单号，核保时保险公司生成',
  `total_prem`         decimal(14, 2) NOT NULL DEFAULT 0.00 COMMENT '总保费（单位--元）',
  `stdprem_factor`     decimal(14, 4)          DEFAULT '1.0000' COMMENT '标保折标系数：100%--1.0；75%--0.75',
  `stdprem`            decimal(14, 2)          DEFAULT '0.00' COMMENT '标准保费',
  `total_amount`       decimal(14, 2)          DEFAULT NULL DEFAULT 0.00 COMMENT '总保额（单位--元）',
  `insured_date`       datetime       NOT NULL COMMENT '投保日期',
  `valid_date`         datetime       NOT NULL COMMENT '保单生效日期',
  `invalid_date`       datetime                DEFAULT NULL COMMENT '保单终止日期',
  `partner_id`         int(11)        NOT NULL COMMENT '会员基本信息表ID',
  `channel_id`         int(11)        NOT NULL COMMENT '渠道ID',
  `passageway_id`      int(11)        NOT NULL COMMENT '通道ID',
  `product_id`         int(11)        NOT NULL COMMENT '产品ID',
  `plan_id`            int(11)                 DEFAULT NULL COMMENT '产品计划ID',
  `period_flag`        tinyint(2)              DEFAULT NULL COMMENT '长短险标识：0--长险；1--短险；',
  `copies`             int(5)         NOT NULL DEFAULT 1 COMMENT '购买份数',
  `main_flag`          tinyint(5)     NOT NULL DEFAULT 1 COMMENT '保单主次标识：1 -- 主保单；2 -- 次保单；',

  `personal_or_family` tinyint(2)              DEFAULT NULL COMMENT '保单是个人单还是家庭单：1--个人；2--家庭；',
  `pay_intv`           varchar(24)             DEFAULT NULL COMMENT '缴费频次：0--一次交清；1--月交；3--季交；6--半年交；12--年交',
  `pay_period`         int(4)                  DEFAULT NULL COMMENT '缴费期间',
  `pay_period_unit`    varchar(24)             DEFAULT NULL COMMENT '缴费期间单位：M--月；S--季度；HY--半年；Y--年；A--缴至',
  `insure_period`      int(10)                 DEFAULT NULL COMMENT '保障期间',
  `insure_period_unit` varchar(24)             DEFAULT NULL COMMENT '保障期间单位：M--月；S--季度；HY--半年；Y--年；A--保至',
  `draw_period`        int(10)                 DEFAULT NULL COMMENT '领取时长',
  `draw_period_unit`   varchar(24)             DEFAULT NULL COMMENT '领取年限单位：M--月；S--季度；HY--半年；Y--年；A--领取至',
  `draw_start_age`     int(10)                 DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`  tinyint(2)              DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',

  `appnt_name`         varchar(128)            DEFAULT NULL COMMENT '投保人姓名',
  `insured_names`      varchar(512)            DEFAULT NULL COMMENT '被保人姓名列表，英文逗号分隔',
  `is_legal_bnf`       tinyint(2)              DEFAULT NULL COMMENT '受益人类型：0--法定受益人；1--指定受益人；',
  `create_time`        datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保单基本信息表';

CREATE TABLE `pol_appnt`
(
  `id`              int NOT NULL AUTO_INCREMENT COMMENT '序号',
  `policy_id`       int          DEFAULT NULL COMMENT '保单ID',
  `name`            varchar(20)  DEFAULT NULL COMMENT '投保人姓名',
  `gender`          char(2)      DEFAULT NULL COMMENT '投保人性别：0男1女',
  `birthday`        varchar(10)  DEFAULT NULL COMMENT '投保人生日：yyyy-MM-dd',
  `card_type`       char(4)      DEFAULT NULL COMMENT '投保人证件类型 01 -- 身份证；02 -- 户口本；03 -- 驾照；04 -- 军人证（军官证）；05 -- 士兵证；10 -- 台胞证；11 -- 回乡证；12 -- 出生证；99 -- 其它；A -- 护照；B -- 学生证；C -- 工作证；D -- 无证件；E -- 临时身份证；F -- 警官证；',
  `card_no`         varchar(20)  DEFAULT NULL COMMENT '投保人身份证号码',
  `val_start_date`  varchar(10)  DEFAULT NULL COMMENT '证件有效期开始时间：yyyy-MM-dd',
  `val_end_date`    varchar(10)  DEFAULT NULL COMMENT '证件有效期截至时间：yyyy-MM-dd',
  `nationality`     varchar(10)  DEFAULT NULL COMMENT '国籍，代码表CHN',
  `occupation_code` varchar(10)  DEFAULT NULL COMMENT '职业代码',
  `occupation_name` varchar(10)  DEFAULT NULL COMMENT '职业名称',
  `mobile`          varchar(11)  DEFAULT NULL COMMENT '手机号码',
  `email`           varchar(60)  DEFAULT NULL COMMENT '电子邮件',
  `address_detail`  varchar(150) DEFAULT NULL COMMENT '投保人详细地址',
  `post_code`       varchar(10)
) DEFAULT NULL COMMENT '邮政编码',
  `annual_income` decimal(14,2) DEFAULT NULL COMMENT '投保人年收入',
  `height` int DEFAULT NULL COMMENT '身高，单位厘米？',
  `weight` int DEFAULT NULL COMMENT '体重，单位千克',
  `social_insurance_flag` tinyint(2) DEFAULT 0 COMMENT '是否有社保：0--有社保；1--无社保；',
  `tax_resident_type` varchar(2)  DEFAULT NULL COMMENT 'A：中国税收居民身份B：既是中国税收居民，又是其他税收管辖区居民C：非中国税收居民',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT=DYNAMIC COMMENT='投保人信息表';

CREATE TABLE `pol_insured`
(
  `id`                    int NOT NULL AUTO_INCREMENT COMMENT '序号',
  `policy_id`             int            DEFAULT NULL COMMENT '保单ID',
  `name`                  varchar(20)    DEFAULT NULL COMMENT '被保人姓名',
  `gender`                char(2)        DEFAULT NULL COMMENT '被保人性别：0男1女',
  `birthday`              varchar(10)    DEFAULT NULL COMMENT '被保人生日：yyyy-MM-dd',
  `card_type`             char(4)        DEFAULT NULL COMMENT '被保人证件类型：01 -- 身份证；02 -- 户口本；03 -- 驾照；04 -- 军人证（军官证）；05 -- 士兵证；10 -- 台胞证；11 -- 回乡证；12 -- 出生证；99 -- 其它；A -- 护照；B -- 学生证；C -- 工作证；D -- 无证件；E -- 临时身份证；F -- 警官证；',
  `card_no`               varchar(20)    DEFAULT NULL COMMENT '被保人身份证号码',
  `val_start_date`        varchar(10)    DEFAULT NULL COMMENT '证件有效期开始时间：yyyy-MM-dd',
  `val_end_date`          varchar(10)    DEFAULT NULL COMMENT '证件有效期截至时间：yyyy-MM-dd',
  `nationality`           varchar(10)    DEFAULT NULL COMMENT '国籍，代码表CHN',
  `occupation_code`       varchar(10)    DEFAULT NULL COMMENT '职业代码',
  `occupation_name`       varchar(10)    DEFAULT NULL COMMENT '职业名称',
  `mobile`                varchar(11)    DEFAULT NULL COMMENT '手机号码',
  `email`                 varchar(60)    DEFAULT NULL COMMENT '电子邮件',
  `address_detail`        varchar(150)   DEFAULT NULL COMMENT '被保人详细地址',
  `height`                int            DEFAULT NULL COMMENT '身高，单位厘米？',
  `weight`                int            DEFAULT NULL COMMENT '体重，单位千克',
  `post_code`             varchar(6)     DEFAULT NULL COMMENT '邮政编码',
  `incomes`               decimal(14, 2) DEFAULT NULL COMMENT '被保人年收入',
  `social_insurance_flag` tinyint(2)     DEFAULT 0 COMMENT '是否有社保：0--有社保；1--无社保；',
  `tax_resident_type`     varchar(2)     DEFAULT NULL COMMENT 'A：中国税收居民身份B：既是中国税收居民，又是其他税收管辖区居民C：非中国税收居民',
  `relation_to_app`       varchar(2)     DEFAULT NULL COMMENT '被保人与投保人的关系：00本人；01配偶；02子女；03父母',
  `create_time`           datetime       DEFAULT NULL COMMENT '创建时间',
  `update_time`           datetime       DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='被保人信息表';

CREATE TABLE `pol_bnf`
(
  `id`                  int NOT NULL AUTO_INCREMENT,
  `policy_id`           int            DEFAULT NULL COMMENT '保单ID',
  `relation_to_insured` char(20)       DEFAULT NULL COMMENT '受益人与被保人关系00本人；01配偶；02子女；03父母',
  `name`                varchar(20)    DEFAULT NULL COMMENT '受益人姓名',
  `gender`              char(2)        DEFAULT NULL COMMENT '受益人性别：0男1女',
  `birthday`            varchar(10)    DEFAULT NULL COMMENT '受益人生日：yyyy-MM-dd',
  `card_type`           char(4)        DEFAULT NULL COMMENT '受益人证件类型',
  `card_no`             varchar(20)    DEFAULT NULL COMMENT '受益人身份证号码',
  `val_start_date`      varchar(10)    DEFAULT NULL COMMENT '受益人证件有效起期',
  `val_end_date`        varchar(10)    DEFAULT NULL COMMENT '受益人证件有效止期',
  `mobile`              varchar(11)    DEFAULT NULL COMMENT '手机号码',
  `bnf_rate`            decimal(10, 2) DEFAULT NULL COMMENT '受益人收益比例',
  `bnf_order`           int            DEFAULT NULL COMMENT '指定受益人时必填',
  `belong_to_insured`   varchar(10)    DEFAULT NULL COMMENT '所属被保人',
  `create_time`         datetime       DEFAULT NULL COMMENT '创建时间',
  `update_time`         datetime       DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='受益人信息表';


-- 1个保单<-->[1个产品 + 1个计划代码 + n个(险种代码<-->责任代码)]；
CREATE TABLE `pol_risk_info`
(
  `id`                 int(11)        NOT NULL AUTO_INCREMENT,
  `policy_id`          int(11)        NOT NULL COMMENT '保单ID',
  `contract_no`        varchar(128)   NOT NULL COMMENT '保单号',
  `product_id`         int(11)        NOT NULL COMMENT '产品ID',
  `code`               varchar(32)    NOT NULL COMMENT '保司产品代码',
  `plan_id`            int(11)                 DEFAULT NULL COMMENT '产品计划ID',
  `plat_plan_code`     varchar(32)             DEFAULT NULL COMMENT '产品计划编码',
  `risk_id`            int(11)                 DEFAULT NULL COMMENT '险种ID',
  `plat_risk_code`     varchar(32)    NOT NULL COMMENT '公司平台的险种编码',
  `main_flag`          tinyint(2)     NOT NULL COMMENT '基本险标识：0 -- 主险；1 -- 附加险；2 -- 附加主险；',
  `total_prem`         decimal(14, 2) NOT NULL DEFAULT 0.00 COMMENT '总保费（单位--元）',
  `total_amount`       decimal(14, 2)          DEFAULT NULL DEFAULT 0.00 COMMENT '总保额（单位--元）',

  `personal_or_family` tinyint(2)              DEFAULT NULL COMMENT '保单是个人单还是家庭单：1--个人；2--家庭；',
  `pay_intv`           varchar(24)             DEFAULT NULL COMMENT '缴费频次：0--一次交清；1--月交；3--季交；6--半年交；12--年交',
  `pay_period`         int(4)                  DEFAULT NULL COMMENT '缴费期间',
  `pay_period_unit`    varchar(24)             DEFAULT NULL COMMENT '缴费期间单位：M--月；S--季度；HY--半年；Y--年；A--缴至',
  `insure_period`      int(10)                 DEFAULT NULL COMMENT '保障期间',
  `insure_period_unit` varchar(24)             DEFAULT NULL COMMENT '保障期间单位：M--月；S--季度；HY--半年；Y--年；A--保至',
  `draw_period`        int(10)                 DEFAULT NULL COMMENT '领取时长',
  `draw_period_unit`   varchar(24)             DEFAULT NULL COMMENT '领取年限单位：M--月；S--季度；HY--半年；Y--年；A--领取至',
  `draw_start_age`     int(10)                 DEFAULT NULL COMMENT '开始领取年龄',
  `advanced_age_flag`  tinyint(2)              DEFAULT NULL COMMENT '是否含高龄：0--不含高龄；1--含高龄',

  `create_time`        datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保单投保险种表';

CREATE TABLE `pol_status`
(
  `id`                 int(11)      NOT NULL AUTO_INCREMENT,
  `policy_id`          int(11)      NOT NULL COMMENT '保单ID',
  `contract_no`        varchar(128) NOT NULL COMMENT '保单号',
  `hesitation_status`  tinyint(2)   NOT NULL DEFAULT -1 COMMENT '保单犹豫状态：-1 -- 未知； 0 -- 保留；1 -- 承保未过犹； 2 -- 承保已过犹；',
  `observation_period` int(11)      NOT NULL DEFAULT 90 COMMENT '观察等待期',
  `hesitation_period`  int(11)      NOT NULL DEFAULT 15 COMMENT '犹豫期',
  `hesitation_date`    datetime     NOT NULL COMMENT '过犹日期',
  `surrender_status`   tinyint(2)   NOT NULL DEFAULT -1 COMMENT '退保状态：-1 -- 未知； 0 -- 未退保；1 -- 犹退；2 -- 过犹退保；3--犹退审核中；',
  `surrender_date`     datetime              DEFAULT NULL COMMENT '退保日期',
  `receipt_status`     tinyint(2)   NOT NULL DEFAULT -1 COMMENT '回执标识：-1 -- 未知； 0 -- 未回执；1 -- 回执成功；2 -- 回执失败；',
  `receipt_time`       datetime              DEFAULT NULL COMMENT '回执时间',
  `visit_status`       tinyint(2)   NOT NULL DEFAULT -1 COMMENT '回访标识：-1 -- 未知； 0 -- 未回访；1 -- 回访成功；2 -- 回访失败；',
  `visit_time`         datetime              DEFAULT NULL COMMENT '回访时间',
  `preservation_type`  varchar(10)  NOT NULL DEFAULT -1 COMMENT '保全类型：-1 -- 未保全； 0 -- 全部退保费；1 -- 部分退保费；2 -- 不涉及退保费；',
  `edor_vali_date`     datetime              DEFAULT NULL COMMENT '保全日期',
  `total_wt_prem`      decimal(14, 2)        DEFAULT 0.00 COMMENT '保全总退保费',
  `create_time`        datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='保单状态表';

CREATE TABLE `pol_renewal_record`
(
  `id`               int(11)      NOT NULL AUTO_INCREMENT COMMENT '序号',
  `new_policy_id`    int(11)      NOT NULL COMMENT '新保单ID',
  `new_policy_no`    varchar(128) NOT NULL COMMENT '新保单号',
  `old_policy_id`    int(11)        DEFAULT NULL COMMENT '历史保单id',
  `old_policy_no`    varchar(128)   DEFAULT NULL COMMENT '历史保单号',
  `partner_id`       int(11)      NOT NULL COMMENT '会员基本信息表ID',
  `product_id`       int(11)      NOT NULL COMMENT '产品id',
  `plan_id`          int(11)        DEFAULT NULL COMMENT '产品计划ID',
  `period_flag`      tinyint(2)     DEFAULT NULL COMMENT '长短险标识：0--长险；1--短险；',
  `customer_id`      int(11)        DEFAULT NULL COMMENT '投保人ID',
  `appnt_name`       varchar(32)    DEFAULT NULL COMMENT '投保人姓名',
  `should_prem`      decimal(14, 2) DEFAULT 0.00 COMMENT '应续费金额',
  `actual_prem`      decimal(14, 2) DEFAULT 0.00 COMMENT '实收保费',
  `cvali_date`       datetime     NOT NULL COMMENT '生效时间',
  `all_renewal`      int(11)      NOT NULL COMMENT '总共期数',
  `now_renewal`      int(11)      NOT NULL COMMENT '当前期数',
  `should_pay_time`  datetime       DEFAULT NULL COMMENT '应缴纳时间',
  `actual_pay_time`  datetime       DEFAULT NULL COMMENT '实际缴纳时间',
  `renewal_status`   tinyint(2)     DEFAULT NULL COMMENT '续期续保状态：0--待续期续保；1--已续期续保；2--已失效；',
  `expire_time`      datetime       DEFAULT NULL COMMENT '到期时间',
  `up_year_end_time` datetime       DEFAULT NULL COMMENT '上一年度保单中止日期',
  `renewal_way`      int(4)         DEFAULT NULL COMMENT '续保方式：0 -- 续保-渠道；1 -- 续保-微信；2 -- 续保-线下；',
  `renewal_time`     datetime       DEFAULT NULL COMMENT '续保日期',
  `grace_days`       int(11)        DEFAULT NULL COMMENT '宽限天数',
  `create_time`      datetime       DEFAULT NULL COMMENT '创建时间',
  `update_time`      datetime       DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='续期续保记录表';

CREATE TABLE `ord_base_info`
(
  `id`                  int(11)     NOT NULL AUTO_INCREMENT,
  `order_no`            varchar(32) NOT NULL COMMENT '订单号',
  `product_id`          int(11)     NOT NULL COMMENT '产品ID',
  `passageway_id`       int(11)     NOT NULL COMMENT '通道ID',
  `channel_id`          int(11)     NOT NULL COMMENT '渠道ID',
  `partner_id`          int(11)     NOT NULL COMMENT '会员ID',
  `order_type`          tinyint(2)  NOT NULL COMMENT '订单类型:',
  `order_amount`        decimal(14, 2) DEFAULT NULL COMMENT '订单金额(单位：元)',
  `paid_amount`         decimal(14, 2) DEFAULT NULL COMMENT '实付金额(单位：元)',
  `total_refund_amount` decimal(14, 2) DEFAULT NULL COMMENT '总退费金额(单位：元)',
  `order_status`        varchar(18)    DEFAULT NULL COMMENT '订单状态：0 -- 待出单；1 -- 已出单；2 -- 订单取消；',
  `delivery_mode`       varchar(18)    DEFAULT NULL COMMENT '出单方式：0 -- 线上出单；1 -- 线下出单；',
  `pay_id`              int(11)     NOT NULL COMMENT '支付ID',
  `order_sources`       varchar(18)    DEFAULT NULL COMMENT '订单来源：0 -- 宁康保；1 -- 健康产品平台；',
  `create_time`         datetime       DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`         datetime       DEFAULT current_timestamp COMMENT '更新时间',
  primary key (`id`)
) ROW_FORMAT = DYNAMIC COMMENT = '订单基本信息表';

CREATE TABLE `ord_payment`
(
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `order_id`       int(11)     NOT NULL COMMENT '订单ID',
  `pay_no`         varchar(64) NOT NULL COMMENT '支付单号',
  `pay_time`       datetime     DEFAULT current_timestamp COMMENT '支付时间',
  `pay_status`     varchar(18)  DEFAULT NULL COMMENT '支付状态：0 -- 待支付；1 -- 已支付；2 -- 待退费；3 -- 已退费；',
  `pay_method`     varchar(18)  DEFAULT NULL COMMENT '支付方式：0 -- 银行卡支付；1 -- 微信支付；2 -- 支付宝支付；',
  `account_name`   varchar(64)  DEFAULT NULL COMMENT '支付方银行账户名称',
  `bank_account`   varchar(64)  DEFAULT NULL COMMENT '支付方银行卡号',
  `bank_name`      varchar(64)  DEFAULT NULL COMMENT '支付方银行名称',
  `subbranch_name` varchar(256) DEFAULT NULL COMMENT '支付方银行开户行支行名称',
  `create_time`    datetime     DEFAULT current_timestamp COMMENT '创建时间',
  `update_time`    datetime     DEFAULT current_timestamp COMMENT '更新时间',
  primary key (`id`)
) ROW_FORMAT = DYNAMIC COMMENT = '订单支付信息表';


CREATE TABLE `pmt_inner_msg`
(
  `id`          int(11)      NOT NULL AUTO_INCREMENT,
  `title`       varchar(128) NOT NULL COMMENT '消息标题',
  `content`     text         NOT NULL COMMENT '消息内容',
  `msg_type`    varchar(10)  NOT NULL COMMENT '消息类型',
  `create_time` datetime     NOT NULL COMMENT '创建时间',
  `send_count`  int(11) DEFAULT '0' COMMENT '发送人数',
  `send_id`     int(11)      NOT NULL COMMENT '发送者id',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='站内信内容表';

CREATE TABLE `pmt_inner_msg_send`
(
  `id`           int(11)      NOT NULL AUTO_INCREMENT,
  `rec_id`       int(11)      NOT NULL COMMENT '接收者id',
  `title`        varchar(128) NOT NULL COMMENT '标题',
  `inner_msg_id` int(11)      NOT NULL COMMENT 'pmt_inner_msg表id',
  `send_id`      int(11)      NOT NULL COMMENT '发送者id',
  `platform`     tinyint(2) DEFAULT NULL COMMENT '平台1宁康保 ',
  `status`       tinyint(4) DEFAULT '0' COMMENT '0未读，1已读',
  `create_time`  datetime     NOT NULL,
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='站内信';

CREATE TABLE `pmt_advertisement_info`
(
  `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `pic_url`      varchar(200) DEFAULT NULL COMMENT '展示广告地址',
  `ad_title`     varchar(50)  DEFAULT NULL COMMENT '广告标题',
  `ad_url`       varchar(200) DEFAULT NULL COMMENT '广告跳转地址',
  `ad_order`     int(11)      DEFAULT NULL COMMENT '广告展示顺序',
  `ad_type`      varchar(10)  DEFAULT NULL COMMENT '广告类型(0,不跳转，1产品详情，2文章详情)',
  `ad_show_area` varchar(10)  DEFAULT NULL COMMENT '广告展示区域(1学吧banner；2保险商场banner；3头条推荐；4即将上线，5客服中心)',
  `product_id`   int(11)      DEFAULT NULL COMMENT '产品id',
  `content_id`   int(11)      DEFAULT NULL COMMENT '内容id',
  `is_use`       int(11)      DEFAULT NULL COMMENT '是否启用0不启用，1启用',
  `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
  `remark`       varchar(50)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='广告信息表';


CREATE TABLE `pmt_content`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `pic_url`     varchar(200) DEFAULT NULL COMMENT '列表图片地址',
  `title`       varchar(100) DEFAULT NULL COMMENT '标题',
  `type`        int(11)      DEFAULT NULL COMMENT '1学习文章，2发现产品解读，3发现销售技巧，4发现行业资讯',
  `content`     longtext CHARACTER SET utf8mb4 COMMENT '专题正文',
  `status`      tinyint(1)   DEFAULT NULL COMMENT '状态：1显示，0不显示',
  `sort_index`  int(11)      DEFAULT NULL COMMENT '排序',
  `tag_list`    varchar(300) DEFAULT NULL COMMENT '标签列表用#分割',
  `read_number` int(11)      DEFAULT '0' COMMENT '阅读数',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  `remark`      varchar(50)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ROW_FORMAT = DYNAMIC COMMENT ='文章信息表';


CREATE TABLE `pmt_poster`
(
  `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `pic_url`     varchar(200) DEFAULT NULL COMMENT '图片地址',
  `title`       varchar(100) DEFAULT NULL COMMENT '标题',
  `introduce`   varchar(200) DEFAULT NULL COMMENT '介绍',
  `type`        int(11)      DEFAULT NULL COMMENT '海报分类1产品，2理念，3问候，4发现',
  `status`      tinyint(1)   DEFAULT NULL COMMENT '状态：1显示，0不显示',
  `sort_index`  int(11)      DEFAULT NULL COMMENT '排序',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  `remark`      varchar(50)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) COMMENT ='海报表';

CREATE TABLE `pmt_customer_service`
(
  `id`             int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `question_type`  varchar(5)   DEFAULT NULL COMMENT '问题类别1结算开票；2保全理赔；3常见问题',
  `question_title` varchar(50)  DEFAULT NULL COMMENT '问题标题',
  `question_info`  varchar(100) DEFAULT NULL COMMENT '问题内容',
  `status`         tinyint(1)   DEFAULT NULL COMMENT '状态：1显示，0不显示',
  `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
  `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
  `remark`         varchar(50)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) COMMENT ='客服中心';
