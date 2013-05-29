package andr.lexibook.mylittlestory.lrrh.ui.widget;

import andr.lexibook.mylittlestory.lrrh.ui.R;
import andr.lexibook.mylittlestory.lrrh.ui.ViewIml.GifMovieView;
import andr.lexibook.mylittlestory.lrrh.ui.ViewIml.PageView;
import android.content.Context;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

/**
 * User: rain
 * Date: 4/22/13
 * Time: 8:23 PM
 */
@SuppressWarnings("deprecation")
public class Page07 extends PageView {

    private GifMovieView window;
    private GifMovieView red;
    private GifMovieView wolf;

    public Page07(Context context) {
        super(context, R.layout.page07);
        setting.setP07New(true);
        layout = (AbsoluteLayout) page.findViewById(R.id.layout_p07);
        layout.setBackgroundResource(bgSrc.setLang(setting.getLangId()).getPageDrawableId(6));

        //get view
        window = (GifMovieView) page.findViewById(R.id.gif_p07_window);
        red = (GifMovieView) page.findViewById(R.id.gif_p07_red);
        wolf = (GifMovieView) page.findViewById(R.id.gif_p07_wolf);
        window.setMovieAsset(ctx.getString(R.string.p07_window));
        red.setMovieAsset(ctx.getString(R.string.p07_red));
        wolf.setMovieAsset(ctx.getString(R.string.p07_wolf));

        //dynamic
        params = (AbsoluteLayout.LayoutParams) window.getLayoutParams();
        params.x = (int) (getWidthScale() * getDimens(R.dimen.p07_window_x));
        params.y = (int) (getHeightScale() * getDimens(R.dimen.p07_window_y));
        window.setLayoutParams(params);

        params = (AbsoluteLayout.LayoutParams) red.getLayoutParams();
        params.x = (int) (getWidthScale() * getDimens(R.dimen.p07_red_x));
        params.y = (int) (getHeightScale() * getDimens(R.dimen.p07_red_y));
        red.setLayoutParams(params);

        params = (AbsoluteLayout.LayoutParams) wolf.getLayoutParams();
        params.x = (int) (getWidthScale() * getDimens(R.dimen.p07_wolf_x));
        params.y = (int) (getHeightScale() * getDimens(R.dimen.p07_wolf_y));
        wolf.setLayoutParams(params);

    }

    public GifMovieView getWindow() {
        if (window.getParent() != null)
            ((AbsoluteLayout) window.getParent()).removeView(window);
        window.setPaused(4000);
        window.invalidate();
        return window;
    }

}
