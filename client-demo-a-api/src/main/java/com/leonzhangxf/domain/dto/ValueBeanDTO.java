package com.leonzhangxf.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

@ApiModel("值实体")
public class ValueBeanDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字符串")
    private String str;

    @ApiModelProperty("整数")
    private Integer inte;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Integer getInte() {
        return inte;
    }

    public void setInte(Integer inte) {
        this.inte = inte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueBeanDTO valueBean = (ValueBeanDTO) o;
        return Objects.equals(str, valueBean.str) &&
                Objects.equals(inte, valueBean.inte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, inte);
    }

    @Override
    public String toString() {
        return "ValueBean{" +
                "str='" + str + '\'' +
                ", inte=" + inte +
                '}';
    }
}
