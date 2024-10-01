package org.xiaoshuyui.simplekb.documentLoader;

import org.xiaoshuyui.simplekb.documentLoader.result.Result;

import java.io.InputStream;

public interface BaseLoader {

    Result extract(InputStream stream);
}
