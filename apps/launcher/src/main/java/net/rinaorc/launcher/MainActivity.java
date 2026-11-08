package net.rinaorc.launcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
public final class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(12,15,24)); getWindow().setNavigationBarColor(Color.rgb(12,15,24));
  LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setGravity(Gravity.CENTER); root.setPadding(28,28,28,28); root.setBackgroundColor(Color.rgb(12,15,24)); setContentView(root);
  TextView logo=new TextView(this); logo.setText("RINAORC"); logo.setTextColor(Color.rgb(116,139,255)); logo.setTextSize(16); logo.setGravity(Gravity.CENTER); root.addView(logo);
  TextView title=new TextView(this); title.setText("Minecraft Mobile"); title.setTextColor(Color.WHITE); title.setTextSize(30); title.setGravity(Gravity.CENTER); root.addView(title);
  TextView ver=new TextView(this); ver.setText("Version cible : 26.3-snapshot-1"); ver.setTextColor(Color.LTGRAY); ver.setTextSize(15); root.addView(ver);
  Button play=new Button(this); play.setText("Ouvrir Rinaorc Engine"); root.addView(play); play.setOnClickListener(v->openEngine());
  TextView note=new TextView(this); note.setText("Compte Microsoft et profils : intégration à venir. Le moteur doit être installé séparément."); note.setTextColor(Color.GRAY); note.setGravity(Gravity.CENTER); root.addView(note);
 }
 private void openEngine(){String[] pkgs={"net.rinaorc.engine","net.rinaorc.engine.debug"}; for(String p:pkgs){Intent i=getPackageManager().getLaunchIntentForPackage(p); if(i!=null){startActivity(i);return;}}
  new AlertDialog.Builder(this).setTitle("Engine non installé").setMessage("Installe Rinaorc Engine pour continuer.").setPositiveButton("OK",null).show();}
}
