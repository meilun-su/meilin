package com.meilun.meilun_su;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import meilun.tools.Bird;

public class AppData extends Application {

    /**
     * 总价钱更新
     */
    public void price_up(){
        int data = AppData.SumPrice;
        for (int i=0;i<AppData.price.length;i++){
            for (int j=0;j<AppData.price[0].length;j++){
                AppData.SumPrice+=AppData.price[i][j]*AppData.number[i][j];
            }
        }
        AppData.SumPrice-=data;
    }

    public static int state;

    public static String str="";
    public static String str1="";

    public static String admin_name="管理员测试账户";
    public static String admin_account="MeiLun_Su";
    public static String admin_pass="123456";

    public static String username;
    public static String account;
    public static String password;

    public static String log_account;
    public static String log_password;

    public static List<Bird> list=new ArrayList<>();

    public static ArrayList<Bird> birds = new ArrayList<>();

    public static int SumPrice;

    //小吃数量整型      用于变量加减
    public static int[][] number={
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
    };

    //小吃id
    public static int[][] footid = {
            {0,1,2,3,4,5,6,7},
            {0,1,2,3,4,5,6,7},
            {0,1,2,3,4,5,6,7},
            {0,1,2,3,4,5,6,7},
            {0,1,2,3,4,5,6,7},
    };

    //小吃所在分类id
    public static int[][] groupid = {
            {0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,1,1},
            {2,2,2,2,2,2,2,2},
            {3,3,3,3,3,3,3,3},
            {4,4,4,4,4,4,4,4},
    };

    //小吃名称
    public static String[][] name ={
            {"豌豆黄儿","炸灌肠","卤味火烧","老北京炸酱面","果脯","炒肝儿","豆汁儿","爆肚"},
            {"钟水饺","石墨豆花","龙抄手","夫妻肺片","韩包子","二姐兔丁","担担面","北川凉粉"},
            {"盐酥鸡","彰化肉圆","肉圆","台北牛肉面","蚵仔煎","大肠包小肠","凤梨酥","棺材板"},
            {"芝士斋糕干","水爆肚","卷圈","锅巴菜","煎饼果子","耳朵眼炸糕","狗不理包子","曹记驴肉"},
            {"重庆小面","重庆酸辣粉","重庆毛血旺","重庆辣子鸡","重庆过桥抄手","赖桃酥","重庆怪味豆","江津米花糖"},
    };

    //小吃价钱
    public static int[][] price={
            {1,2,3,4,5,6,7,8},
            {1,2,3,4,5,6,7,8},
            {1,2,3,4,5,6,7,8},
            {1,2,3,4,5,6,7,8},
            {1,2,3,4,5,6,7,8},
    };

