package com.example.handwriting.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.handwriting.bean.Pic;
import com.example.handwriting.db.Replay;
import com.example.handwriting.other.DestroyActivityUtil;
import com.example.handwriting.other.GetData;
import com.example.handwriting.view.ItemView;
import com.example.handwriting.other.MyImgBtn;
import com.example.handwriting.R;
import com.example.handwriting.adapter.TieAdapter;
import com.example.handwriting.bean.Tie;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.WordZ;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.Learn;
import com.example.handwriting.db.Plan;
import com.example.handwriting.db.Userdb;
import com.example.handwriting.other.UploadFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends Activity
{
    List<Pic>listb=new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar,shequ_pro;
    private TextView Hsk,title;
    private TextView day,allHan,wHan;
    private Button h,studyJ,single,cuozi,phb;
    private MyImgBtn f_cz;
    private String timeStr;
    List<Tie> tieList = new ArrayList<>();
    List<Replay> replayList=new ArrayList<>();
    List<Plan> plans=new ArrayList<>();
    List<Plan> planList=new ArrayList<>();
    List<Learn>learns=new ArrayList<>();
    int size=0;
    private int selectedSexIndex = 0;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final int GALLERY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String mTempPhotoPath;
    private ImageView mHHead;
    private User mUser;
    private Userdb user;
    private TextView mUserName;
    private TextView mUserVal;
    private ItemView mNickName,mVip;
    private ItemView mSex;
    private ItemView mSignName;
    private ItemView mPass,mTie,mAbout;
    private RadioGroup  radioGroup;
    private RadioButton radioButton;
    private Button practise,tc,review;
    private long firstTime = 0;
    private void showTypeDialog() {
        //显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(MainActivity.this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            getString(R.string.permission_read_storage_rationale),
                            REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                } else {
                    dialog.dismiss();
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
                }
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            getString(R.string.permission_write_storage_rationale),
                            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
                } else {
                    dialog.dismiss();
                    File tempPhotoFile = new File(mTempPhotoPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 如果在Android7.0以上,使用FileProvider获取Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        String authority =MainActivity.this.getPackageName() + ".fileProvider";
                        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, authority, tempPhotoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPhotoFile));
                    }
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }
   
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }
    protected void showAlertDialog(String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }
    public void startCropActivity(Uri source) {
        UCrop.Options options = new UCrop.Options();
        //裁剪后图片保存在文件夹中
        Uri destinationUri = Uri.fromFile(new File(getExternalCacheDir(), "uCrop.jpg"));
        UCrop uCrop = UCrop.of(source, destinationUri);//第一个参数是裁剪前的uri,第二个参数是裁剪后的uri
        uCrop.withAspectRatio(1,1);//设置裁剪框的宽高比例
        //下面参数分别是缩放,旋转,裁剪框的比例
        options.setAllowedGestures(UCropActivity.ALL,UCropActivity.NONE,UCropActivity.ALL);
        options.setToolbarTitle("移动和缩放");//设置标题栏文字
        options.setCropGridStrokeWidth(2);//设置裁剪网格线的宽度(我这网格设置不显示，所以没效果)
        options.setCropFrameStrokeWidth(2);//设置裁剪框的宽度
        options.setMaxScaleMultiplier(3);//设置最大缩放比例
        options.setHideBottomControls(false);//隐藏下边控制栏
        options.setShowCropGrid(true);  //设置是否显示裁剪网格
//        options.setOvalDimmedLayer(true);//设置是否为圆形裁剪框
        options.setShowCropFrame(true); //设置是否显示裁剪边框(true为方形边框)
        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
        options.setCropGridColor(Color.YELLOW);//设置裁剪网格的颜色
        options.setCropFrameColor(Color.YELLOW);//设置裁剪框的颜色
        uCrop.withOptions(options);
        uCrop.start(this);
    }
    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri ) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
           mHHead.setImageBitmap(bitmap);
            String filePath = resultUri.getEncodedPath();
            String imagePath = Uri.decode(filePath);
            user.setPhoto(imagePath);
            String fileName = UUID.randomUUID().toString();
            savePhoto(fileName);
            Userdb user=new Userdb();
            user.setPhoto(fileName);
            user.updateAll("phonenum=?",mUser.getPhoneNum());
            //Toast.makeText(MainActivity.this, "图片已经保存到:" + imagePath, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }
    private void setPhb(String name){
        String url = getString(R.string.s_url_addPhb);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("NAME", name)
                .add("SCORE",""+0)
                .add("DAY",""+0)
                .add("ZI",""+0)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONArray jsonArray = null;
                try {
                    String state = "";
                    jsonArray = new JSONArray(s);
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("STATE");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + s);
            }
        });
    }
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(MainActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }
    private void initData() {
        //如果手机有sd卡
        try {
            String path = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting";
            File files = new File(path);
            if (!files.exists()) {
                //如果有没有文件夹就创建文件夹
                files.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String path2 = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting/Picture";
            File file1 = new File(path2);
            if (!file1.exists()) {
                //如果有没有文件夹就创建文件夹
                file1.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
           String path3 = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting/PointData";
            File file2 = new File(path3);
            if (!file2.exists()) {
                //如果有没有文件夹就创建文件夹
                file2.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 调用相机拍照
            case CAMERA_REQUEST_CODE:
                File temp = new File(mTempPhotoPath);
                //if(temp==null)break;
                startCropActivity(Uri.fromFile(temp));
                break;
            // 直接从相册获取
            case GALLERY_REQUEST_CODE:
                if(data==null)break;
                startCropActivity(data.getData());
                break;
            // 裁剪图片结果
            case UCrop.REQUEST_CROP:
                handleCropResult(data);
                break;
            // 裁剪图片错误
            case UCrop.RESULT_ERROR:
                handleCropError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView() {
        //顶部头像控件
        mHHead = (ImageView) findViewById(R.id.h_head);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserVal = (TextView) findViewById(R.id.user_val);
        //下面item控件
        mNickName = (ItemView) findViewById(R.id.nickName);
        mVip=(ItemView)findViewById(R.id.vip);
        mSex = (ItemView) findViewById(R.id.sex);
        mSignName = (ItemView) findViewById(R.id.signName);
        mPass = (ItemView) findViewById(R.id.pass);
        mTie=(ItemView)findViewById(R.id.phone);
        mAbout=(ItemView)findViewById(R.id.about);
    }
    private List<Plan> getPlan(String name){
        String url = getString(R.string.s_url_getPlan);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Gson gson1=new Gson();
                planList = gson1.fromJson(s, new TypeToken<List<Plan>>(){}.getType());
            }
        });
        return planList;
    }
    private void savePlan(Plan plan){
        Gson gson = new Gson();
        String str = gson.toJson(plan);
        String url = getString(R.string.s_url_addPlan);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("plan",str)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONArray jsonArray = null;
                try {
                    String state = "";
                    jsonArray = new JSONArray(s);
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("STATE");
                    }
                    if (state.equals("success")){
                    }else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + s);
            }
        });
    }
    private void setData() {
        user=DataSupport.findFirst(Userdb.class);
        Log.d("sss0","dd="+user.getRole());
        mUserName.setText(user.getNickname());
        mUserVal.setText(user.getPhoneNum());
        mNickName.setRightDesc(user.getNickname());
        if(user.getRole()==1) {
            mVip.setRightDesc("普通会员");
        }else mVip.setRightDesc("超级会员");
        mSex.setRightDesc(user.getSex());
        mSignName.setRightDesc(user.getSign());
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .get()
                .url("http://192.168.137.1:8080/HSKHandWrting/pic/"+mUser.getPhoto()+".jpg")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                //将图片显示到ImageView中
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mHHead.setImageBitmap(bitmap);
                    }
                });
                //将图片保存到本地存储卡中
                inputStream.close();
            }
        });
        mNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mVip.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                if(user.getRole()==2){
                    Toast.makeText(MainActivity.this,"您已经开通了啊！",Toast.LENGTH_SHORT).show();
                }else
                vipDialog();
            }
        });
        mPass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent=new Intent(MainActivity.this, PasswordChangeActivity.class);
                startActivity(intent);
            }
        });
        mSignName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                signDialog();
            }
        });
        mTie.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent=new Intent(MainActivity.this, MyTieActivity.class);
                startActivity(intent);
            }
        });
        mAbout.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MainActivity.this,"当前版本：V1.0.0", Toast.LENGTH_SHORT).show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                sexDialog();
            }
        });
        mHHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });
    }
    private void sexDialog(){
        final String[] sex = new String[] { "男","女" };
        Dialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("请选择性别")
                //设置标题
                .setIcon(R.drawable.ic_sex)
                //设置图标
                // 设置对话框显示一个单选List，指定默认选中项，同时设置监听事件处理
                .setSingleChoiceItems(sex, selectedSexIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedSexIndex = which;
                    }
                })
                //添加取消按钮并增加监听处理
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                })
                //添加确定按钮并增加监听处理
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSex.setRightDesc(sex[selectedSexIndex]);
                        Toast.makeText(getApplication(), sex[selectedSexIndex], Toast.LENGTH_SHORT).show();
                        String url = getString(R.string.s_url_change_sex);
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("SEX", sex[selectedSexIndex])
                                .add("PHONE", user.getPhoneNum())
                                .add("FLAG",1+"")
                                .build();
                        Request request = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                JSONArray jsonArray = null;
                                try {
                                    String state = "";
                                    jsonArray = new JSONArray(s);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        state = jsonObject.getString("STATE");
                                        if(!state.equals("0")){
                                            Userdb user=new Userdb();
                                            user.setSex(sex[selectedSexIndex]);
                                            user.updateAll("phonenum=?",mUser.getPhoneNum());
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                })
                .create();
        alertDialog.show();
    }
    private void savePhoto(final String filename){
        String url = getString(R.string.s_url_change_sex);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("SEX", filename)
                .add("PHONE", user.getPhoneNum())
                .add("FLAG",4+"")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONArray jsonArray = null;
                try {
                    String state = "";
                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("STATE");
                        if(!state.equals("0")){
                            Userdb user=new Userdb();
                            user.setPhoto(filename);
                            user.updateAll("phonenum=?",mUser.getPhoneNum());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void signDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_share,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        final Button qx=view.findViewById(R.id.share_qx);
        final Button qd=view.findViewById(R.id.share_qd);
        final EditText et=view.findViewById(R.id.edit_share);
        final TextView title=view.findViewById(R.id.share_title);
        title.setText("更改个性签名");
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sign=et.getText().toString();
                mSignName.setRightDesc(sign);
                String url = getString(R.string.s_url_change_sex);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("SEX", sign)
                        .add("PHONE", user.getPhoneNum())
                        .add("FLAG",3+"")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                                if(!state.equals("0")){
                                    Userdb user=new Userdb();
                                    user.setSign(sign);
                                    user.updateAll("phonenum=?",mUser.getPhoneNum());
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void vipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_cz, null, false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        Button qd = view.findViewById(R.id.cz_qd);
        Button qx = view.findViewById(R.id.cz_qx);
        TextView t = view.findViewById(R.id.cz_t);
        TextView title=view.findViewById(R.id.cz_title);
        title.setText("会员开通");
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVip.setRightDesc("超级会员");
                final String url = getString(R.string.s_url_change_sex);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("SEX", ""+2)
                        .add("PHONE", user.getPhoneNum())
                        .add("FLAG",2+"")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                                if(!state.equals("0")){
                                    Userdb user1=new Userdb();
                                    user1.setRole(2);
                                    user1.updateAll("phonenum=?",mUser.getPhoneNum());
                                    List<Userdb> userlist=DataSupport.where("phoneNum=?",mUser.getPhoneNum()).find(Userdb.class);
                                    if(userlist.size()>0){
                                        user=userlist.get(0);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        t.setText("暂时没有与支付API对接。您是否确定开通？");
        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 900;
        params.height = 900;
        dialog.getWindow().setAttributes(params);
    }
    private void setdb(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        timeStr=simpleDateFormat.format(date);
        List<Userdb> userlist=DataSupport.where("phoneNum=?",mUser.getPhoneNum()).find(Userdb.class);
        if(userlist.size()>0){
            user=userlist.get(0);
        }
        if(userlist.size()==0){
            user=new Userdb();
            user.setNickname(mUser.getNickname());
            user.setPhoneNum(mUser.getPhoneNum());
            user.setPassword(mUser.getPassword());
            user.setSex(mUser.getSex());
            user.setSign(mUser.getSign());
            user.setRole(mUser.getRole());
            user.setPhoto(mUser.getPhoto());
            user.save();
        }
        learns= DataSupport.where("day=?and name=?",timeStr,user.getPhoneNum()).find(Learn.class);
        if(learns.size()<1) {
            for(int i=0;i<6;i++) {
                Learn learn = new Learn();
                learn.setXue(0);
                learn.setStu(0);
                learn.setS_p(1);
                learn.setLevel(i);
                learn.setName(user.getPhoneNum());
                learn.setDay(timeStr);
                learn.save();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //全屏显示，显示时间和电量
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tc=(Button)findViewById(R.id.tc);
        mUser = UserLab.get(MainActivity.this,"","").getUser();
        initData();
        initView();
        setdb();
        setData();
        shequ();
        fenji();
        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getSharedPreferences("userInfo",MODE_PRIVATE);
                if(sp!=null){
                 sp.edit().clear().commit();
                 }
                DataSupport.deleteAll(Userdb.class);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		//将DengLuActivity至于栈顶
                startActivity(intent);
                finish();
                //DestroyActivityUtil destroyActivityUtil = new DestroyActivityUtil();
                //destroyActivityUtil.exit();
            }
        });
        //设置切换Fragment
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        RadioGroupList radigGroupList = new RadioGroupList();
        radioGroup.setOnCheckedChangeListener(radigGroupList);
        //设置默认按钮为选中状态
        radioButton =(RadioButton) findViewById(R.id.btn_0);
        radioButton.setChecked(true);
        //开始处理Fragment
        findViewById(R.id.fragment_main).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_shequ).setVisibility(View.INVISIBLE);
        findViewById(R.id.fragment_my).setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class RadioGroupList implements RadioGroup.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if(group.getId() == R.id.radiogroup)
            {
                switch (checkedId)
                {
                    case R.id.btn_0:
                        findViewById(R.id.fragment_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.fragment_shequ).setVisibility(View.INVISIBLE);
                        findViewById(R.id.fragment_my).setVisibility(View.INVISIBLE);
                        break;
                    case R.id.btn_2:
                        findViewById(R.id.fragment_main).setVisibility(View.INVISIBLE);
                        findViewById(R.id.fragment_shequ).setVisibility(View.VISIBLE);
                        findViewById(R.id.fragment_my).setVisibility(View.INVISIBLE );
                        break;
                    case R.id.btn_3:
                        findViewById(R.id.fragment_main).setVisibility(View.INVISIBLE);
                        findViewById(R.id.fragment_shequ).setVisibility(View.INVISIBLE);
                        findViewById(R.id.fragment_my).setVisibility(View.VISIBLE );
                        break;
                    default :
                        break;
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }
    private void fenji(){
        progressBar=(ProgressBar)findViewById(R.id.f_p);
        title=(TextView)findViewById(R.id.tv_title);
        f_cz=(MyImgBtn) findViewById(R.id.f_cz);
        h=(Button)findViewById(R.id.write);
        Hsk=(TextView)findViewById(R.id.num);
        day=(TextView)findViewById(R.id.day);
        review=(Button)findViewById(R.id.review);
        MyImgBtn myImgBtn=(MyImgBtn)findViewById(R.id.mm);
        MyImgBtn hanList=(MyImgBtn)findViewById(R.id.han_list);
        myImgBtn.setImageResource(R.drawable.tiaozheng);
        myImgBtn.setText("计划调整");
        hanList.setImageResource(R.drawable.jilu);
        hanList.setText("汉字列表");
        f_cz.setImageResource(R.drawable.zhongzhi);
        f_cz.setText("重置");
        studyJ=(Button)findViewById(R.id.study);
        single=(Button)findViewById(R.id.single_word);
        cuozi=(Button)findViewById(R.id.wrong);
        allHan=(TextView)findViewById(R.id.f_all);
        wHan=(TextView)findViewById(R.id.f_pro);
        final List<Plan> plan= DataSupport.where("name=?",user.getPhoneNum()).find(Plan.class);
        List<Replay> replays=DataSupport.where("userPhone=?",user.getPhoneNum()).find(Replay.class);
        if(replays.size()==0){
            GetMyReplayAsyncTask getMyReplayAsyncTask=new GetMyReplayAsyncTask();
            getMyReplayAsyncTask.execute();
        }
        if(plan.size()<1) {
        if(planList.size()<1){
                for (int i = 0; i < 6; i++) {
                    Plan plan1 = new Plan();
                    plan1.setName(user.getPhoneNum());
                    plan1.setLevel(i);
                    plan1.setLearn(0);
                    plan1.setDayPlan(15);
                    if (i == 0) {
                        plan1.setCc(1);
                    } else plan1.setCc(0);
                    plan1.save();
                    savePlan(plan1);
                }
            }
        }else {
            for (int i = 0; i < planList.size(); i++) {
                Plan plan1 = new Plan();
                plan1.setName(planList.get(i).getName());
                plan1.setLevel(planList.get(i).getLevel());
                plan1.setLearn(planList.get(i).getLearn());
                plan1.setDayPlan(planList.get(i).getDayPlan());
                plan1.setCc(planList.get(i).getCc());
                plan1.save();
                }
        }
        plans= DataSupport.where("name = ? and cc = ?",user.getPhoneNum(),"1").find(Plan.class);
        int level=plans.get(0).getLevel();
        learns = DataSupport.where("day=?and name=?and level=?", timeStr, user.getPhoneNum(),""+level).find(Learn.class);
        for(int i=0;i<6;i++) {
            if(i==level) {
                title.setText("HSK" + (i + 1) + "级");
                if (i == 0) {
                    size = 172;
                } else if (i == 1) {
                    size = 344;
                } else if (i == 2) {
                    size = 622;
                } else if (i == 3) {
                    size = 1075;
                } else if (i == 4) {
                    size = 1;
                } else if (i == 5) {
                    size = 1;
                }
            }
        }
        if(learns.size()>0) {
            if(size==plans.get(0).getLearn()){
                h.setText("已学完");
                h.setEnabled(false);
            }else if (learns.get(0).getXue() >= plans.get(0).getDayPlan() && learns.get(0).getStu() == 0) {
                h.setText("再来" + plans.get(0).getDayPlan() + "个字");
            } else if (learns.get(0).getStu() < plans.get(0).getDayPlan() && learns.get(0).getStu() > 0) {
                h.setText("继续学习");
            }
            else {
                h.setText("开始学习");
            }
        }
        int dP=plans.get(0).getDayPlan();
        final int dR=plans.get(0).getLearn();
        progressBar.setMax(size);
        progressBar.setProgress(dR);
        allHan.setText(""+size);
        List<HanZi> hanZiList=DataSupport.findAll(HanZi.class);
        if(hanZiList.size()==0) {
                GetWord(0);
        }
        wHan.setText(""+dR);
        int  d;
        if(size%dP==0) {
            d = (size-dR )/ dP;
        }else
            d=(size-dR)/dP+1;
        day.setText(""+d);
        Hsk.setText(""+dP);
        if(plans.get(0).getLearn()>0){
            review.setVisibility(View.VISIBLE);
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this, ReviewActivity.class);
                    startActivity(intent);
                }
            });
        }

            //学习进度重置
        f_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                czDialog(dR);
            }
        });
        //汉字列表
        hanList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, WordListActivity.class);

                startActivity(intent);
            }
        });
        //开始学习
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(learns.get(0).getXue()==plans.get(0).getDayPlan()) {
                    Learn learn = new Learn();
                    learn.setToDefault("xue");
                    learn.updateAll("day=?and name=?and level=?", timeStr, user.getPhoneNum(),""+plans.get(0).getLevel());
                }
                    Intent intent = new Intent(MainActivity.this, StuActivity.class);
                    startActivity(intent);
            }
        });
        //学习记录
        studyJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AllJiluActivity.class);
                startActivity(intent);
            }
        });
        //单字学习
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SingleWordActivity.class);
                startActivity(intent);
            }
        });
        //错字集
        cuozi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CuoziActivity.class);
                startActivity(intent);
            }
        });
        //计划调整
        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getRole()!=2){
                    Toast.makeText(MainActivity.this,"您还不是超级会员，需要开通会员才能使用该功能！",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    //重置确认对话框
    private void czDialog(int d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_cz, null, false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        Button qd = view.findViewById(R.id.cz_qd);
        Button qx = view.findViewById(R.id.cz_qx);
        TextView t = view.findViewById(R.id.cz_t);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setVisibility(View.GONE);
                h.setEnabled(true);
                Plan plan=new Plan();
                plan.setToDefault("learn");
                plan.updateAll("name = ? and cc=?",user.getPhoneNum(),"1");
                Learn learn=new Learn();
                learn.setToDefault("stu");
                learn.setToDefault("xue");
                learn.updateAll("day=?and name=?",timeStr,user.getPhoneNum());
                progressBar.setMax(size);
                progressBar.setProgress(0);
                allHan.setText(""+size);
                int  d;
                int dP=plans.get(0).getDayPlan();
                if(size%dP==0) {
                    d = size/ dP;
                }else
                    d=size/dP+1;
                day.setText(""+d);
                wHan.setText(""+0);
                h.setText("开始学习");
                dialog.dismiss();
            }
        });
        t.setText("您已经学习了"+d+"个汉字了，重置后将无法恢复该进度,是否确定要重置?");
        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 900;
        params.height = 900;
        dialog.getWindow().setAttributes(params);
    }
    public void shequ() {
        recyclerView = (RecyclerView) findViewById(R.id.tie_view);
        shequ_pro=(ProgressBar)findViewById(R.id.shequ_pro);
        Button chj = (Button) findViewById(R.id.chj);
        phb=(Button)findViewById(R.id.phb);
        phb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PhbActivity.class);
                startActivity(intent);
            }
        });
        chj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChjActivity.class);
                startActivity(intent);
            }
        });
        GetTieAsyncTask getTieAsyncTask=new GetTieAsyncTask();
        getTieAsyncTask.execute();
    }
    private void n(final String n) {
        String url="http://192.168.137.1:8080/HSKHandWrting/pic/"+n+".jpg";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                //将图片显示到ImageView中
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Pic pic=new Pic();
                if(bitmap!=null) {
                    pic.setBitmap(bitmap);
                    pic.setId(n);
                    listb.add(pic);
                }else n(n);
                inputStream.close();
            }
        });
    }
    public class GetTuAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i=0;i<tieList.size();i++) {
                n(tieList.get(i).getPic());
            }
            try {
                while (true) {
                    if (listb.size() ==tieList.size()) {
                        break;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                shequ_pro.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        TieAdapter adapter = new TieAdapter(1,tieList, listb, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    public class GetTieAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getAllTie);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Gson gson1=new Gson();
                    tieList = gson1.fromJson(s, new TypeToken<List<Tie>>(){}.getType());
                }
            });
            try {
                while(true) {
                    if (tieList.size() > 0) {
                        break;
                    }
                }
            }catch (Exception e){
                Log.d("dsa", "phb");
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                GetTuAsyncTask getTuAsyncTask=new GetTuAsyncTask();
                getTuAsyncTask.execute();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    public void GetWord(int k){
        String url = getString(R.string.s_url_getWord);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("num",k+"")
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Gson gson1=new Gson();
                List<WordZ> list= gson1.fromJson(s, new TypeToken<List<WordZ>>(){}.getType());
                for(int i=0;i<list.size();i++){
                  HanZi hanZi=new HanZi();
                  hanZi.setId(list.get(i).getId());
                  hanZi.setName(list.get(i).getName());
                  hanZi.setPy(list.get(i).getPy());
                  hanZi.setBhs(list.get(i).getBhs());
                  hanZi.setBushou(list.get(i).getBushou());
                  Gson gson = new Gson();
                  String str = gson.toJson(list.get(i).getBishun());
                  hanZi.setBishun(str);
                  hanZi.save();
                  }
            }
        });
    }
    public class GetMyReplayAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = "http://192.168.137.1:8080/HSKHandWrting/GetMyReplay";
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("userPhone",user.getPhoneNum())
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Log.d("dsa", "ps="+s);
                    Gson gson1=new Gson();
                    replayList = gson1.fromJson(s, new TypeToken<List<Replay>>(){}.getType());
                }
            });
            try {
                while (true) {
                    if (replayList.size() > 0) {
                        Log.d("dsa", "phb");
                        break;
                    }else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }catch  (Exception e){
                Log.d("dsa", "phb");
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                if(replayList.size()>0){
                    for(int i=0;i<replayList.size();i++){
                        Replay replay=new Replay();
                        replay.setId(replayList.get(i).getId());
                        replay.setName(replayList.get(i).getName());
                        replay.setUserPhone(replayList.get(i).getUserPhone());
                        replay.setReplay_msg(replayList.get(i).getReplay_msg());
                        replay.setScore(replayList.get(i).getScore());
                        replay.save();
                    }
                }
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}

