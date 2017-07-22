/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.DialogMenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.dialogs.DirectoryChooserDialog;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;

/**
 * A listener that handles long press for video content. Includes displaying a
 * dialog for toggling watched and unwatched status as well for possibly playing
 * on the TV.
 *
 * @author dcarver
 *
 */
public class AbstractVideoOnItemLongClickListener extends BaseInjector {

    protected Dialog dialog;
    protected Activity context;
    protected VideoContentInfo info;
    protected ImageView vciv;

    @Inject
    protected AndroidHelper androidHelper;

    @Inject
    @ForVideoQueue
    protected LinkedList<VideoContentInfo> videoQueue;

    @Inject
    protected SharedPreferences prefs;

    @Inject
    protected VideoQueueHelper videoQueueHelper;


    protected boolean onItemLongClick() {
        context = (Activity) vciv.getContext();

        dialog = new Dialog(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Dialog));
        builder.setTitle(context.getString(R.string.video_options));

        ListView modeList = new ListView(context);
        modeList.setSelector(R.drawable.menu_item_selector);
        ArrayList<DialogMenuItem> options = addMenuOptions();

        ArrayAdapter<DialogMenuItem> modeAdapter = new ArrayAdapter<DialogMenuItem>(
                context, R.layout.simple_list_item, R.id.list_item_text,
                options);

        modeList.setAdapter(modeAdapter);
        modeList.setOnItemClickListener(getDialogSelectedListener());

        builder.setView(modeList);
        dialog = builder.create();
        dialog.show();

        return true;
    }

    /**
     * @return
     */
    protected OnItemClickListener getDialogSelectedListener() {
        return new DialogOnItemSelected();
    }

    protected ArrayList<DialogMenuItem> addMenuOptions() {
        ArrayList<DialogMenuItem> options = new ArrayList<DialogMenuItem>();
        options.add(createMenuItemToggleWatchStatus());
        options.add(createMenuItemDownload());
        options.add(createMenuItemAddToQueue());

        if (!androidHelper.isGoogleTV() && androidHelper.hasSupportedCaster()) {
            options.add(createMenuItemFling());
        }
        return options;
    }

    private DialogMenuItem createMenuItemToggleWatchStatus() {
        return createMenuItem(
                context.getString(R.string.toggle_watched_status), 0);
    }

    private DialogMenuItem createMenuItemDownload() {
        return createMenuItem(
                context.getString(R.string.download_video_to_device), 1);
    }

    private DialogMenuItem createMenuItemAddToQueue() {
        return createMenuItem(context.getString(R.string.add_video_to_queue), 2);
    }

    private DialogMenuItem createMenuItemPlayTrailer() {
        return createMenuItem("Play Trailer", 3);
    }

    private DialogMenuItem createMenuItemFling() {
        return createMenuItem(context.getString(R.string.cast_fling_with_), 4);
    }

    protected DialogMenuItem createMenuItem(String title, int action) {
        DialogMenuItem menuItem = new DialogMenuItem();
        menuItem.setTitle(title);
        menuItem.setMenuDialogAction(action);
        return menuItem;
    }

    protected void performWatchedToggle() {
        View posterLayout = (View) vciv.getParent();
        posterLayout.findViewById(R.id.posterInprogressIndicator)
                .setVisibility(View.INVISIBLE);

        toggleGraphicIndicators(posterLayout);
        info.toggleWatchStatus();
    }

    /**
     * @param posterLayout
     */
    protected void toggleGraphicIndicators(View posterLayout) {
        if (info.isPartiallyWatched() || info.isUnwatched()) {
            final float percentWatched = info.viewedPercentage();
            if (percentWatched <= 0.90f) {
                ImageInfographicUtils.setWatchedCount(vciv, context, info);
                ImageView view = (ImageView) posterLayout
                        .findViewById(R.id.posterWatchedIndicator);
                view.setImageResource(R.drawable.overlaywatched);
                view.setVisibility(View.VISIBLE);
                return;
            }
        }

        ImageInfographicUtils.setUnwatched(vciv, context, info);
        posterLayout.findViewById(R.id.posterWatchedIndicator).setVisibility(
                View.INVISIBLE);
    }

    protected class DialogOnItemSelected implements OnItemClickListener {

        @Override
        public void onItemClick(android.widget.AdapterView<?> arg0, View v,
                                int position, long arg3) {

            DialogMenuItem menuItem = (DialogMenuItem) arg0
                    .getItemAtPosition(position);

            switch (menuItem.getMenuDialogAction()) {
                case 0:
                    performWatchedToggle();
                    break;
                case 1:
                    startDownload();
                    break;
                case 2:
                    videoQueueHelper.performAddToQueue(info);
                    break;
                case 3:
                    androidHelper.performGoogleTVSecondScreen(info, dialog);
            }
            v.requestFocusFromTouch();
            dialog.dismiss();
        }

    }

    protected void startDownload() {
        directoryChooser();
    }

    protected void startDownload(String destination) {

    }

    protected void directoryChooser() {
        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(
                context, new DirectoryChooserDialog.ChosenDirectoryListener() {
            @Override
            public void onChosenDir(String chosenDir) {
                Toast.makeText(
                        context,
                        context.getString(R.string.chosen_directory_)
                                + chosenDir, Toast.LENGTH_LONG).show();
                startDownload(chosenDir);
            }
        });
        directoryChooserDialog.setNewFolderEnabled(true);
        directoryChooserDialog.chooseDirectory("");
    }

}
