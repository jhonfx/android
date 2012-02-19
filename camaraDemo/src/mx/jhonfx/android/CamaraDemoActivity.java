package mx.jhonfx.android;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CamaraDemoActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static int TAKE_PICTURE = 1;
	private static int maxSize=640;
	Intent intent;
	int code = TAKE_PICTURE;
	ImageView iv;
	String nomarch;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	Button btnAction = (Button)findViewById(R.id.btnFoto);
    	intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	iv = (ImageView)findViewById(R.id.imagen);
    	nomarch = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tomafoto.jpg";
    	
    	btnAction.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri output = Uri.fromFile(new File(nomarch));
				//decimos en que archivo guardara la imagen
				intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
				//lanza la camar y esperamos los resultados
				startActivityForResult(intent, code);
			}
		});    
    }
    
  //creamos un método para mostrar fotos del SD card,en pantalla
  	private void MuestraFoto(String arch) {
  	  try {
  		BitmapFactory.Options op=new BitmapFactory.Options();
  		op.inJustDecodeBounds=true;
          int scale = 1;		
  		FileInputStream fd=new FileInputStream(arch);
  		//abrimos el archivo sin reservar memoria
  		BitmapFactory.decodeFileDescriptor(fd.getFD(),null,op);
  		//cambiamos el tamaño de la imagen, a una pequeña para
  		//no desperdiciar memoria y que se vea bien.
          if (op.outHeight > maxSize || op.outWidth > maxSize) {
              double d = Math.pow(2, (int) Math.round(Math.log(maxSize / (double) Math.max(op.outHeight, op.outWidth)) / Math.log(0.5)));
              scale = (int) d;
          }
          op=new BitmapFactory.Options();
          op.inSampleSize=scale;
  		iv.setImageBitmap(BitmapFactory.decodeFileDescriptor(fd.getFD(),null,op));
  	  } catch(Exception e) {
  		  Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
  	  }
  	}
  	
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		// TODO Auto-generated method stub
  		super.onActivityResult(requestCode, resultCode, data);
  		if (requestCode==TAKE_PICTURE) {
  			//si logró tomar la foto,la mostramos
  			MuestraFoto(nomarch);
  		}
  	}
}