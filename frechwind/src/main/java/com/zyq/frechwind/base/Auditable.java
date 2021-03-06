package com.zyq.frechwind.base;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 可以执行审核操作的，都可以继承本接口
 */

public interface Auditable {

    /**
     * pass：通过；
     * @param auditFlag
     * @return
     */
    public void setAuditFlag(AuditFlag auditFlag);

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum AuditFlag implements PojoEnum {
        UnApply("未申请", "bgBlue"), NotAudited("未审核","bgYellow"), Confirmed("通过", "bgGreen"), Reject("不通过", "bgRed"), Warn("警告", "bgBlack");

        private String title;
        private String clsName;
        private AuditFlag(String title, String clsName) {
            this.title = title;
            this.clsName = clsName;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name();
        }
        public String getClsName() {
            return clsName;
        }
    }

    public String getId();
}
