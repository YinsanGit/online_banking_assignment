package com.finalBanking.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
@MappedSuperclass
public class Base {
    @Column(name = "created_by")
    private String createBy;
    @Column(name = "created_at" , updatable = false)
    private Date createAt;
    @Column(name = "updated_by")
    private String updateBy;
    @Column(name = "updated_at")
    private Date updateAt;
}
