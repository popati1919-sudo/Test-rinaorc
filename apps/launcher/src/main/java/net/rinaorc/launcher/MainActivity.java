package net.rinaorc.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import java.util.regex.Pattern;

public final class MainActivity extends Activity {
 private static final String PREFS = "rinaorc_launcher";
 private static final String KEY_USERNAME = "username";
 private EditText usernameInput;

 @Override public void onCreate(Bundle b){
  super.onCreate(b);
  getWindow().setStatusBarColor(Color.rgb(12,15,24));
  getWindow().setNavigationBarColor(Color.rgb(12,15,24));
  SharedPreferences prefs=getSharedPreferences(PREFS,MODE_PRIVATE);
  LinearLayout root=new LinearLayout(this);
  root.setOrientation(1); root.setGravity(Gravity.CENTER);
  root.setPadding(28,28,28,28); root.setBackgroundColor(Color.rgb(12,15,24));
  setContentView(root);

  TextView logo=new TextView(this); logo.setText("RINAORC");
  logo.setTextColor(Color.rgb(116,139,255)); logo.setTextSize(16);
  logo.setGravity(Gravity.CENTER); root.addView(logo);
  TextView title=new TextView(this); title.setText("Minecraft Mobile");
  title.setTextColor(Color.WHITE); title.setTextSize(30); title.setGravity(Gravity.CENTER);
  root.addView(title);
  TextView ver=new TextView(this); ver.setText("Version cible : 26.3");
  ver.setTextColor(Color.LTGRAY); ver.setTextSize(15); root.addView(ver);

  TextView caption=new TextView(this); caption.setText("Ton pseudo Rinaorc");
  caption.setTextColor(Color.WHITE); caption.setTextSize(15);
  caption.setPadding(0,28,0,8); root.addView(caption);
  usernameInput=new EditText(this);
  usernameInput.setSingleLine(true); usernameInput.setTextColor(Color.WHITE);
  usernameInput.setHintTextColor(Color.GRAY); usernameInput.setHint("Ex. Alex_123");
  usernameInput.setText(prefs.getString(KEY_USERNAME,""));
  usernameInput.setPadding(16,12,16,12);
  usernameInput.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(116,139,255)));
  root.addView(usernameInput,new LinearLayout.LayoutParams(-1,-2));

  TextView note=new TextView(this);
  note.setText("Connexion par pseudo, comme sur le launcher officiel. Microsoft n'est pas obligatoire.");
  note.setTextColor(Color.GRAY); note.setGravity(Gravity.CENTER);
  note.setPadding(0,12,0,18); root.addView(note);

  Button play=new Button(this); play.setText("CONTINUER");
  root.addView(play); play.setOnClickListener(v->openEngine());
 }
 private void openEngine(){
  String username=usernameInput.getText().toString().trim();
  if(!Pattern.matches("[A-Za-z0-9_]{3,16}",username)){
   new AlertDialog.Builder(this).setTitle("Pseudo invalide")
    .setMessage("Utilise 3 à 16 caractères : lettres, chiffres ou _.")
    .setPositiveButton("OK",null).show(); return;
  }
  getSharedPreferences(PREFS,MODE_PRIVATE).edit().putString(KEY_USERNAME,username).apply();
  String[] pkgs={"net.rinaorc.engine","net.rinaorc.engine.debug"};
  for(String p:pkgs){
   Intent i=getPackageManager().getLaunchIntentForPackage(p);
   if(i!=null){i.putExtra("net.rinaorc.engine.EXTRA_USERNAME",username);startActivity(i);return;}
  }
  new AlertDialog.Builder(this).setTitle("Engine non installé")
   .setMessage("Installe Rinaorc Engine pour continuer.")
   .setPositiveButton("OK",null).show();
 }
}
