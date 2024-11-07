create table qyweixin_provider_access_token (
    id bigint auto_increment primary key comment '系统主键',
    config_id varchar(64) not null comment '配置标识',
    config varchar(2000) not null comment '配置信息',
    access_token varchar(255) not null comment '访问令牌',
    expires_in bigint not null comment '有效期时长',
    expires_timestamp bigint not null comment '过期时间点',
    update_time datetime not null comment '更新时间'
) comment '企业微信服务商访问令牌';

create unique index udx_qyweixin_provider_access_token_config_id on qyweixin_provider_access_token(config_id);

create table qyweixin_suite_access_token (
    id bigint auto_increment primary key comment '系统主键',
    config_id varchar(64) not null comment '配置标识',
    config varchar(2000) not null comment '配置信息',
    access_token varchar(255) not null comment '访问令牌',
    expires_in bigint not null comment '有效期时长',
    expires_timestamp bigint not null comment '过期时间点',
    update_time datetime not null comment '更新时间'
) comment '企业微信服务商代开发应用访问令牌';

create unique index udx_qyweixin_suite_access_token_config_id on qyweixin_suite_access_token(config_id);

create table qyweixin_suite_ticket (
    id bigint auto_increment primary key comment '系统主键',
    config_id varchar(64) not null comment '配置标识',
    config varchar(2000) not null comment '配置信息',
    suite_id varchar(255) not null comment '应用标识',
    suite_ticket varchar(255) not null comment '应用票据',
    update_time datetime not null comment '更新时间'
) comment '企业微信服务商代开发应用票据';

create unique index udx_qyweixin_suite_ticket_config_id on qyweixin_suite_ticket(config_id);

create table qyweixin_permanent_code (
    id bigint auto_increment primary key comment '系统主键',
    suite_id varchar(255) not null comment '应用标识',
    corp_id varchar(255) not null comment '授权企业标识',
    corp_name varchar(255) not null comment '授权企业名称',
    permanent_code varchar(255) not null comment '永久授权码',
    agent_id int not null comment '授权企业应用标识',
    update_time datetime not null comment '更新时间'
) comment '企业微信永久授权码';

create unique index udx_qyweixin_permanent_code on qyweixin_permanent_code(suite_id, corp_id);