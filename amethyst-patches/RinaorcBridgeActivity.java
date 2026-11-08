package net.kdt.pojavlaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import net.kdt.pojavlaunch.value.MinecraftAccount;

import java.io.File;
import java.util.regex.Pattern;

/** Rinaorc bridge: starts the pinned game version with a saved local username profile. */
public final class RinaorcBridgeActivity extends Activity {
    public static final String EXTRA_VERSION = "net.rinaorc.engine.EXTRA_VERSION";
    public static final String EXTRA_USERNAME = "net.rinaorc.engine.EXTRA_USERNAME";
    private static final String TARGET_VERSION = "26.3-snapshot-1";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        String requested = getIntent().getStringExtra(EXTRA_VERSION);
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        if (!TARGET_VERSION.equals(requested)) {
            Toast.makeText(this, "Version Rinaorc non autorisée.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (username == null || !Pattern.matches("[A-Za-z0-9_]{3,16}", username)) {
            Toast.makeText(this, "Entre un pseudo valide dans Rinaorc Launcher.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            // Amethyst's native local-account format: accessToken=0 means offline/local profile.
            MinecraftAccount account = MinecraftAccount.load(username);
            if (account == null) account = new MinecraftAccount();
            account.username = username;
            account.accessToken = "0";
            account.clientToken = "0";
            account.isMicrosoft = false;
            account.save();
            PojavProfile.setCurrentProfile(this, username);

            File versionJson = new File(
                Tools.DIR_HOME_VERSION,
                TARGET_VERSION + File.separator + TARGET_VERSION + ".json"
            );
            if (!versionJson.isFile()) {
                Toast.makeText(this,
                    "Installe d'abord " + TARGET_VERSION + " dans Amethyst, puis relance Rinaorc.",
                    Toast.LENGTH_LONG).show();
                Intent launcher = new Intent(this, LauncherActivity.class);
                launcher.putExtra(EXTRA_VERSION, TARGET_VERSION);
                startActivity(launcher);
                finish();
                return;
            }

            Intent game = new Intent(this, MainActivity.class);
            game.putExtra(MainActivity.INTENT_MINECRAFT_VERSION, TARGET_VERSION);
            game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(game);
        } catch (Throwable error) {
            Toast.makeText(this, "Erreur de lancement Amethyst : " + error.getMessage(),
                Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }
}
