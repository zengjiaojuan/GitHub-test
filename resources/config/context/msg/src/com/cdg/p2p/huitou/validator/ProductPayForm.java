package com.cddgg.p2p.huitou.validator;


import javax.validation.constraints.Pattern;



/**
 * 产品认购表单
 * @author ldd
 *
 */
public class ProductPayForm {
    
    /**
     * 排除
     */
    @Pattern(regexp="$|[0-9,]+")
    private String exclude;

    

    /**
     * exclude
     * @return  exclude
     */
    public String getExclude() {
        return exclude;
    }

    /**
     * exclude
     * @param exclude   exclude
     */
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
    
}
