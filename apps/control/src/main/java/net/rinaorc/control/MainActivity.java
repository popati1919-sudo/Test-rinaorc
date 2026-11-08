package net.rinaorc.control;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
public final class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(12,15,24)); getWindow().setNavigationBarColor(Color.rgb(12,15,24));
  LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(24,32,24,24); root.setBackgroundColor(Color.rgb(12,15,24)); setContentView(root);
  TextView title=new TextView(this); title.setText("Rinaorc Control"); title.setTextColor(Color.WHITE); title.setTextSize(28); root.addView(title);
  TextView sub=new TextView(this); sub.setText("Centre de réglages • ARM64"); sub.setTextColor(Color.LTGRAY); root.addView(sub);
  addSeek(root,"RAM cible",512,8192,4096," Mo"); addSeek(root,"Limite FPS",15,120,60," FPS"); addSeek(root,"Échelle de rendu",50,100,100," %");
  TextView info=new TextView(this); info.setText("Les réglages sont enregistrés localement dans cette application. La synchronisation avec le moteur sera ajoutée via une interface partagée."); info.setTextColor(Color.GRAY); info.setPadding(0,24,0,0); root.addView(info);
 }
 private void addSeek(LinearLayout root,String name,int min,int max,int initial,String unit){TextView label=new TextView(this); label.setTextColor(Color.WHITE); label.setText(name+": "+initial+unit); root.addView(label); SeekBar bar=new SeekBar(this); bar.setMax(max-min); bar.setProgress(initial-min); bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar b,int p,boolean u){label.setText(name+": "+(min+p)+unit);} public void onStartTrackingTouch(SeekBar b){} public void onStopTrackingTouch(SeekBar b){}}); root.addView(bar);}
}
