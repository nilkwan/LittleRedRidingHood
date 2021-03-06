package andr.lexibook.mylittlestory.lrrh.ui;

import andr.lexibook.mylittlestory.lrrh.control.BgSrc;
import andr.lexibook.mylittlestory.lrrh.control.PageFactory;
import andr.lexibook.mylittlestory.lrrh.libs.FlipViewController;
import andr.lexibook.mylittlestory.lrrh.model.FlipAdapter;
import andr.lexibook.mylittlestory.lrrh.ui.ViewIml.GifMovieView;
import andr.lexibook.mylittlestory.lrrh.ui.widget.Page02;
import andr.lexibook.mylittlestory.lrrh.ui.widget.Page07;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import java.io.IOException;

/**
 * User: rain
 * Date: 4/23/13
 * Time: 8:05 PM
 */
@SuppressWarnings("deprecation")
public class Pages extends BaseActivity implements PageFactory.Callback, FlipViewController.PlayPauseCallBack {

    private FlipViewController flipView;
    private boolean isFirst = true;
    private int position = 0;
    private Fliplistener flipListener;
    private MediaPlayer.OnCompletionListener langCompleteListener;
    private MediaPlayer.OnCompletionListener pageCompleteListener;
    private FlipAdapter flipAdapter;
    private PageFactory pageFactory;
    private GifMovieView p02_grand_start;
    private GifMovieView p02_grand_loop;
    private GifMovieView p02_window;
    private GifMovieView p02_mother;
    private GifMovieView p07_window;
    private Page02 p02;
    private Page07 p07;
    /**
     * there are a strange thing that p02 is true after call factory.get();
     */
    private int p07WindowIndex = -1;
    private int p02WindowIndex = -1;
    private int p02MotherIndex = -1;

    /**
     * add play & pause
     */
    protected Handler mHandler;
    protected TimerThread mTimerThead;
    protected boolean ifAllowFlip = false;
    protected boolean isPaused = false;
    protected boolean isPrepared = false;

    private ImageView ll_play;
    private ImageView ll_pause;
    private AbsoluteLayout.LayoutParams params;
    private View preView;

    protected int[] playPauseLocations = {
            R.array.btn_play_pause_p01
            , R.array.btn_play_pause_p02
            , R.array.btn_play_pause_p03
            , R.array.btn_play_pause_p04
            , R.array.btn_play_pause_p05
            , R.array.btn_play_pause_p06
            , R.array.btn_play_pause_p07
            , R.array.btn_play_pause_p08
            , R.array.btn_play_pause_p09
            , R.array.btn_play_pause_p10
            , R.array.btn_play_pause_p11
    };

