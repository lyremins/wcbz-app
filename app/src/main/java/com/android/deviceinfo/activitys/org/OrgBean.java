package com.android.deviceinfo.activitys.org;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.util.List;

/**
 */
public class OrgBean extends BaseResponseBean {


    /**
     * status : 1
     * data : [{"organiz_id":2,"organizArray":[{"id":1,"label":"航空兵xx旅","children":[{"id":4,"label":"机务大队","children":[{"id":1000,"label":"机务一大队","children":[]},{"id":1001,"label":"机务二大队","children":[]},{"id":1000,"label":"机务三分队","children":[]}]},{"id":1001,"label":"修理厂","children":[]},{"id":1002,"label":"场站","children":[{"id":1003,"label":"四站连","children":[]},{"id":1004,"label":"汽车连","children":[]},{"id":1005,"label":"场务连","children":[]},{"id":1006,"label":"航材股","children":[]},{"id":1007,"label":"弹药大队","children":[{"id":1000,"label":"测试部门","children":[]}]}]}]}],"__v":0}]
     */
    public List<DataBean> data;

    public static class DataBean {
        /**
         * organiz_id : 2
         * organizArray : [{"id":1,"label":"航空兵xx旅","children":[{"id":4,"label":"机务大队","children":[{"id":1000,"label":"机务一大队","children":[]},{"id":1001,"label":"机务二大队","children":[]},{"id":1000,"label":"机务三分队","children":[]}]},{"id":1001,"label":"修理厂","children":[]},{"id":1002,"label":"场站","children":[{"id":1003,"label":"四站连","children":[]},{"id":1004,"label":"汽车连","children":[]},{"id":1005,"label":"场务连","children":[]},{"id":1006,"label":"航材股","children":[]},{"id":1007,"label":"弹药大队","children":[{"id":1000,"label":"测试部门","children":[]}]}]}]}]
         * __v : 0
         */

        public int organiz_id;
        public int __v;
        public List<OrganizArrayBean> organizArray;

        public static class OrganizArrayBean {
            /**
             * id : 1
             * label : 航空兵xx旅
             * children : [{"id":4,"label":"机务大队","children":[{"id":1000,"label":"机务一大队","children":[]},{"id":1001,"label":"机务二大队","children":[]},{"id":1000,"label":"机务三分队","children":[]}]},{"id":1001,"label":"修理厂","children":[]},{"id":1002,"label":"场站","children":[{"id":1003,"label":"四站连","children":[]},{"id":1004,"label":"汽车连","children":[]},{"id":1005,"label":"场务连","children":[]},{"id":1006,"label":"航材股","children":[]},{"id":1007,"label":"弹药大队","children":[{"id":1000,"label":"测试部门","children":[]}]}]}]
             */

            public int id;
            public String label;
            public List<ChildrenBeanX> children;

            public static class ChildrenBeanX {
                /**
                 * id : 4
                 * label : 机务大队
                 * children : [{"id":1000,"label":"机务一大队","children":[]},{"id":1001,"label":"机务二大队","children":[]},{"id":1000,"label":"机务三分队","children":[]}]
                 */

                public int id;
                public String label;
                public List<ChildrenBean> children;

                public static class ChildrenBean {
                    /**
                     * id : 1000
                     * label : 机务一大队
                     * children : []
                     */

                    public int id;
                    public String label;
                    public List<?> children;
                }
            }
        }
    }
}
