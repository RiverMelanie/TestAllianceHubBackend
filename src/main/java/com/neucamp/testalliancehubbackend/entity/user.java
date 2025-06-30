package com.neucamp.testalliancehubbackend.entity;
import lombok.Data;

import java.util.Date;
@Data
public class user {
    private Integer userId;
    private Integer companyId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Byte gender;
    private String password;
    private Date createTime;
    private Byte status;
    private Byte isSuper;

    // 构造方法
    public user() {}

    public user(Integer userId, Integer companyId, String username, String nickname,
                String phone, String email, Byte gender, String password,
                Date createTime, Byte status, Byte isSuper) {
        this.userId = userId;
        this.companyId = companyId;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.createTime = createTime;
        this.status = status;
        this.isSuper = isSuper;
    }

    // Getter 和 Setter 方法
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Byte getGender() { return gender; }
    public void setGender(Byte gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }

    public Byte getIsSuper() { return isSuper; }
    public void setIsSuper(Byte isSuper) { this.isSuper = isSuper; }

    // 重写 toString() 方法（可选）
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", companyId=" + companyId +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", isSuper=" + isSuper +
                '}';
    }
}
