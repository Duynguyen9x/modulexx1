package info.androidhive.navigationdrawer.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Laptop TCC on 12/19/2016.
 */
public class SoundUtis {

    private static final String PATH_FILE_MP3 = "mp3/";
    private static final String DUOI_FILE = ".mp3";

    public static void play(Context context, String file) {
        file = file.replace(" ", "_");
        MediaPlayer meidaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(PATH_FILE_MP3 + file + DUOI_FILE);

            meidaPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength()
            );
            afd.close();
            meidaPlayer.prepare();
            meidaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
