package com.example.demo.enums;

/**
 * Created by chenzw on 2018/10/22.
 */
public class YybEnum {
    public enum YzName{
        zmz(80034119,"章盟主"),
        zmz2(80032552,"章盟主"),//
        zmz3(80033681,"章盟主"),//
        fxx(80131791,"方新侠"),
        xhgm(80623462,"西湖国贸"),
        zlg(80042615,"赵老哥"),//
        zlg2(80033815,"赵老哥"),//
        zsyx(80034092,"作手新一"),
        cgyj2(80034998,"炒股养家"),//
        cgyj(80549964,"炒股养家"),//
        zssn(80129081,"招商深南");

        private YzName(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getYzName(int code){
            for(YzName d : YzName.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }

}
