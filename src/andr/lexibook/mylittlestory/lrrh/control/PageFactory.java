package andr.lexibook.mylittlestory.lrrh.control;

import andr.lexibook.mylittlestory.lrrh.ui.R;
import andr.lexibook.mylittlestory.lrrh.ui.ViewIml.PageView;
import andr.lexibook.mylittlestory.lrrh.ui.widget.*;
import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: rain
 * Date: 5/3/13
 * Time: 11:54 PM
 */
public class PageFactory {

    private Activity ctx;
    private static PageFactory instance;
    private Map<String, WeakReference<PageView>> pages;
    private Map<String, Class<?>> pagesMap;
    private String[] pagesKey;
//    private MyProgressDialog dialog;

    private PageFactory(Context ctx) {
        this.ctx = (Activity) ctx;
        pages = new WeakHashMap<String, WeakReference<PageView>>();
        pagesKey = this.ctx.getResources().getStringArray(R.array.page_index);
        pagesMap = new WeakHashMap<String, Class<?>>();
        pagesMap.put(pagesKey[0], Page01.class);
        pagesMap.put(pagesKey[1], Page02.class);
        pagesMap.put(pagesKey[2], Page03.class);
        pagesMap.put(pagesKey[3], Page04.class);
        pagesMap.put(pagesKey[4], Page05.class);
        pagesMap.put(pagesKey[5], Page06.class);
        pagesMap.put(pagesKey[6], Page07.class);
        pagesMap.put(pagesKey[7], Page08.class);
        pagesMap.put(pagesKey[8], Page09.class);
        pagesMap.put(pagesKey[9], Page10.class);
        pagesMap.put(pagesKey[10], Page11.class);
        pagesMap.put(pagesKey[11], Page12.class);
//        dialog = new MyProgressDialog(ctx);
    }

    public static PageFactory getInstance(Context ctx) {
        if (instance == null)
            instance = new PageFactory(ctx);
        return instance;
    }

    public PageView getPage(int position) {
        return getPage(pagesKey[position]);
    }

    public boolean loadPage(int position) {
        return getPage(pagesKey[position]) != null;
    }


    public PageView getPage(String key) {
        if (!pages.containsKey(key) || pages.get(key).get() == null) {
            try {
                pages.put(key, new WeakReference<PageView>((PageView) pagesMap.get(key).getConstructors()[0].newInstance(ctx)));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                System.out.println(" ERR InvocationTargetException " + e.getCause().toString());
//               dialog.show();
            } catch (InstantiationException e) {
                e.printStackTrace();
                System.out.println(" ERR InstantiationException " + e.getCause().toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.out.println(" ERR IllegalAccessException " + e.getCause().toString());
            }
        }
        return pages.get(key).get();
    }

    public void removePage(int postion) {
        if (pages.containsKey(pagesKey[postion])) {
            pages.remove(pagesKey[postion]);
        }
    }

    public int getCount() {
        return pagesMap.size();
    }

}
