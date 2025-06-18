# 数据库

本项目使用的数据库为**postgresSQL**，在使用项目前需要一下几个表格以及相关数据

## book表

```postgresql
create table book
(
    id          bigserial
        constraint book_pk
            primary key,
    title       text   not null,
    author      text   not null,
    description text   not null,
    tags        text[] not null
);

alter table book
    owner to postgres;

INSERT INTO public.book (id, title, author, description, tags) VALUES (2, '龙族', '江南', '什么？我考上了卡塞尔学院？', '{青春,奇幻,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (1, '斗罗大陆', '唐家三少', '什么？我穿越了？穿越到了斗罗大陆', '{玄幻,等级,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (3, '斗罗大陆2绝世唐门', '唐家三少', '我是霍雨浩，位面之子', '{玄幻,等级,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (4, '斗罗大陆5重生唐三', '唐家三少', '我重生了，为了小舞', '{玄幻,等级,长篇,爱情}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (5, '光之子', '唐家三少', '在光明与黑暗之间，他选择了光明。', '{玄幻,成长,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (6, '冰火魔厨', '唐家三少', '一名厨师的异界冒险之旅。', '{奇幻,美食,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (7, '酒神', '唐家三少', '一个酒鬼的修炼之路。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (8, '琴帝', '唐家三少', '音乐与武道的完美结合。', '{玄幻,音乐,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (9, '天珠变', '唐家三少', '天珠的力量，改变命运。', '{玄幻,冒险,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (10, '神印王座', '唐家三少', '神印的传承，王座的争夺。', '{玄幻,传承,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (11, '斗破苍穹', '天蚕土豆', '少年萧炎，天赋异禀，却因一场变故而失去一切。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (12, '武动乾坤', '天蚕土豆', '林动，天赋异禀，却因家族变故而被迫离开家乡。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (13, '大主宰', '天蚕土豆', '牧尘，天赋异禀，却因家族变故而被迫离开家乡。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (14, '元尊', '天蚕土豆', '周元，天赋异禀，却因家族变故而被迫离开家乡。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (15, '天域苍穹', '天蚕土豆', '少年萧炎，天赋异禀，却因一场变故而失去一切。', '{玄幻,修炼,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (16, '战争与和平', '列夫·托尔斯泰', '以俄法战争为背景，描绘了俄国贵族家庭的命运与历史变迁。', '{历史,战争,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (17, '卡拉马佐夫兄弟', '陀思妥耶夫斯基', '通过三兄弟的故事，探讨了人性、信仰与道德的深刻主题。', '{哲学,家庭,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (18, '百年孤独', '加西亚·马尔克斯', '描述了布恩迪亚家族七代人的传奇故事，融合了魔幻与现实。', '{魔幻现实主义,家族,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (19, '悲惨世界', '维克多·雨果', '讲述了法国大革命时期，主人公冉·阿让的悲惨遭遇与救赎之路。', '{历史,社会,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (20, '老人与海', '欧内斯特·海明威', '讲述了古巴老渔夫圣地亚哥与一条巨型马林鱼搏斗的故事。', '{冒险,人生,中篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (21, '代码大全', 'Steve McConnell', '全面介绍软件构建的最佳实践，适合所有开发者阅读。', '{编程实践,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (22, '程序员修炼之道', 'Andrew Hunt & David Thomas', '通过一系列独立的部分，全面阐释软件开发的最佳实践和重大陷阱。', '{职业发展,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (23, '代码整洁之道', 'Robert C. Martin', '强调代码整洁的重要性，提供了编写高质量代码的指导原则。', '{编程实践,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (24, '重构：改善既有代码的设计', 'Martin Fowler', '介绍了如何通过重构改善代码结构，提高代码质量。', '{软件设计,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (25, '人月神话', 'Frederick P. Brooks', '探讨了软件开发中的管理问题，提出了“人月”概念。', '{项目管理,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (26, '深入理解计算机系统', 'Randal E. Bryant & David R. O’Hallaron', '从计算机系统的角度，深入讲解计算机的工作原理。', '{系统原理,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (27, '计算机程序的构造和解释', 'Harold Abelson & Gerald Jay Sussman', '通过Scheme语言，介绍计算机程序设计的基本概念。', '{编程语言,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (28, '算法导论', 'Thomas H. Cormen 等', '系统介绍了算法设计与分析的基本方法和技术。', '{算法,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (29, '设计模式：可复用面向对象软件的基础', 'Erich Gamma 等', '介绍了23种经典的设计模式，帮助开发者构建可复用的系统。', '{设计模式,长篇}');
INSERT INTO public.book (id, title, author, description, tags) VALUES (30, '程序员的自我修养：链接、装载与库', '俞甲子 等', '介绍了程序在计算机系统中的运行过程，帮助开发者理解程序的运行机制。', '{系统原理,长篇}');
```

## user表

```postgresql
create table public."user"
(
    id       bigserial
        constraint user_pk
            primary key,
    username text not null,
    password text not null
);

alter table public."user"
    owner to postgres;
```

## favorite表

```postgresql
create table public.favorite
(
    id      bigserial
        constraint favorite_pk
            primary key,
    user_id bigint not null,
    book_id bigint not null
);

alter table public.favorite
    owner to postgres;
```

# 后端：Java项目相关配置

## AIChat

因为使用了AI部分（Deepseek-V3），需要配置key

在这个部分使用key，就可以使用AI功能了

![image-20250617160409304](README.assets\image-20250617160409304.png)

## application.yml

确保数据库部分正确

![image-20250617160646922](README.assets\image-20250617160646922.png)

## 启动

![image-20250617161108670](README.assets\image-20250617161108670.png)

# 前端

前端使用vite进行启动

![image-20250617161852181](README.assets\image-20250617161852181.png)

启动即可