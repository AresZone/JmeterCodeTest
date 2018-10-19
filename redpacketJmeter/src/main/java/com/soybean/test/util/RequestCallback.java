package com.soybean.test.util;

import java.io.InputStream;

public interface RequestCallback {
    public abstract boolean processResult(InputStream stream);
}