    //小吃详细信息
    public static String[][] detailed={
            {
                //豌豆黄儿
                    "\t\t豌豆黄儿是北京春夏季节一种应时佳品。原为民间小吃，后传入宫廷。清代宫廷的豌豆黄儿，用上等白豌豆为原料，做出成品色泽浅黄、细腻、纯净，入口即化，味道香甜，清凉爽口。\n" +
                    "\t\t按北京习俗，农历三月初三要吃豌豆黄。因此每当春季豌豆黄就上市，一直供应到春末。\n" +
                    "\t\t北京的豌豆黄儿有两种，一种是北海公园仿膳制作的所谓宫廷小吃。另一种则是走街串巷的小贩出售的制作较粗糙的豌豆黄儿，这两种小吃都叫豌豆黄儿，但用料、工艺、价格有天壤之别。",
                //炸灌肠
                    "\t\t炸灌肠是一种北京特有的风味小吃，早在明刘若愚所著《明宫史》中就有记载。清光绪年间福兴居的灌肠在京城小有名气，福兴居的掌柜被称为为\"灌肠普\"，传说其制作的灌肠为慈禧所喜。老北京的灌肠以长安街聚仙居的最好。最初的灌肠是用猪大肠灌制进淀粉、碎肉制成的，后来随着历史的发展，灌肠的制作工艺发生了变化，改用淀粉加上红曲和香料灌在猪小肠中成型，而超市中能够买到的灌肠则把红曲和小肠都省去了，仅仅是用绿豆淀粉加香料灌制成型的一个长长的淀粉肠。\n" +
                    "\t\t灌肠讲究用猪大肠中练出的油炸制，因此正宗的炸灌肠闻起来总有一股猪大肠的特殊味道，但人们出于健康的考虑已经很少用猪大肠油来炸灌肠。炸灌肠的时候须先将成型的灌肠切片，在饼铛中炸至两面冒泡变脆，即取出浇上拌好的盐水蒜汁趁烫吃。",
                //卤煮火烧
                    "\t\t卤煮火烧是北京汉族特色小吃，起源于北京城南的南横街。据说光绪年间因为用五花肉煮制的苏造肉价格昂贵，所以人们就用猪头肉和猪下水代替，经过民间烹饪高手的传播，久而久之，造就了卤煮火烧。地道的北京人估计没几个不好吃卤煮火烧的。卤煮火烧是老北京纯粹的东西，土生土长，比京剧还要纯粹。最初的卤煮出自于宫廷的“苏造肉。”据说光绪年间因为用五花肉煮制的苏造肉价格昂贵，所以人们就用猪头肉和猪下水代替，经过民间烹饪高手的传播，久而久之，造就了卤煮火烧。地道的北京人估计没几个不好吃卤煮火烧的。",
                //炸酱面
                    "\t\t炸酱面是汉族面食，老北京炸酱面在北京十分流行，而上海、广东、东北也有不同制法的炸酱面。韩国亦有炸酱面，是由华侨带入韩国，以春酱（黑豆酱）为调味料，加上洋葱、虾、肉类等。不过摆放相当精致，中间盘放面条，最中央是一撮紫色的炸酱，像一盘工艺品。炸酱面是北京富有特色的食物，由菜码、炸酱拌面条而成。将黄瓜、香椿、豆芽、青豆、黄豆切好或煮好，做成菜码备用。然后做炸酱，将肉丁及葱姜等放在油里炒，再加入黄豆制作的黄酱或甜面酱炸炒，即成炸酱。面条煮熟后，捞出，浇上炸酱，拌以菜码，即成炸酱面。也有面条捞出后用凉水浸洗再加炸酱、菜码的，称“过水面”。",
                //果脯
                    "\t\t果脯，也称蜜饯，是以桃、杏、李、枣或冬瓜、生姜等果蔬为原料，用糖或蜂蜜腌制后而加工成的食品。除了作为小吃或零食直接食用外，蜜饯也可以用来放于蛋糕、饼干等点心上作为点缀。果脯是明朝时期的御膳房发明的。北京和台湾为蜜饯生产重镇。\n" +
                    "\t\t北京所产的果脯称为京式果脯，是北京特产，中外闻名。果脯种类繁多，著名传统产品有苹果脯、酸角脯、杏脯、梨脯、桃脯、太平果脯、青梅、山楂片、果丹皮等。",
                //炒肝儿
                    "\t\t炒肝儿是北京地区著名的汉族传统小吃之一，清末由前门外鲜鱼口“会仙居”的“白水杂碎”改进而成。名炒肝儿，其实以猪肥肠为主，猪肝只占1/3。制作方法是先将猪肠用碱、盐浸泡揉搓，用清水加醋洗净后再煮。开锅后改用文火炖，锅盖盖好使肠子熟透而不跑油。烂熟后切成5分长的小段，俗称“顶针段”，再将鲜猪肝洗净，用刀斜片成柳叶形的条。",
                //豆汁儿
                    "\t\t豆汁儿是老北京独特的汉族小吃，根据文字记载有300年的历史。豆汁是以绿豆为原料，将淀粉滤出制作粉条等食品后，剩余残渣进行发酵产生的，具有养胃、解毒、清火的功效。\n" +
                    "\t\t提起北京小吃，首先让人想起豆汁。北京人爱喝豆汁，并把喝豆汁当成是一种享受。可第一次喝豆汁，那犹如泔水般的气味使人难以下咽，捏着鼻子喝两次，感受就不同一般了。有些人竟能上瘾，满处寻觅，排队也非喝不可。《燕都小食品杂咏》中说：“糟粕居然可作粥，老浆风味论稀稠。无分男女，齐来坐，适口酸盐各一瓯。”并说：“得味在酸咸之外，食者自知，可谓精妙绝伦。”\n" +
                    "\t\t豆汁儿具有色泽灰绿，豆汁浓醇，味酸且微甜的特色。豆汁是北京具有独特风味的冬、春季流行小吃。尤其是老北京人对它有特殊的偏爱。过去卖豆汁的分售生和售熟两种。售生者多以手推木桶车，同麻豆腐一起卖；售熟者多以肩挑一头是豆汁锅，另一头摆着焦圈、麻花、辣咸菜。《燕都小食品杂咏》中说：“糟粕居然可作粥，老浆风味论稀稠。无分男女齐来坐，适口酸盐各一瓯。”并说：“得味在酸咸之外，食者自知，可谓精妙绝伦。”喝豆汁必须配切得极细的酱菜，一般夏天用苤蓝，讲究的要用老咸水芥切成细丝，拌上辣椒油，还要配套吃炸得焦黄酥透的焦圈，风味独到。\n" +
                    "\t\t北京卖豆汁儿的小店虽然不像以前那样多了，但是像德华居小店、锦馨回民豆汁儿店等生意依然和以前一样火爆。尤其是锦馨回民豆汁儿店，因为这里的豆汁儿和焦圈都有“中华名小吃”之称，都得到了原来花市火神庙“豆汁丁”的真传，十分地道，所以生意更是红火。",
                //爆肚
                    "\t\t爆肚是北京风味小吃中的名吃，多为回族同胞经营。爆肚早在清乾隆年代就有记载。过去和现时，每当秋末冬初，北京的清真餐馆和摊贩就经营爆肚。北京天桥有“爆肚石”，门框胡同有“爆肚杨”，还有“爆肚冯”、“爆肚满”等最为出名。\n" +
                    "\t\t爆肚是个统称，它分羊爆肚和牛爆肚，牛爆肚分两种———牛百叶和牛肚仁；羊爆肚分9种———羊散丹、羊肚领、阳面肚板、阴面肚板、蘑菇儿、蘑菇儿尖、食信儿、葫芦儿、大草牙。爆肚王一般只做5种大众易于接受的品种：牛百叶、牛肚仁、羊散丹、羊肚领、羊肚板（阳面肚板）。",

                },

            {
                    "\t\t水饺，古名为“水角”，北方人读“角”为“佼”音，故称“水饺”。水饺是北方人常用的食品，馅心用蔬菜多于用肉食，但逢年过节时，馅心也十分考究。在四川，水饺只是作为一种小吃，因此馅心多用肉制作，制作也讲究得多，配上特制的好汤和调料，互相补充，相得益彰。以前，成都卖水饺的摊店不少，其中以“水饺钟”最为有名，这是因其水饺皮薄、馅嫩、味美之故。",

                    "\t\t石磨豆花是四川及重庆常见的汉族小吃。是嫩豆花的一种吃法，它与运用特殊工艺加工而成的豆花蘸水和大米饭配合食用，因其鲜嫩可口而流传广、影响大，在中国餐饮文化中占有一席之地。垫江石磨豆花的配方、制作，自有其历史渊源和独特之处，米饭、豆花、蘸水三位一体，密不可分。垫江石磨豆花又称豆腐脑或豆冻，是由黄豆浆凝固后形成的中式食品。不过豆花比豆腐更加嫩软，在岭南通常加入糖水或黑糖食用。中国北方称豆花为豆腐脑，但北方豆腐脑多半为咸辛味，使用盐卤凝固，南方则多使用石膏。\n" +
                    "\t\t花园镇一带的石磨豆花，白净、细嫩且韧性特好。传说为汉刘邦庶子淮南厉王刘长贬居雍店(花园镇古名)时，其妃雍氏善磨豆花，雍氏去世后，花园镇一带的豆花已小有名气，人们认为系雍氏神力所助，故称\"神仙豆花(豆腐)\"。这种制作豆花的精细工艺很早便在郫县流传开来。望丛祠餐厅推出的\"魔方豆花\"，当属郫县豆花的佼佼者。除豆花白净、细嫩、韧性好以外，因豆花佐料有40多种，分放在细瓷小碟中，小碟里外三层放在一张桌子上，构成一个美妙多彩的图案，因其形状如魔方而被人称为\"魔方豆花\"，最有趣的是，就餐时客人须亲自动手，按自己喜好，调制豆花蘸碟。这是郫县最有名的一道特色菜，被誉为\"东方一绝\"。",

                    "\t\t龙抄手，是成都非常出名的小吃，龙抄手皮薄馅嫩，爽滑鲜香，汤浓色白，为蓉城小吃的佼佼者。龙抄手的得名并非老板姓龙，而是创办人张武光与其好友等在当时的“浓花茶园”商议开抄手店之事，切磋店名时，借用“浓花茶园的“浓”字，以谐音字“龙”为名号，也寓有“龙腾虎跃”、“吉祥”、生意兴“隆”之意。“抄手”是四川人对馄饨的特殊叫法。成都的“龙抄手”1941年开设于成都的悦来场，上个世纪50年代初迁往新集场，60年代后又迁至春熙路南段至今，迄今已有60余年的历史了。",

                    "\t\t夫妻肺片，四川汉族特色小吃，以牛头皮、牛心、牛舌、牛肚、牛肉为料，并不用肺。注重选料，制作精细，调味考究。夫妻肺片片大而薄，粑糯入味，麻辣鲜香，细嫩化渣。深受群众喜爱，为区别于其他肺片，便以“夫妻肺片”称之，在用料上更为讲究，质量日益提高。",

                    "\t\t韩包子从创业至今已有八十多年的历史了。包子应该是中国最普遍的小吃了，从北到南，从东到西都能见到包子，做出名的也很多，成都韩包子便是其中一例。原料特级面粉、肥瘦猪肉、化猪油等及各种调料。韩包子其特色是：花纹清晰，皮薄馅饱，松软细嫩。在做韩包子要注意制作方法。",

                    "\t\t二姐兔丁在成都很有名气，它最有名是兔丁肉多骨头少，不加兔头，佐料加有二姐特殊的配法，香鲜可口。二姐的\"兔\"系列中还有五香卤兔、红板兔、麻辣兔丁。另外，二姐兔丁店还经营红油鸡块、蒜泥白肉、凉拌肺片、五香蹄筋等多种凉菜。\n" +
                    "\t\t二姐兔丁在用料上讲究下个“精”字，不但选用的兔肉肥嫩，所用的各种佐料亦是好中选优。特别是海椒，定要选用成都二筋条和朝天椒；花椒选用汉源优质品料；酱油、豆豉、花生、芝麻、无不精挑细选，采用最佳原料。在火候上、刀工上，红油的炼制上她都匠心独运，讲究制作工艺程序。所以她拌出的兔子，色泽红亮，形态饱满，麻辣适口，香嫩回甜，入口细腻、油润。",

                    "\t\t担担面（Noodles, Sichuan Style），汉族特色面食。著名的成都小吃(又说自贡小吃，起源于自贡）。用面粉擀制成面条，煮熟，舀上炒制的猪肉末而成。成菜面条细薄，卤汁酥香，咸鲜微辣，香气扑鼻，十分入味。此菜在四川广为流传，常作为筵席点心。\n" +
                    "\t\t用面粉擀制成面条，煮熟，舀上炒制的猪肉末而成。成菜面条细薄，卤汁酥香，咸鲜微辣，香气扑鼻，十分入味。",

                    "\t\t川北凉粉发源于清末民初的四川省南充市，原为农舍小食，现已成为川菜代表性汉族特色著名小吃之一。川北凉粉是采用优质豌豆去壳，用水浸泡后，磨成细浆，然后过滤去渣，沉淀脱水，制成豆粉。再经加热搅拌成糊状，装入盆、盘待用。凉粉制作法有很多种，可也用绿豆、大米等做出不同味道的凉粉区别，及营养价值与凉粉的影响。",
            },

            {
                    "\t\t盐酥鸡是台湾最常见的小吃之一，不过其实它是一个综合性的全称，盐酥鸡摊位中除了油炸小块鸡肉骨外，通常一并卖炸甜不辣（包括猪血糕及鱼板）、炸花枝脚、炸蕃薯条、炸四季豆、炸芋粿等等，有的摊位会兼卖炸鸡排。",

                    "\t\t彰化肉圆是台湾彰化县的特色小吃，据传是由位于彰化市的一名肉圆摊业者吴许水桃所创。肉圆外皮多以甘薯粉制作，内馅视各家口味不同而有差异，但多数店家用猪后腿肉制成的绞肉，佐以香菇为主。 调理方法先将肉圆连同容器放入蒸笼蒸熟，固定外型，待食用时，再油炸而成。",

                    "\t\t肉圆，在中国大部分地区叫肉圆子，主要的配料是各种家禽的肉。由于肉圆子制作简单，食用方便，所以它是中国地区，乃至整个东南亚地区都喜爱的一种家常料理。",

                    "\t\t拿下第一届台北国际牛肉面节冠军的精品牛肉面，今年以餐饮美味、环境卫生、餐饮卖相、服务质量及价格合理拿下五颗星的质量鉴定，店中牛肉面除了好吃的没话说之外，在环境卫生和服务上都有其独特用心之处。馔王总经理苏先生表示，店中牛肉面强调减油味醇，讲求以真材实料来制作牛肉面，熬煮汤头的成本还要比肉来得贵，选用顶级进口牛肉和牛骨加上数十种独门食材，有蕃茄、清炖、红烧和麻辣四种汤头可供选择，秉持原味不添加味精，使您品尝完后不至口干舌燥，用餐时刻，服务人员还会不时询问客人是否有需要加汤的服务。馔王牛肉选用美国牛的腱子心部位，以数十种独门食材，慢火细炖十二钟头以上，牛肉尝起来口感较为软嫩，而搭配的面条有细、宽面之分，从第一口至最后一口依旧Q劲十足，具口感。位在台北东区顶好商圈的馔王精品牛肉面，虽然店面在二楼，自从得到第一届冠军牛肉面的头衔后，不仅打响了品牌的知名度，也受到许多客人的肯定，除了餐点美味好吃以外，在清洁卫生上让吃牛肉面的客人享受干净舒适的用餐环境，以及工作人员亲切贴心的服务，五颗星的荣誉可说是实至名归喔！",

                    "\t\t蚵仔煎（读做ô-á-jián，普通话译作“海蛎煎”），发源于福建泉州，是闽南，台湾，潮汕等地经典的汉族小吃。起源是沿海地区人民在无法饱食下所发明的替代粮食，是一种贫苦生活的象征。蚵仔煎据传就是这样的一种在贫穷社会之下所发明的创意料理。",

                    "\t\t大肠包小肠 ，是台湾1990年代兴起的一种特殊小吃，说穿了很简单，就是将体积较大的糯米肠切开后，再夹住体积较小的台式香肠，即成为“大肠包小肠”，与美国的热狗有异曲同工之妙。\n" +
                    "\t\t台湾某些地区的夜市也会提供豪华版本的大肠包小肠，除了香肠之外，还会加上各种如蒜头、花生粉、酸菜等配料，藉此创造更丰富的口味及口感。",

                    "\t\t凤梨酥相传最早起源于三国时期，其凤梨闽南话发音又称“旺来”，象征子孙旺旺来的意思，而凤梨亦是台湾人拜拜常用的贡品，取其“旺旺”“旺来”之意，所以在当代台湾婚礼习俗中，也是广为应用，深受民众喜爱。\n" +
                    "\t\t凤梨酥内馅，并不是单纯的菠萝。为了口感需要，通常会添加冬瓜，这样的口味已经是大多数人的习惯。近年流行养生，台北市面上可以买到加了五谷杂粮、松子、蛋黄、栗子等不同口味的凤梨酥；饼皮也加入燕麦等食材，口感更为多元。也有业者改用其它水果做馅料，发明了例如香瓜酥、蜜李酥、酸梅酥等。",

                    "\t\t棺材板，前身是用西式酥盒加上鸡肝等中式配料做成的。一开始不称棺材板，而为鸡肝板。据说在三四十年前，台湾有一位姓许的师傅，他品尝了一种名叫“鸡肝板”的点心后，觉得口味很特别，于是回家研究改良，终于制成了一种令他更加满意的点心。但如何给这种点心取个新的名字呢？他想来想去没想出来。有一天，他忽然发现新的点心怎么看怎么像棺材，于是就决定命名为“棺材板”。或许由于制作特别、口味特殊，加上名称的“怪”与“邪”，最后这种点心受欢迎的程度，远远超过了“鸡肝板”，成为台南至今盛行的独特小吃。",
            },

            {
                    "\t\t芝兰斋糕干是天津津门著名的汉族传统糕点。具有60多年历史的老字号。芝兰斋糕干是用小站稻米、糯米磨粉夹入多种馅料蒸制而成。因该糕干系芝兰斋字号创制，故名芝兰斋糕干。\n" +
                    "\t\t芝兰斋糕干外观洁白、不粘牙、不掉面、口感绵软、风味独特。",

                    "\t\t水爆肚是京津两地特有的清真风味小吃，以天津“知味斋”制做最佳。所谓水爆肚，就是把羊肚或牛肚（包括千层百叶）切成细丝，放入开水中焯一下，将其爆熟，要恰到好处，才能保持肚丝的鲜、脆、嫩、爽口。吃爆肚时，必须要趁热蘸佐料吃，才能构成其特有的风味。作料置放于小碗内，其中有酱油、醋、芝麻酱、香菜、辣椒油，边吃边蘸，滋味醇厚，多数人用之佐酒，也有配以芝麻烧饼作为菜肴的。水爆肚不仅风味独特，还有健脾养胃之功，因此深受群众欢迎。",

                    "\t\t炸卷圈是天津传统小吃，用香油、麻酱、酱豆腐、姜末、五香粉、盐等调拌绿豆菜、香干、香菜、粉皮等成馅，以豆皮卷馅，切成约十五厘米的长段，用面粉、醋、盐调成的糊，糊粘两头切口，然后下油锅炸，炸成金黄色即可。\n" +
                    "\t\t炸卷圈清淡适口，外脆里嫩，天津人一般都是夹饼吃。",

                    "\t\t锅巴菜即津味嘎巴菜。色、形美观，多味混合，清香扑鼻、素淡爽口。“嘎巴”香嫩有咬劲，卤子透亮而觉鲜。加上绿豆性甘凉，能清热，解暑，利水、解毒，因此，每逢夏季，备受欢迎。",

                    "\t\t煎饼果子是天津著名的小吃，天津人把其作为早点。由绿豆面薄饼，鸡蛋，还有果子（油条）或者薄脆的“果篦儿”组成，配以面酱，葱末，辣椒酱（可选）作为佐料。",

                    "\t\t天津三绝食品之一，清真美食，用糯米作皮面，红小豆、赤白砂糖炒制成馅，以香油炸制而成。成品外型呈扁球状，淡金黄色，馅心黑红细腻，是津门特产。",

                    "\t\t天津狗不理包子是我国著名小吃，为“天津三绝”之首，是中华老字号之一，起源于1858年清朝咸丰年间。狗不理包子倍受欢迎的原因在于其用料精细，制作讲究，在选料、配方、搅拌、揉面、擀面等方面都有一定的绝招，做工上更是有明确的规格标准。刚出屉的包子，褶花匀称，每个包子都是15个褶，大小整齐，香而不腻，一直深得百姓和各国友人的青睐。",

                    "\t\t曹记驴肉是京津地区汉族传统名菜之一。因创始人姓曹而得名，在北京流传至今已有二百余年历史。曹记驴肉，精选新鲜优质驴肉，配以多种香料，放进锅里慢火煮七至八个小时。曹记驴肉，富含蛋白，酥烂易嚼，味道鲜美，远近闻名。",
            },

            {
                    "\t\t重庆小面是一款发源于重庆的汉族特色小吃，属于渝菜。品种丰富,一般按有没有臊子来分，没有臊子的小面调味料也很丰富。一碗面条全凭调料来提味儿，先调好调料，再放入面条。麻辣当先，面条劲道，汤鲜而厚味。",

                    "\t\t酸辣粉是四川、重庆等地的汉族传统名小吃，其特点是“麻、辣、鲜、香、酸且油而不腻”。 “酸辣粉”主粉由红薯，红苕，豌豆按比例调和，然后由农家用传统手工漏制而成。酸辣粉源于四川民间，取食材至当地手工制作的红薯粉，味以突出酸辣为主而得名。",

                    "\t\t重庆毛血旺是非常流行的小吃。将鸭胸肉，猪肚，猪心，火腿肠放炒锅内加泡辣椒，干辣椒会炒至香，加入少许鲜汤，然后放入牛油，香油，味精，鸡精起锅后盛入炒好做底用的辅料上即成。 麻辣鲜香，汁浓味足。血有补血和清热解毒作用，并有预防和缓解缺铁性贫血的效果。",

                    "\t\t辣子鸡，是一道大众喜闻乐见的美味佳肴，一般以鸡为主料，加上葱、干辣椒、花椒、盐、胡椒、味精等多种材料精制而成，营养丰富，味道鲜美，虽然是同一道菜，各地制作也各有特色，如，重庆歌乐山辣子鸡、超级辣子鸡、辣子鸡块、黔味菜肴辣子鸡、川味辣子鸡丁、辣子鸡丁等。",

                    "\t\t因其独特的饮食方法而闻名，食用时将碗中抄手夹入味碟中，蘸上碟中调味后食用，犹如过桥，故名。特色是细嫩爽口，因人而宜，味多变化。",

                    "\t\t赖桃酥是重庆市著名的特产，它是一款老少皆宜的美味糕点。口味质地细腻，甜香可口，有浓郁的麻油和花果料香味，以香甜酥松而闻名，入口化渣，无腻人感。外观呈核桃壳花纹，有自然裂口，色泽深黄光润。",

                    "\t\t重庆怪味胡豆具有特有的工艺、独有的风味，集麻、辣、甜、咸、鲜、酥为一体，堪称一绝。",

                    "\t\t江津米花糖，因产于重庆江津区而得名，是重庆市的著名特产之一。以优质糯米、核桃仁、花生仁、芝麻、白糖、动植物油、饴糖、玫瑰糖等为原料，经10余道工序精工制成。产品洁白晶莹，香甜酥脆，爽口化渣，甜而不腻，营养丰富，有滋阴补肾、开胃健脾等功效。江津米花糖历史悠久，销往北京、广州、深圳、上海、昆明、新疆等234个城市，还远销美国、新西兰等国家和地区，深受各地消费者的喜爱。",
            },
    };

