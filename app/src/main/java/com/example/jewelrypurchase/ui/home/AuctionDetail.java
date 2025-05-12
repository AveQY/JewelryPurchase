package com.example.jewelrypurchase.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Auction;

import java.util.ArrayList;
import java.util.List;

public class AuctionDetail extends AppCompatActivity {
    private TextView auctionDetailTitle;
    private TextView auctionDetailD;
    private TextView auctionDetailG;
    private TextView auctionDetailP;
    private TextView auctionDetailRP;
    private TextView auctionDetailQ;
    private TextView auctionDetailT;
    private TextView auctionDetailH;
    private ImageView auctionDetailImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_detail);

        CardView auctionDetailBack = findViewById(R.id.auctionDetailBack);
        auctionDetailImg = findViewById(R.id.auctionDetailImg);
        auctionDetailTitle = findViewById(R.id.auctionDetailTitle);
        auctionDetailD = findViewById(R.id.auctionDetailD);
        auctionDetailG = findViewById(R.id.auctionDetailG);
        auctionDetailP = findViewById(R.id.auctionDetailP);
        auctionDetailRP = findViewById(R.id.auctionDetailRP);
        auctionDetailQ = findViewById(R.id.auctionDetailQ);
        auctionDetailT = findViewById(R.id.auctionDetailT);
        auctionDetailH = findViewById(R.id.auctionDetailH);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auctionDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Auction auction = (Auction) intent.getSerializableExtra("Auction");

        Glide.with(this)
                .load(auction.getImageUrl())
                .centerCrop()
                .error(R.drawable.error_picture)
                .into(auctionDetailImg);
        auctionDetailTitle.setText(auction.getName());
        auctionDetailD.setText(auctionDetailD.getText() + getD(auction.getId()));
        auctionDetailG.setText(auctionDetailG.getText() + getG(auction.getId()));
        auctionDetailP.setText(auctionDetailP.getText() + getP(auction.getId()));
        auctionDetailRP.setText(auctionDetailRP.getText() + getRP(auction.getId()));
        auctionDetailQ.setText(auctionDetailQ.getText() + getQ(auction.getId()));
        auctionDetailT.setText(auctionDetailT.getText() + auction.getEndTime());
        auctionDetailH.setText(auctionDetailH.getText() + auction.getAuthor());

    }

    public String getD(int n) {
        List<String> str = new ArrayList<>();
        str.add("这条项链以纯净湛蓝的无烧锡兰蓝宝石为主石，蓝宝石未经任何加热优化处理，保留了最天然纯粹的特质。主石周围环绕镶嵌着一圈璀璨的钻石，如众星捧月般衬托出蓝宝石的深邃与高贵。项链链条采用高品质 18K 白金打造，工艺精湛，线条流畅，与宝石相得益彰，整体设计优雅华丽，尽显高级珠宝的魅力。");
        str.add("该戒指的祖母绿主石来自著名矿区，颜色浓郁鲜艳，呈现出祖母绿特有的翠绿色，晶体清澈透亮，包裹体自然且具有独特的美感，仿佛诉说着其形成的漫长岁月。祖母绿被镶嵌在精致的 18K 黄金戒托上，戒托周围运用了细腻的密镶工艺，镶嵌着众多小钻石，璀璨光芒与祖母绿的翠绿相互辉映，独特的设计既展现了祖母绿的古典韵味，又不失现代时尚感。");
        str.add("这枚戒指的亮点在于那颗大克拉的黄钻，黄钻颜色鲜艳，达到了极为稀有的浓彩级别，火彩闪耀夺目。钻石被精心切割成完美的圆形明亮式，最大限度地展现了其璀璨光芒。戒托采用 18K 玫瑰金打造，流畅的线条设计突出了黄钻的华丽，周围镶嵌的小钻石进一步提升了戒指的整体奢华感，使其成为一件令人瞩目的珠宝佳作。");
        str.add("此项链的主石是一颗艳丽迷人的帕拉伊巴碧玺，呈现出帕拉伊巴独有的霓虹般的蓝色调，色彩极为鲜艳且浓郁，在不同光线下均能散发出迷人的光彩。碧玺周围镶嵌着一圈晶莹剔透的白钻，与碧玺相互映衬，更显其灵动与高贵。项链链条由 18K 白金制成，搭配精致的搭扣，佩戴舒适且牢固，整体设计时尚又独特，完美展现了帕拉伊巴碧玺的魅力。");
        str.add("Winston Kaleidoscope 系列的这条绿碧玺项链设计独具匠心，主石为一颗品质上乘的绿碧玺，其颜色清新淡雅，犹如春日里的嫩叶，散发着生机与活力。项链整体采用了复杂的镶嵌工艺，绿碧玺周围环绕着多种彩色宝石以及钻石，色彩斑斓，相互交织，宛如万花筒般绚丽多彩，充分展现了品牌的精湛工艺和独特创意。18K 金的链条与宝石完美搭配，质感十足。");
        return str.get(n - 1);
    }

    public String getG(int n) {
        List<String> str = new ArrayList<>();
        str.add("主石帕拉伊巴碧玺长约 12mm，宽约 10mm，项链长度为 42cm。");
        str.add("主石祖母绿长约 10mm，宽约 8mm，戒圈尺寸为 14 号，可根据顾客需求适当调整。");
        str.add("主石黄钻克拉数为 3 克拉，戒圈尺寸 13 号，可进行定制调整。");
        str.add("主石帕拉伊巴碧玺长约 12mm，宽约 10mm，项链长度为 42cm。");
        str.add("主石绿碧玺尺寸为长 10mm，宽 8mm，项链总长度 43cm。");
        return str.get(n - 1);
    }

    public String getP(int n) {
        List<String> str = new ArrayList<>();
        str.add("1,000,000 - 1,500,000");
        str.add("500,000 - 700,000");
        str.add("1,500,000 - 2,000,000");
        str.add("1,000,000 - 1,500,000");
        str.add("600,000 - 900,000");
        return str.get(n - 1);
    }

    public String getRP(int n) {
        List<String> str = new ArrayList<>();
        str.add("1,300,000");
        str.add("620,000");
        str.add("1,800,000");
        str.add("1,300,000");
        str.add("750,000");
        return str.get(n - 1);
    }

    public String getQ(int n) {
        List<String> str = new ArrayList<>();
        str.add("珠宝首饰 - 项链 - 帕拉伊巴碧玺项链");
        str.add("珠宝首饰 - 戒指 - 祖母绿戒指");
        str.add("珠宝首饰 - 戒指 - 黄钻戒指");
        str.add("珠宝首饰 - 项链 - 帕拉伊巴碧玺项链");
        str.add("珠宝首饰 - 项链 - 品牌特色绿碧玺项链");
        return str.get(n - 1);
    }
}