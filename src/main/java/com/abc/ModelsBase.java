package com.abc;

/**
 * Created by Prithvi on 11/14/15.
 */
public abstract class ModelsBase  {
    private final long id = DateProvider.getInstance().now().getTime();
    private static final long serialVersionUID = 1L;

    public long getId() {
        return id;
    }
}
