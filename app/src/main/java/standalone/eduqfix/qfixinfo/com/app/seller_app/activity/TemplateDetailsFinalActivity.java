package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import standalone.eduqfix.qfixinfo.com.app.R;

public class TemplateDetailsFinalActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView finalTemplate;
    Button whatsappBtn,pdfBtn,fbBtn,share_next;
    SharedPreferences sharedPreferences;
    String page;
    Bitmap bitmap;
    String uri;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_details_final);

        finalTemplate = findViewById(R.id.final_flyer_image);
        whatsappBtn = findViewById(R.id.share_on_whatsapp);
        pdfBtn = findViewById(R.id.save_pdf);
        fbBtn  = findViewById(R.id.share_on_fb);
        share_next = findViewById(R.id.share_next);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        try{
            page = getIntent().getStringExtra("from_page");
            uri = getIntent().getStringExtra("uri");
            filePath = getIntent().getStringExtra("filePath");

        }catch (Exception e){

        }


        Log.d("urri",page);
        try {
             bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
            finalTemplate.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }



        whatsappBtn.setOnClickListener(this);
        pdfBtn.setOnClickListener(this);
        fbBtn.setOnClickListener(this);
        share_next.setOnClickListener(this);

        if(page.equalsIgnoreCase("email")){
            whatsappBtn.setVisibility(View.GONE);
            pdfBtn.setVisibility(View.GONE);
            fbBtn.setVisibility(View.GONE);
            share_next.setVisibility(View.VISIBLE);
        }
        else {
            whatsappBtn.setVisibility(View.VISIBLE);
            pdfBtn.setVisibility(View.VISIBLE);
            fbBtn.setVisibility(View.VISIBLE);
            share_next.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_on_whatsapp:
                onClickApp("com.whatsapp",bitmap);
                break;
            case R.id.share_on_fb:
                onClickApp("com.facebook.katana",bitmap);
                break;
            case R.id.save_pdf:
                createPdf(bitmap);
                break;
            case R.id.share_next:
                Intent intent = new Intent(TemplateDetailsFinalActivity.this,SMSContactListActivity.class);
                intent.putExtra("from_page",page);
                intent.putExtra("path",uri);
                intent.putExtra("filePath",filePath);
                startActivity(intent);
                break;
        }

    }

    public void onClickApp(String pack, Bitmap bitmap) {
        PackageManager pm = getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Template", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//            sendIntent.setPackage("com.whatsapp");
//            startActivity(sendIntent);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, pack);
            startActivity(waIntent);
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(TemplateDetailsFinalActivity.this, "App not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPdf(final Bitmap bm){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        Bitmap bitmap = Bitmap.createScaledBitmap(bm, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        // write the document content
    //    String targetPdf = getFilesDir().getAbsolutePath();
        File mFolder = new File(getFilesDir() + "/sample");
        File imgFile = new File(mFolder.getAbsolutePath() + "/template.pdf");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        if (!imgFile.exists()) {
            try {
                imgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

     //   File filePath = new File(targetPdf);


        try {
            Log.d("fileeeeeee.", String.valueOf(imgFile));
            document.writeTo(new FileOutputStream(imgFile));
            //  btn_generate.setText("Check PDF");
            Toast.makeText(TemplateDetailsFinalActivity.this,"PDF saved Successfully",Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
}
