package net.kdt.pojavlaunch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Rinaorc branded entry point; Amethyst remains the embedded Minecraft engine. */
public final class RinaorcHomeActivity extends Activity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12, 15, 24));
        getWindow().setNavigationBarColor(Color.rgb(12, 15, 24));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        int pad = (int) (28 * getResources().getDisplayMetrics().density + 0.5f);
        root.setPadding(pad, pad, pad, pad);
        root.setBackgroundColor(Color.rgb(12, 15, 24));
        setContentView(root);

        TextView brand = new TextView(this);
        brand.setText("RINAORC");
        brand.setTextColor(Color.rgb(116, 139, 255));
        brand.setTextSize(17);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        brand.setGravity(Gravity.CENTER);
        root.addView(brand);

        TextView title = new TextView(this);
        title.setText("Minecraft Mobile");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, pad / 2, 0, pad / 3);
        root.addView(title);

        TextView version = new TextView(this);
        version.setText("Minecraft Java • 26.3-snapshot-1");
        version.setTextColor(Color.LTGRAY);
        version.setTextSize(15);
        version.setGravity(Gravity.CENTER);
        root.addView(version);

        TextView auth = new TextView(this);
        auth.setText("Moteur Amethyst intégré\nConnexion officielle Microsoft requise");
        auth.setTextColor(Color.rgb(180, 190, 210));
        auth.setTextSize(14);
        auth.setGravity(Gravity.CENTER);
        auth.setPadding(0, pad, 0, pad);
        root.addView(auth);

        Button play = new Button(this);
        play.setText("OUVRIR LE LAUNCHER");
        play.setAllCaps(false);
        play.setTextColor(Color.WHITE);
        play.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(74, 98, 235)));
        root.addView(play, new LinearLayout.LayoutParams(-1, -2));
        play.setOnClickListener(v -> startActivity(new Intent(this, TestStorageActivity.class)));
    }
}
