package us.nineworlds.serenity.test;

import android.widget.ArrayAdapter;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowBaseAdapter;
import org.robolectric.util.ReflectionHelpers;

/**
 * Shadow for {@link android.widget.ArrayAdapter}.
 */
@SuppressWarnings("UnusedDeclaration")
@Implements(ArrayAdapter.class)
public class ShadowArrayAdapter<T> extends ShadowBaseAdapter {
    @RealObject
    private ArrayAdapter<T> realArrayAdapter;

    public int getTextViewResourceId() {
        return ReflectionHelpers.getField(realArrayAdapter, "mFieldId");
    }

    public int getResourceId() {
        return ReflectionHelpers.getField(realArrayAdapter, "mResource");
    }

    public int getDropDownViewResourceId() {
        return ReflectionHelpers.getField(realArrayAdapter, "mDropDownResource");
    }
}