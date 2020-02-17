package com.ztkmkoo.dss.server.util;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:53
 */
public class CollectionUtils {

    private CollectionUtils() {}

    public static class List {

        private List() {}

        public static boolean isEmpty(java.util.List list) {
            return Objects.isNull(list) || list.isEmpty();
        }
    }
}