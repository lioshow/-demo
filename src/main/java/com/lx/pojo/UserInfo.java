package com.lx.pojo;

import com.lx.entity.Education;
import com.lx.entity.Employment;
import com.lx.entity.Idcard;
import com.lx.entity.User;
import lombok.Data;

@Data
public class UserInfo {
    private User user;
    private Idcard idcard;
    private Education education;
    private Employment employment;

    public UserInfo(User user, Idcard idcard, Education education, Employment employment)
    {
        this.user = user;
        this.idcard = idcard;
        this.education = education;
        this.employment = employment;
    }
}
