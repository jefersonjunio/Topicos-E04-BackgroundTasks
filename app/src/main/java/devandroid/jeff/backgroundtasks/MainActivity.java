package devandroid.jeff.backgroundtasks;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadBtn = findViewById(R.id.btn_download);
        EditText txtLink = findViewById(R.id.txt_img_link);
        ImageView imgView = findViewById(R.id.img_picture);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

//        TODO[1]: Processo de download e carregamento da imagem acontecendo na Main Thread, ALTERAR!!
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                // TODO[2]: Exibir barra de progresso quando estiver fazendo download da imagem
                Thread threadDownload= new Thread() {

                    public void run() {
                        Bitmap img = MainActivity.this.downloadImage(txtLink.getText().toString());
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                imgView.setImageBitmap(img);
                            }
                        });

                    }
                };
                threadDownload.start();
            }
        });
    }

    private Bitmap downloadImage(String imgLink) {
        try {
            return ImageDownloader.download(imgLink);
        } catch (IOException e) {
            Log.e("MainActivity", e.toString());
            return null;
        }
    }
}