package us.nineworlds.serenity;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SerenityGlideModuleTest {

    SerenityGlideModule module;

    @Mock
    Context mockContext;

    @Mock
    GlideBuilder mockBuilder;

    @Before
    public void setUp() {
        module = new SerenityGlideModule();
    }

    @Test
    public void glidIsSetToUseARGB8888() {
        module.applyOptions(mockContext, mockBuilder);

        verify(mockBuilder).setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

}