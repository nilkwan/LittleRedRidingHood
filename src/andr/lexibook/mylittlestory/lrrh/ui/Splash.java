package andr.lexibook.mylittlestory.lrrh.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * @author hezi
 */
public class Splash extends BaseActivity implements View.OnClickListener {

    private Button btn_splash_bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        btn_splash_bg = (Button) findViewById(R.id.btn_splash_bg);
        btn_splash_bg.setOnClickListener(this);
        refreshBg();

        mPlayer = factory.getSplash();
        try {
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMenu();
            }
        }, 1000);

    }

    @Override
    public void onClick(View view) {
        toMenu();
    }

    private void toMenu() {
        System.out.println(" Splash " + setting.getReadMode().isFirst());
        if (setting.getReadMode().isFirst()) {
            setting.getReadMode().setFirst(false);
            setting.save();
            toPage(LangSelect.class);
        } else {
            toPage(Menu.class);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public void changeBgByLang() {
        refreshBg();
    }

    private void refreshBg() {
        this.btn_splash_bg.setBackgroundDrawable(bgFactory.setLang(checkLangToId(setting.getLang())).getSplash());
    }

}
