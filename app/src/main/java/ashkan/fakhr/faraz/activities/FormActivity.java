package ashkan.fakhr.faraz.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.PopupWindow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.adapters.ChoiceSpinnerAdapter;
import ashkan.fakhr.faraz.adapters.TopicSpinnerAdapter;
import ashkan.fakhr.faraz.models.FormModel;
import ashkan.fakhr.faraz.models.TopicModel;
import ashkan.fakhr.faraz.utilities.Constants;
import ashkan.fakhr.faraz.utilities.Snippets;
import ashkan.fakhr.faraz.utilities.Spinner;

import static ashkan.fakhr.faraz.utilities.Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static ashkan.fakhr.faraz.utilities.Constants.GALLERY_PICK_IMAGE_REQUEST_CODE;

public class FormActivity extends AppCompatActivity {

    List<TopicModel> topicList;
    LinearLayout formLinLay;
    private String filePath = null;
    private Uri fileUri; // file url to store image/video
    Bitmap bitmap;
    ImageView imgPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        TextView textView = (TextView) findViewById(R.id.toolbarTitle);
        textView.setText(R.string.form_activity_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Snippets.setFontForActivity(findViewById(R.id.root));
        Snippets.setupUI(this, findViewById(R.id.root));

        formLinLay = (LinearLayout) findViewById(R.id.dynamicFormLinLay);
        topicList = JSON.parseArray(getIntent().getExtras().getString(Constants.DATA), TopicModel.class);
        TopicSpinnerAdapter topicSpinnerAdapter = new TopicSpinnerAdapter(this, topicList);
        final Spinner spinner = (Spinner) findViewById(R.id.topicSpinner);
        spinner.setAdapter(topicSpinnerAdapter);
        spinner.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        spinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                spinner.setSelection(position);
                onTopicSelected(topicList.get(position));
                return false;
            }
        });
        onTopicSelected(topicList.get(0));

        final ImageButton btnAddPhoto = (ImageButton) findViewById(R.id.addPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(FormActivity.this, android.R.style.Theme_Material_Light_Dialog)
                        .setMessage(R.string.add_new_photo)
                        .setPositiveButton(R.string.gallery_txt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (Snippets.verifyStoragePermissions(FormActivity.this)) {
                                        pickFromGallery();
                                    }
                                } else {
                                    pickFromGallery();
                                }

                            }
                        })
                        .setNegativeButton(R.string.camera_txt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                captureImage();

                            }
                        }).show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/theme.ttf");
                textView.setTypeface(face);
                textView = (TextView) dialog.findViewById(android.R.id.button1);
                face = Typeface.createFromAsset(getAssets(), "fonts/theme_bold.ttf");
                textView.setTypeface(face);
                textView = (TextView) dialog.findViewById(android.R.id.button2);
                textView.setTypeface(face);

//                Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialog){
//                    @Override
//                    public void onPositiveActionClicked(DialogFragment fragment) {
//                        super.onPositiveActionClicked(fragment);
////                      open gallery to chose a picture
//
//                    }
//
//                    @Override
//                    public void onNegativeActionClicked(DialogFragment fragment) {
//                        super.onNegativeActionClicked(fragment);
////                        open camera to take a new picture
//
//                    }
//                };
//
//                builder.title(getString(R.string.add_new_photo));
//                builder.positiveAction(getString(R.string.gallery_txt));
//                builder.negativeAction(getString(R.string.camera_txt));
//
//// 3. Get the AlertDialog from create()
//                DialogFragment fragment = DialogFragment.newInstance(builder);
//                fragment.show(getSupportFragmentManager(), null);

//                startActivity(new Intent(FormActivity.this, AddPhotoPopupActivity.class));
            }
        });

        Button sentButton = (Button) findViewById(R.id.sentButton);
        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void onTopicSelected(TopicModel topicModel) {
        formLinLay.removeAllViews();
        for (FormModel formModel : topicModel.getForm()) {
            switch (formModel.getType()) {
                case "text":
                    createTextRow(formModel);
                    break;
                case "choices":
                    createChoicesRow(formModel);
                    break;
                case "bool":
                    createCheckBox(formModel);
                    break;
            }
        }

        Snippets.setupUI(this, findViewById(R.id.root));
        ViewAnimator.animate(findViewById(R.id.formScrollView)).alpha(0, 1).duration(600).start();
    }

    private void createCheckBox(FormModel formModel) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_bool, formLinLay, false);
        ((TextView) row.findViewById(R.id.title)).setText(formModel.getLabel());
