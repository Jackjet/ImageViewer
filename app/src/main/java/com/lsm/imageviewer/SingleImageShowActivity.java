package com.lsm.imageviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class SingleImageShowActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout titleLayout;
    private RelativeLayout bottomLayout;
    private ImageView back;
    private LinearLayout selectViewer;
    private ImageView selectorViewer;
    private TextView okViewer;
    private ImageView showViewer;

    private boolean isSelect;
    private String url;
    private int titleBgColorId;
    private int backImgId;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_show);
        titleLayout = findViewById(R.id.singleTitleViewer);
        bottomLayout = findViewById(R.id.singleBottomViewer);
        back = findViewById(R.id.singleBackViewer);
        selectViewer = findViewById(R.id.singleSelectViewer);
        selectorViewer = findViewById(R.id.singleSelectorViewer);
        okViewer = findViewById(R.id.singleOKViewer);
        showViewer = findViewById(R.id.singleShowViewer);
        back.setOnClickListener(this);
        selectViewer.setOnClickListener(this);
        okViewer.setOnClickListener(this);
        isSelect = getIntent().getBooleanExtra("isSelect", false);
        url = getIntent().getStringExtra("url");
        titleBgColorId = getIntent().getIntExtra("titleBgColorId", R.color.imageViewerTitle);
        backImgId = getIntent().getIntExtra("backImgId", R.drawable.back_viewer);
        position = getIntent().getIntExtra("position", 0);
        titleLayout.setBackgroundResource(titleBgColorId);
        bottomLayout.setBackgroundResource(titleBgColorId);
        back.setImageResource(backImgId);
        if (isSelect){
            bottomLayout.setVisibility(View.VISIBLE);
            selectorViewer.setImageResource(R.drawable.imageviweer_select);
        }else {
            bottomLayout.setVisibility(View.GONE);
            selectorViewer.setImageResource(R.drawable.imageviewer_un_select);
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(showViewer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.singleBackViewer:
                finish();
                break;
            case R.id.singleSelectViewer:
                if (isSelect){
                    isSelect = false;
                    bottomLayout.setVisibility(View.GONE);
                    selectorViewer.setImageResource(R.drawable.imageviewer_un_select);
                }else {
                    isSelect = true;
                    bottomLayout.setVisibility(View.VISIBLE);
                    selectorViewer.setImageResource(R.drawable.imageviweer_select);
                }
                break;
            case R.id.singleOKViewer:
                Intent intent = new Intent();
                intent.putExtra("position",position);
                intent.putExtra("isSelect",isSelect);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
