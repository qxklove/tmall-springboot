package com.qxk.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("user")
@Getter
@Setter
@ToString
public class User {
    @TableId(type = IdType.AUTO)
	private int id;

	private String password;
	private String name;
	private String salt;

    @TableField(exist = false)
	private String anonymousName;

	public String getAnonymousName(){
        if (null != anonymousName) {
			return anonymousName;
        }
        if (null == name) {
			anonymousName= null;
        } else if (name.length() <= 1) {
			anonymousName = "*";
        } else if (name.length() == 2) {
			anonymousName = name.substring(0,1) +"*";
        } else {
			char[] cs =name.toCharArray();
			for (int i = 1; i < cs.length-1; i++) {
				cs[i]='*';
			}
			anonymousName = new String(cs);
		}
		return anonymousName;
	}

}