//        ((CheckBox) row.findViewById(R.id.checkbox)).setText("ب");
        formLinLay.addView(row);

    }

    private void createChoicesRow(FormModel formModel) {

        View row = LayoutInflater.from(this).inflate(R.layout.item_options_spinner, formLinLay, false);
        final Spinner spinner = (Spinner) row.findViewById(R.id.spinner);
        ChoiceSpinnerAdapter choiceSpinnerAdapter = new ChoiceSpinnerAdapter(this, formModel.getConfiguration().getChoices());
        spinner.setAdapter(choiceSpinnerAdapter);
        spinner.getLabelView().setText(formModel.getLabel());
        spinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                spinner.setSelection(position);
                return false;
            }
        });
        formLinLay.addView(row);

    }

    private void createTextRow(FormModel formModel) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_text, formLinLay, false);
        ((TextView) row.findViewById(R.id.title)).setText(formModel.getLabel());
        formLinLay.addView(row);
    }

    private void pickFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_PICK_IMAGE_REQUEST_CODE);
    }

    private void captureImage() {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(this, "دستگاه شما دوربین ندارد", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        * Capturing Camera Image will launch camera app request image capture
        */
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Constants.getOutputMediaFileUri(Constants.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    private boolean isDeviceSupportCamera() {
        // this device has a camera
        // no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filePath = "file:" + image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                filePath = fileUri.getPath();
                try {
                    // bitmap factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    bitmap = BitmapFactory.decodeFile(filePath, options);
                    //resizing the image for better performance
                    if (Math.floor(options.outWidth / 1000) > 4
                            || Math.floor(options.outHeight / 1000) > 4) {
                        options.inSampleSize = 4;
                    } else {
                        if (Math.floor(options.outWidth / 700) > 2
                                || Math.floor(options.outHeight / 700) > 2) {
                            options.inSampleSize = 2;
                        }
                    }
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(filePath, options);
                    previewCapturedImage();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "عکسی گرفته نشد ...", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "شرمنده، مشکلی در خواندن عکس گرفته شده به وجود آمده", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == GALLERY_PICK_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
                // bitmap factory
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = BitmapFactory.decodeFile(filePath, options);
                //resizing the image for better performance
                if (Math.floor(options.outWidth / 1000) > 4
                        || Math.floor(options.outHeight / 1000) > 4) {
                    options.inSampleSize = 4;
                } else {
                    if (Math.floor(options.outWidth / 700) > 2
                            || Math.floor(options.outHeight / 700) > 2) {
                        options.inSampleSize = 2;
                    }
                }
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(filePath, options);

                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "عکسی انتخاب نشد ..", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "شرمنده، مشکلی در خواندن عکس گرفته شده به وجود آمده", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * this method is used to get the bitmap of the image that
     * was taken from camera or chosen from
     * gallery and show the preview lay for it
     */
    private void previewCapturedImage() {
        imgPreview = (ImageView) findViewById(R.id.photoView);
        imgPreview.setImageBitmap(bitmap);

    }

    private void showFlip(final View view, boolean showOrHide) {
        if (showOrHide) {
            ViewAnimator.animate(view).duration(500).alpha(0, 1).translationY(800, 0).scaleX(0.6f, 1f).scaleY(0.6f, 1f).onStart(new AnimationListener.Start() {
                @Override
                public void onStart() {
                    view.setVisibility(View.VISIBLE);
                    view.bringToFront();
                }
            }).start();
        } else {
            ViewAnimator.animate(view).duration(500).alpha(1, 0).translationY(0, 800).scaleX(1f, 0.6f).scaleY(1f, 0.6f).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    view.setVisibility(View.GONE);
                }
            }).start();
        }
    }

}