package com.example.cloud_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.Model_State_Login;
import com.example.cloud_note.Model.POST.ModelPostImageNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoteImageActivity extends AppCompatActivity {
    private ImageButton backFromTextNote;
    private ImageButton btnDone;
    private CardView cardViewTextnote;
    private Button btnUpload;
    private ImageView imgBackground;
    private EditText titleName;
    private TextView tvDateCreate;
    private ImageView imgDateCreate;
    private TextView tvTimeCreate;
    private ImageView imgTimeCreate;
    private EditText addContentText;
    private ImageButton menuTextNote;
    String color_background = "#FF7D7D";
    String imageBase64 = "";
    Login daoUser;
    Model_State_Login user;
    String fieldString;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_image);
        backFromTextNote = (ImageButton) findViewById(R.id.back_from_text_note);
        btnDone = (ImageButton) findViewById(R.id.btn_done);
        cardViewTextnote = (CardView) findViewById(R.id.cardView_textnote);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        imgBackground = (ImageView) findViewById(R.id.img_background);
        titleName = (EditText) findViewById(R.id.title_name);
        tvDateCreate = (TextView) findViewById(R.id.tv_dateCreate);
        imgDateCreate = (ImageView) findViewById(R.id.img_dateCreate);
        tvTimeCreate = (TextView) findViewById(R.id.tv_timeCreate);
        imgTimeCreate = (ImageView) findViewById(R.id.img_timeCreate);
        addContentText = (EditText) findViewById(R.id.add_content_text);
        menuTextNote = (ImageButton) findViewById(R.id.menu_text_note);
        daoUser = new Login(NoteImageActivity.this);
        user = daoUser.getLogin();
        imgBackground.setVisibility(View.GONE);
        cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
        backFromTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(NoteImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requesPermisstion();
                } else {
                    pickImage();

                }

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleName.getText().toString();
                String content = addContentText.getText().toString();
                ModelPostImageNote obj = new ModelPostImageNote();
                obj.setTitle(title);
                obj.setData(content);
                obj.setType("image");
                obj.setColor(chuyenMau(color_background));
                obj.setPinned(false);
                obj.setLock("");
                obj.setShare("");
                obj.setDuaAt("");
                obj.setReminAt("");
                if (imageBase64 != "") {
                    getimage(imageBase64);
//                    obj.setMetaData("https://i.ibb.co/px585wQ/d5vi8b0-9925c151-97d4-4318-8a23-af91c8f561d5.jpg");
//                    if (title != "" && content != "") {
//                        postImageNote(obj);
//                    }
                    AsyncTask<Void, Void , String > async = new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            return img();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            Log.e("TAG", "onPostExecute: "+s );
                            obj.setMetaData(s);
                            if (title != "" && content != "") {
                                postImageNote(obj);
                            }

                        }
                    };
                    async.execute();



                } else {
                    obj.setMetaData("");
                    if (title != "" && content != "") {
                        postImageNote(obj);
                    }
                }

            }
        });
        menuTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });
    }

    private void postImageNote(ModelPostImageNote obj) {
        ModelReturn r = new ModelReturn();
        APINote.apiService.post_image_note(user.getIdUer(), obj).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ModelReturn modelReturn) {
                        r.setStatus(modelReturn.getStatus());
                        r.setMessage(modelReturn.getMessage());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (r.getStatus() == 200) {
                            Toast.makeText(NoteImageActivity.this, r.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }

    private String img() {
        OkHttpClient client = new OkHttpClient();
        String base64String = imageBase64;
        String key ="6374d7c9cfa9f0cb372098bdf76d806e";
        String boundary = "Boundary-" + UUID.randomUUID().toString();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=" + boundary);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .addFormDataPart("image", base64String)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();
        String imageUrl="";
        try {
            // Thực hiện yêu cầu và lấy phản hồi trả về
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            response.close();
            // Trích xuất URL của hình ảnh từ phản hồi JSON của ImgBB
            JSONObject jsonObject = new JSONObject(responseBody);
           imageUrl = jsonObject.getJSONObject("data").getString("url");
            // In ra URL của hình ảnh đã tải lê


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: " + data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgBackground.setVisibility(View.VISIBLE);
                imgBackground.setImageBitmap(decodedByte);
                fieldString = "--E7ED2820-5F59-43E5-81F4-9462606A90A2 \\n" +
                        "Content-Disposition: form-data; name =   \\n" +
                        "Content-Type: text/plain; charset =ISO-8859-1  \\n" +
                        "Content-Transfer-Encoding: 8bit \\n" + imageBase64 + " \\n" +
                        " --E7ED2820-5F59-43E5-81F4-9462606A90A2--";

            } catch (IOException e) {
                Log.e("TAG", "onActivityResult: " + e.getMessage());
            }
        }
    }

    private void requesPermisstion() {
        ActivityCompat.requestPermissions(NoteImageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999 & grantResults[0] == 0) {
            // đồng ý

        } else {
            // không đồng ý
            Toast.makeText(NoteImageActivity.this, "Do bạn không đồng ý !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(NoteImageActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int days = dayOfMonth;
                int months = month;
                int years = year;
                tvDateCreate.setText(days + "-" + (months + 1) + "-" + years);
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void dialogTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NoteImageActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                int hour = i;
                int minute = i1;
                tvTimeCreate.setText(hour + ":" + minute);
            }
        }, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    public void Menu_Dialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_select_color);

        //Xác định vị trí cho dialog

        Window window = dialog.getWindow();
        if (window == null) {

        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        //Ánh xạ
        ImageButton red = dialog.findViewById(R.id.color_red);
        ImageButton orange = dialog.findViewById(R.id.color_orange);
        ImageButton yellow = dialog.findViewById(R.id.color_yellow);
        ImageButton green1 = dialog.findViewById(R.id.color_green1);
        ImageButton green2 = dialog.findViewById(R.id.color_green2);
        ImageButton mint = dialog.findViewById(R.id.color_mint);
        ImageButton blue = dialog.findViewById(R.id.color_blue);
        ImageButton purple = dialog.findViewById(R.id.color_purple);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FF7D7D";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FFBC7D";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FAE28C";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#D3EF82";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#A5EF82";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82EFBB";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82C8EF";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#8293EF";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public com.example.cloud_note.Model.Color chuyenMau(String hexColor) {
        Log.e("TAG", "chuyenMau: " + hexColor);
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        Log.e("TAG", "chuyenMau:R " + red);
        Log.e("TAG", "chuyenMau: G" + green);
        Log.e("TAG", "chuyenMau: B" + blue);
        com.example.cloud_note.Model.Color color = new com.example.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(blue);
        color.setG(green);
        color.setR(red);
        return color;
    }

    private void getimage(String myStringData) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "E7ED2820-5F59-43E5-81F4-9462606A90A2";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        // Send parameter #1
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(myStringData + lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        } catch (IOException e) {
            Log.e("TAG", "getimage: " + e.getMessage());
        }


        ByteArrayInputStream content = new ByteArrayInputStream(baos.toByteArray());


        Log.e("TAG", "getimage: ");


    }


}