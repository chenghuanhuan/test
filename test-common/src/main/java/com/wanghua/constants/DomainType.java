package com.wanghua.constants;

/**
 * Created by chenghuanhuan on 15/10/12.
 */
public enum DomainType {

    DEFAULT("0000", "默认");

    private String domainType = null;

    private String domainName = null;

    private DomainType(String domainType, String domainName) {
        this.domainType = domainType;
        this.domainName = domainName;
    }

    /**
     * 获取domain名称
     *
     * @return
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * 返回字符串类型的domainType
     *
     * @return
     */
    public String getDomainType() {
        return domainType;
    }

    /**
     * 返回int的dType，名称沿用原来的简写
     *
     * @return
     */
    public int dType() {
        return Integer.valueOf(domainType);
    }

    /**
     * 根据字符的dType构造枚举类型
     *
     * @param domainType
     * @return
     */
    static public DomainType typeOf(String domainType) {
        for (DomainType type : DomainType.values()) {
            if (type.getDomainType().equals(domainType)) {
                return type;
            }
        }

        return DEFAULT;
    }

    /**
     * 根据int的dType构造枚举类型
     *
     * @param dType
     * @return
     */
    static public DomainType typeOf(int dType) {
        String sDomainType = String.format("%04d", dType);
        return typeOf(sDomainType);
    }
}
