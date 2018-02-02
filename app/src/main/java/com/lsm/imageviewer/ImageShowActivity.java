package com.lsm.imageviewer;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 建议在AndroidManifest.xml清单文件中给该activity设置状态栏的颜色
 * 颜色值和该activity的标题栏颜色一直即可（即：titleBgColorId）
 *
 * 可通过isSingle来控制是多选还是单选，默认多选
 * 在onActivityResult中根据resultCode来获取数据   ArrayList<String> list = data.getStringArrayListExtra("data")
 * resultCode == RESULT_OK，点击了“完成”按钮；resultCode == RESULT_CANCELED，点击了“返回”按钮
 */
public class ImageShowActivity extends AppCompatActivity implements ImageViewerAdapter.OnItemChildViewClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private RelativeLayout titleLayout;
    private ImageView back;
    private TextView finishViewer;
    private List<ImageViewerModel> list;
    private GridLayoutManager gridLayoutManager;
    private ImageViewerAdapter adapter;
    private int titleBgColorId;
    private int backImgId;
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        recyclerView = findViewById(R.id.recyclerView_Viewer);
        titleLayout = findViewById(R.id.titleViewer);
        back = findViewById(R.id.backViewer);
        finishViewer = findViewById(R.id.finishViewer);
        back.setOnClickListener(this);
        finishViewer.setOnClickListener(this);
        titleBgColorId = getIntent().getIntExtra("titleBgColorId",R.color.imageViewerTitle);
        backImgId = getIntent().getIntExtra("backImgId",R.drawable.back_viewer);
        isSingle = getIntent().getBooleanExtra("isSingle",false);
        titleLayout.setBackgroundResource(titleBgColorId);
        back.setImageResource(backImgId);
        list = new ArrayList();
        gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ImageViewerAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
        getImage();
    }

    public void getImage(){
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            list.add(new ImageViewerModel(path,false));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onViewClick(View itemView, int position, int id) {
        switch (id){
            case 0:
                if (isSingle){
                    for (int i = 0; i < list.size(); i++) {
                        if (i != position && list.get(i).isSelect()) {
                            list.get(i).setSelect(false);
                            adapter.notifyItemChanged(i);
                        }
                    }
                    list.get(position).setSelect(!list.get(position).isSelect());
                    adapter.notifyItemChanged(position);
                }else {
                    boolean select = list.get(position).isSelect();
                    if (select) {
                        list.get(position).setSelect(false);
                    } else {
                        list.get(position).setSelect(true);
                    }
                    adapter.notifyItemChanged(position);
                }
                break;
            case 1:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backViewer:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.finishViewer:
                ArrayList<String> selectList = new ArrayList<>();
                for (ImageViewerModel model : list) {
                    if (model.isSelect()) {
                        selectList.add(model.getPath());
                    }
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("data",selectList);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