    //小吃图片URL
    public static String imageUrl[][] = {

            {//北京小吃
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KiJToGWioxIoMYPN.Nn2.AkMo4I6soaYQKEnJX3QeJZRQU7WbBX5tiwKUeMFv8bLAQ!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5Kn4ByiyuQu2FX67.1Tb6i7lG6wGowKDhwqiSth1BVThLh4Fp.p3EwvvXkpxzG8IsTg!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KiWHEPMTEkh24oFECf4faQWW8EEpmvgDfTDmSbiVBOAb7.nb.*lgzUd54O*ZZMU.HQ!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KlHwhew13PQxqCCOvBuKhhb*IOq6KhDQbB.zVUJ0I.Knfu0yHR3F1bNHPlV.IbuPKg!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KmAzwr6s93K*JC7bXgBfbPEd6YuG8FX.5ARflLh83PFU*Gyzdtfn7tqf5UQ1zjEJ*g!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KmJLm*nMQX9Vz8i2rQzbyBGKMqgdHqbgxLBHIzT109pLp*o4leAfYaR5XMIltJGQUA!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5Kq3KKumX63qcDRbykYzFlmZ6Qs8PJOCJQXgm9OHMkC3iSV.Isa.acKN*8Lc6M1Dq7A!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4953Siaz8/AmUus9Db.JHJUP6FXIQ5KswvcXF17QTh*69KCUX4JHSi0fDVkVa.gJhCOov0QtO8dXq1so*Xy2W4TXvLLKoWtg!!/mnull&bo=yACWAMgAlgARCT4!&rf=photolist&t=5",
            },
            {//成都小吃
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5KtSyZJJ1QPv3p*JUbyBRjSQ0vt.cs25WaipTbgq0jghgKwiqM9Cn7cg8MLHWK8rUVQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5KjHaMARNEqF6N7GIqh5hoEeyn0302M*hLT8218cI*Ca2vPUmowP2ZrLzuc4EuAfByg!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5Kr*5zpH6k5ocUbyGg*Kss9pqa6gSR*aDMBNyfQDBINatyHF48Plj5e97aZluP02BFQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5KhFmHjza.srMQrgfsbi0gi95GrPhdO9Q34aMrKwS2oLLI.fpOf.ptz*1f0wzgsfv1w!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5KudTy3X1ws*2Nb3Wo*x9SKFCv2QRVI9HgXR60Vb9nlC1XXN6d3VDPxxr*jst*9D1hA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5Kq90TEI6CCF68ZvemH01bZEfTajPsBmG9ZvQeLDEs4WFOCKY2Z6lv2wVG5zZ0SfnQA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5Ku5PAo2fKMQQjxy5EDJzs.tDsdNml5HDImr122k9jnsYtUyAS0Uz9PnzdyYu*fxYHA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew49509nf3i/AmUus9Db.JHJUP6FXIQ5KiuCVL1fzenJ4BNRbMT3.9lGtp4qtSVamYf99A6EOciRqMOpNOKdtzcO4xSiu0HpJQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
            },
            {//台湾小吃
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KmF9MjLuDGhSCZi4iTcuizibSawGOPaW1VPJHl3TdWPCU5LOqVC6QJVT.T6YFPbwFw!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KjJOzhD*zTIK*ivBDpPMyeZanHm.3g9zenjnPSUNdS49xIXeP2fpYXdpLZuYgO0w5A!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KndIMLoOP3HQ3XAmCJWS00NIyRz0V9m2hHZ.jNy6L3lWf0IiktQsgEWdtRG*68eQ4Q!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5Kla*kdxUISYKzWssTn0mkj*f4MqIHTeTs6ecy4WPG62yHTORlJJs3B2IqDRVgXApSQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KvIaFNqEPO9fginrVnGLHRWKtipjbkAOEJMFDsdtN4VBxGy2cI5a1senKnFdLwHZHQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KkfbgkgDmfBIsyR*iD6qd1qeKQ2WM6fXPsTSPgsAJMth9D5OuPf7Wd48IsHftTH5jw!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5Kg09OnM9Mi*z4j1*Pgx1wuFtQT4oDwwL2lSfkO6AZXgw9U.o6CLHyW3aQlMmNy9TuA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4950A6Zwe/AmUus9Db.JHJUP6FXIQ5KrrKwu02jlYBpZ0fZAASEiJ1H*4TwCRbG8su3zfBvNyI*sdWBnXsFBVtgwIKYSYgmQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
            },
            {//天津小吃
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KmOzJya9bPTaXbzsUBzm9daNAmtc2hfd6MFnnn*4FZVqOdO2OeygqZas28EmrbFgLg!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KlCsQTGEctV5TzXQ9ZMclhJv8A*jYnrz5MSRCWsD5gicGb4ccX6o4rgfkACn.7qqIA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5Kje*ACBWN.0swhXgJemxbfX3oaoPpmvGPp5CvJfBfLJ9BzrQPKIBUI1itXCe.d684A!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KkwvTHd0y9fVVORvbRkcJBNuooTIazUPbhB6koMANROnwbqaVCec4qp*Vjw782TS1g!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KrOfBMi7PgAUDo06Uz8*q1h8BaEP6DcZtP2L40l1HEf5BwnE8h1qiZPgeUza0hcxag!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KqlKJLqY6WtjhnhuO7uMufAYxB1AwN5E3UDC28W*fZxEvneyikZ..QiYnj82zUnojg!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5KtnXMRNmk3k1BeWUutFCfWMsv4tJR37cMbKs9rMczadmRRy62qPzMGU8O*AUD.bM7Q!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4952stG0B/AmUus9Db.JHJUP6FXIQ5Km0bqmJcHXzq*GM49NCOS2ZeX0wLm7sN3f2OV.y36hy41rTjl6fPOZnyg1b.wxEC1w!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
            },
            {//重庆小吃
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Knd6smYPMoDm1srqify4*dxDUXH5ufSdW3fVxgGUblJmuVtjJ*FLZHZweXW241lVIw!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5KiL4J1DDV97XAk313vqnUVoPJqVfU7VTjQSJibSUZ1PZmbTiVx4N08Qx5*gUuM1l3Q!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5KugL972skXQg3piDNqiuDfogGYvoemypQs.0D0s9167zgHg1VxO7PPRtBHAaLUAtJQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Kibenj30H*jiSDyZhGtuvMgDVKNdj3yPKBwPpoLBXL9ogZYzBIsPagHZBHdVcOkBeQ!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Kvw5lwWgAVmjfZhY7eX60km*hL3m8u7b7bSxeq0s8aPDMlrh*Pq*MZmSiCZq62O1YA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Kh2uo4Qu6HEOzNx362XEJFh1pLt.G.tlEolqWqNXTPG8uL9CvWzIBlVAU28blB0lJA!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Kj6w3cwEuvqA9uIKWt9OW7ihWYtCR77W854CrUgifomOighuoV0MkaIsirHYR*kmAg!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
                    "http://m.qpic.cn/psc?/V12ew4954WDiPo/AmUus9Db.JHJUP6FXIQ5Kj7qQ458GLdspA8EuwrHmcOPzr5ecY0AE2Zr1JZWdgs.8BZOJwGh0q*N3z6RydcQBw!!/mnull&bo=yACWAAAAAAARB24!&rf=photolist&t=5",
            },
    };

}