    private int[] dimenXs = {R.dimen.btn_play_pause_p01_x
            , R.dimen.btn_play_pause_p02_x
            , R.dimen.btn_play_pause_p03_x
            , R.dimen.btn_play_pause_p04_x
            , R.dimen.btn_play_pause_p05_x
            , R.dimen.btn_play_pause_p06_x
            , R.dimen.btn_play_pause_p07_x
            , R.dimen.btn_play_pause_p08_x
            , R.dimen.btn_play_pause_p09_x
            , R.dimen.btn_play_pause_p10_x
            , R.dimen.btn_play_pause_p11_x
    };
    private int[] dimenYs = {R.dimen.btn_play_pause_p01_y
            , R.dimen.btn_play_pause_p02_y
            , R.dimen.btn_play_pause_p03_y
            , R.dimen.btn_play_pause_p04_y
            , R.dimen.btn_play_pause_p05_y
            , R.dimen.btn_play_pause_p06_y
            , R.dimen.btn_play_pause_p07_y
            , R.dimen.btn_play_pause_p08_y
            , R.dimen.btn_play_pause_p09_y
            , R.dimen.btn_play_pause_p10_y
            , R.dimen.btn_play_pause_p11_y
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bgSrc = BgSrc.getInstance(this);
        pageFactory = PageFactory.getInstance(this);

        flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
        flipView.setPlayPauseCallBack(this);
        flipAdapter = new FlipAdapter(this);
        flipView.setAdapter(flipAdapter);
        flipAdapter.notifyDataSetChanged();
        setContentView(flipView);

//        setting.setAuto(false);
        flipListener = new Fliplistener();
        flipView.setFlipByTouchEnabled(true);
        flipView.setOnViewFlipListener(flipListener);

        if (setting.getReadMode().isAuto()) {
            langCompleteListener = new LangListener();
            pageCompleteListener = new PageListener();
//            flipView.setFlipByTouchEnabled(false);
            if (isFirst) {
                mPlayer = mediaFactory.getPage01();
                mPlayer.setOnCompletionListener(pageCompleteListener);
                try {
                    mPlayer.prepare();
                    isPrepared = true;
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isFirst = false;
            }
        }
        pageFactory.setCallback(this);
        mHandler = new Handler();
        mTimerThead = new TimerThread();

        ll_play = new ImageView(this);
        ll_pause = new ImageView(this);
        ll_play.setBackgroundDrawable(bgSrc.getPlayDrawable());
        ll_pause.setBackgroundDrawable(bgSrc.getPauseDrawable());
        params = new AbsoluteLayout.LayoutParams(43, 43, 677, 412);
        params.x = (int) (getWidthScale() * getResources().getDimension(dimenXs[0]));
        params.y = (int) (getHeightScale() * getResources().getDimension(dimenYs[0]));
        params.width = (int) (getWidthScale() * 43);
        params.height = (int) (getWidthScale() * 43);
    }

    @Override
    public void pauseOrPlay(View view, MotionEvent e) {
        preView = view;
        if (setting.isAuto() && (position >= 0 && position < 11)
                && (e.getAction() == MotionEvent.ACTION_DOWN
                && checkLocation(e, getResources().getIntArray(playPauseLocations[position])))) {
            if (ll_pause.getParent() != null)
                ((AbsoluteLayout) ll_pause.getParent()).removeView(ll_pause);
            if (ll_play.getParent() != null)
                ((AbsoluteLayout) ll_play.getParent()).removeView(ll_play);
            if (isPaused) {
                ll_pause.setLayoutParams(params);
                ((AbsoluteLayout) view).addView(ll_pause);
                isPaused = false;
                if (isPrepared)
                    mPlayer.start();
            } else {
                ll_play.setLayoutParams(params);
                ((AbsoluteLayout) view).addView(ll_play);
                isPaused = true;
                try {
                    mPlayer.pause();
                } catch (Exception ePause) {
                    System.out.println("ePause: " + ePause.getCause());
                }
            }
        }
    }


    @Override
    public void onFliped(View view) {
        System.out.println("onFliped: " + (preView != null));
        if (preView != null) {
            if (ll_play.getParent() != null)
                ((AbsoluteLayout) ll_play.getParent()).removeView(ll_play);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.release();
            toPage(Menu.class);
        }
        return false;
    }

    @Override
    public void setLanguage(int langId) {
        super.setLanguage(langId);
        if (setting.getReadMode().isAuto()) {
            mPlayer.release();
            langPlayer.setOnCompletionListener(langCompleteListener);
        }
        if (langChanged) {
            pageFactory.getPage(this.position).getLayoutView().setBackgroundResource(bgSrc.setLang(langId).getPageDrawableId(this.position));
            flipAdapter.notifyDataSetChanged();
            flipView.flipToPageAgain();
            langChanged = false;
        }
    }

    private void play(int position) {
        this.position = position;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            System.gc();
        }
        switch (position) {
            case 0:
                mPlayer = mediaFactory.getPage01();
                break;
            case 1:
                mPlayer = mediaFactory.getPage02();
                break;
            case 2:
                mPlayer = mediaFactory.getPage03();
                break;
            case 3:
                mPlayer = mediaFactory.getPage04();
                break;
            case 4:
                mPlayer = mediaFactory.getPage05();
                break;
            case 5:
                mPlayer = mediaFactory.getPage06();
                break;
            case 6:
                mPlayer = mediaFactory.getPage07();
                break;
            case 7:
                mPlayer = mediaFactory.getPage08();
                break;
            case 8:
                mPlayer = mediaFactory.getPage09();
                break;
            case 9:
                mPlayer = mediaFactory.getPage10();
                break;
            case 10:
                mPlayer = mediaFactory.getPage11();
                break;
            case 11:
                mPlayer = mediaFactory.getPage12();
                break;
        }
        try {
            mPlayer.setOnCompletionListener(pageCompleteListener);
            mPlayer.prepare();
            isPrepared = true;
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPosition(int posit) {
        this.position = posit;
    }

    @Override
    public void autoFlip() {
        flipView.autoFlip();
        flipView.setFlipByTouchEnabled(true);
    }

    @Override
    public void diableFlip() {
        flipView.setFlipByTouchEnabled(false);
    }

    @SuppressWarnings("deprecation")
    class Fliplistener implements FlipViewController.ViewFlipListener {

        @Override
        public void onViewFlipped(View view, int position) {
            isPaused = false;
            isPrepared = false;
            if ((position >= 0 && position < 11)) {
                params.x = (int) (getWidthScale() * getResources().getDimension(dimenXs[position]));
                params.y = (int) (getHeightScale() * getResources().getDimension(dimenYs[position]));
                params.width = (int) (getWidthScale() * 43);
                params.height = (int) (getWidthScale() * 43);
            }

            /**
             * about slowwer
             */
            mHandler.postDelayed(mTimerThead, 2000);
            flipView.setFlipByTouchEnabled(false);

            //about innormal page
//            if (ll_pause.getParent() != null)
//                ((AbsoluteLayout) ll_pause.getParent()).removeView(ll_pause);
//            ((AbsoluteLayout) view).addView(ll_pause);

            setPosition(position);
            if (setting.getReadMode().isAuto() && !langChanged)
                play(position);
            /**
             * do with abnormal gif of page02
             */
            if (position == 1) {
                p02 = (Page02) pageFactory.getPage(position);
                p02_grand_start = p02.getGrandStart();
                p02_grand_loop = p02.getGrandLoop();
                p02_window = p02.getWindow();
                p02_mother = p02.getMother();
                p02WindowIndex = -1;
                p02MotherIndex = -1;
                for (int i = 0; i < ((AbsoluteLayout) view).getChildCount(); i++) {
                    if (((AbsoluteLayout) view).getChildAt(i).getId() == p02_window.getId())
                        p02WindowIndex = i;
                    if (((AbsoluteLayout) view).getChildAt(i).getId() == p02_mother.getId())
                        p02MotherIndex = i;
                }
                ((AbsoluteLayout) view).addView(p02_grand_start);
                ((AbsoluteLayout) view).addView(p02_grand_loop);
                ((AbsoluteLayout) view).addView(p02_window);
                ((AbsoluteLayout) view).addView(p02_mother);
                if (p02MotherIndex != -1) {
                    ((AbsoluteLayout) view).removeViewAt(p02WindowIndex);
                    if (p02MotherIndex > p02WindowIndex)
                        p02MotherIndex--;
                    ((AbsoluteLayout) view).removeViewAt(p02MotherIndex);
                }
                p02 = null;
                p02_window = null;
                p02_mother = null;
                p02_grand_loop = null;
                p02_grand_start = null;
                System.gc();
            }
            /**
             * do with abnormal gif of page07
             */
            if (position == 6) {
                p07 = (Page07) pageFactory.getPage(position);
                p07_window = p07.getWindow();
                p07WindowIndex = -1;
                for (int i = 0; i < ((AbsoluteLayout) view).getChildCount(); i++) {
                    if (((AbsoluteLayout) view).getChildAt(i).getId() == p07_window.getId())
                        p07WindowIndex = i;
                }
                ((AbsoluteLayout) view).addView(p07_window);
                if (p07WindowIndex != -1) {
                    ((AbsoluteLayout) view).removeViewAt(p07WindowIndex);
                }
                p07 = null;
                p07_window = null;
                System.gc();
            }
        }
    }


    class TimerThread implements Runnable {

        @Override
        public void run() {
            (Pages.this).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timer();
                }
            });
        }

    }


    protected void timer() {
        if (!ifAllowFlip) {
            flipView.setFlipByTouchEnabled(true);
        }
    }

    class LangListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            play(position);
        }
    }

    class PageListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (!isPaused && position >= 0 && position < 11 && !setting.isOOM()) {
                flipView.autoFlip();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Pages OnResume ");
        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pages OnPause ");
        flipView.onPause();
    }

    @Override
    protected void onDestroy() {
        flipView = null;
        mPlayer = null;
        System.out.println("Pages OnDestroy ");
        super.onDestroy();
    }

}


