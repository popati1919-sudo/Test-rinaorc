package net.rinaorc.engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public final class MainActivity extends Activity {
    private static final String PREFS = "rinaorc_engine";
    private static final String TARGET_VERSION = "26.3-snapshot-1";
    private static final String[] AMETHYST_PACKAGES = {
        "org.angelauramc.amethyst", "org.angelauramc.amethyst.debug"
    };
    private SharedPreferences prefs;
    private LinearLayout root;
    private TextView status;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12, 15, 24));
        getWindow().setNavigationBarColor(Color.rgb(12, 15, 24));
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildUi();
    }

    private int dp(float value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView label(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        t.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        return t;
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(12, 15, 24));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(26), dp(22), dp(32));
        scroll.addView(root);
        setContentView(scroll);

        TextView brand = label("RINAORC", 13, Color.rgb(116, 139, 255));
        brand.setTypeface(null, Typeface.BOLD);
        root.addView(brand);
        TextView title = label("Engine", 34, Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);
        TextView subtitle = label("Minecraft Java Edition • Android ARM64", 14, Color.rgb(160, 169, 190));
        root.addView(subtitle, params(0, dp(30)));

        card("VERSION CIBLE", "Minecraft " + TARGET_VERSION + "\nVersion présélectionnée pour ce projet.");
        card("MOTEUR", "Amethyst Android • branche v3_openjdk");

        Button launch = button("OUVRIR AMETHYST");
        launch.setOnClickListener(v -> launchAmethyst());
        root.addView(launch, params(0, dp(54)));

        status = label("", 13, Color.rgb(169, 181, 207));
        status.setPadding(0, dp(12), 0, dp(12));
        root.addView(status);
        refreshStatus();

        section("PERFORMANCES");
        addProfilePicker();
        addSeek("Mémoire cible", "ram", 512, 8192, 4096, " Mo");
        addSeek("Limite FPS", "fps", 15, 120, 60, " FPS");
        addSeek("Échelle de rendu", "scale", 50, 100, 100, " %");

        section("DIAGNOSTIC");
        Button diag = button("VÉRIFIER L'APPAREIL");
        diag.setOnClickListener(v -> showDiagnostics());
        root.addView(diag, params(0, dp(48)));

        TextView foot = label("Le menu règle les préférences locales. La sélection et le lancement automatique du profil 26.3 dans Amethyst nécessitent une intégration moteur dédiée.", 12, Color.rgb(137, 148, 171));
        foot.setPadding(0, dp(22), 0, 0);
        root.addView(foot);
    }

    private LinearLayout.LayoutParams params(int width, int height) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width == 0 ? -1 : width, height);
        p.bottomMargin = dp(12);
        return p;
    }

    private void section(String title) {
        TextView t = label(title, 12, Color.rgb(116, 139, 255));
        t.setTypeface(null, Typeface.BOLD);
        t.setPadding(0, dp(24), 0, dp(10));
        root.addView(t);
    }

    private void card(String heading, String body) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(14), dp(16), dp(14));
        box.setBackgroundColor(Color.rgb(25, 31, 47));
        TextView h = label(heading, 11, Color.rgb(146, 160, 193));
        h.setTypeface(null, Typeface.BOLD);
        TextView b = label(body, 15, Color.WHITE);
        b.setPadding(0, dp(6), 0, 0);
        box.addView(h); box.addView(b);
        root.addView(box, params(0, -2));
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(Color.WHITE);
        b.setTextSize(13);
        b.setAllCaps(false);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(74, 98, 235)));
        return b;
    }

    private void addProfilePicker() {
        TextView caption = label("Profil de performances", 14, Color.WHITE);
        root.addView(caption, params(0, dp(28)));
        Spinner spinner = new Spinner(this);
        String[] profiles = {"Équilibré", "Économie", "Performance"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, profiles));
        spinner.setSelection(prefs.getInt("profile", 0));
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(android.widget.AdapterView<?> p) {}
            public void onItemSelected(android.widget.AdapterView<?> p, View v, int pos, long id) {
                prefs.edit().putInt("profile", pos).apply();
            }
        });
        root.addView(spinner, params(0, dp(48)));
    }

    private void addSeek(String title, String key, int min, int max, int fallback, String suffix) {
        TextView value = label("", 14, Color.WHITE);
        root.addView(value, params(0, dp(28)));
        SeekBar bar = new SeekBar(this);
        int saved = prefs.getInt(key, fallback);
        bar.setMax(max - min);
        bar.setProgress(Math.max(0, Math.min(max - min, saved - min)));
        value.setText(title + " : " + (min + bar.getProgress()) + suffix);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int progress, boolean fromUser) {
                int n = min + progress;
                value.setText(title + " : " + n + suffix);
                if (fromUser) prefs.edit().putInt(key, n).apply();
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });
        root.addView(bar, params(0, dp(42)));
    }

    private String installedAmethyst() {
        PackageManager pm = getPackageManager();
        for (String pkg : AMETHYST_PACKAGES) {
            try {
                PackageInfo info = pm.getPackageInfo(pkg, 0);
                return pkg + " (" + info.versionName + ")";
            } catch (PackageManager.NameNotFoundException ignored) {}
        }
        return null;
    }

    private void refreshStatus() {
        String found = installedAmethyst();
        status.setText(found == null
            ? "Amethyst non détecté. Installe d'abord une version compatible."
            : "Amethyst détecté : " + found);
    }

    private void launchAmethyst() {
        String found = installedAmethyst();
        if (found == null) {
            new AlertDialog.Builder(this)
                .setTitle("Moteur introuvable")
                .setMessage("Installe Amethyst Android avant de lancer le moteur Rinaorc.")
                .setPositiveButton("OK", null).show();
            return;
        }
        String pkg = found.substring(0, found.indexOf(" ("));
        try {
            Intent intent = new Intent();
            intent.setClassName(pkg, "net.kdt.pojavlaunch.RinaorcBridgeActivity");
            intent.putExtra("net.rinaorc.engine.EXTRA_VERSION", TARGET_VERSION);
            startActivity(intent);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                .setTitle("Pont Rinaorc absent")
                .setMessage("Installe l'APK Amethyst compilé par le workflow Rinaorc pour lancer automatiquement " + TARGET_VERSION + ". Détail : " + e.getMessage())
                .setPositiveButton("OK", null).show();
        }
    }

    private void showDiagnostics() {
        String found = installedAmethyst();
        String report = String.format(Locale.ROOT,
            "Android : %s (API %d)\nABI principale : %s\nABI disponibles : %s\nMinecraft cible : %s\nAmethyst : %s\nRAM réglée : %d Mo\nFPS réglés : %d",
            Build.VERSION.RELEASE, Build.VERSION.SDK_INT, Build.SUPPORTED_ABIS[0],
            java.util.Arrays.toString(Build.SUPPORTED_ABIS), TARGET_VERSION,
            found == null ? "non installé" : found,
            prefs.getInt("ram", 4096), prefs.getInt("fps", 60));
        new AlertDialog.Builder(this).setTitle("Diagnostic Rinaorc").setMessage(report)
            .setPositiveButton("Fermer", null).show();
    }
}
