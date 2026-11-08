package net.kdt.pojavlaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

/**
 * Explicit launch bridge for the companion Rinaorc Engine app.
 * Only the project's pinned snapshot is accepted.
 */
public final class RinaorcBridgeActivity extends Activity {
    public static final String EXTRA_VERSION = "net.rinaorc.engine.EXTRA_VERSION";
    private static final String TARGET_VERSION = "26.3-snapshot-1";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        String requested = getIntent().getStringExtra(EXTRA_VERSION);
        if (!TARGET_VERSION.equals(requested)) {
            Toast.makeText(this, "Version Rinaorc non autorisée.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            File versionJson = new File(
                Tools.DIR_HOME_VERSION,
                TARGET_VERSION + File.separator + TARGET_VERSION + ".json"
            );
            if (!versionJson.isFile()) {
                Toast.makeText(this,
                    "Installe d'abord " + TARGET_VERSION + " dans Amethyst, puis relance Rinaorc.",
                    Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LauncherActivity.class));
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
