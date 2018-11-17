package com.beadhouse.dao;

import org.apache.ibatis.annotations.Mapper;

import com.beadhouse.domen.ElderUser;
@Mapper
public interface ElderUserMapper {

    ElderUser selectByElderUserEmail(String elderUserEmail);

    ElderUser selectByToken(String token);

    void insertElderUser(ElderUser elderUser);

    void updateUser(ElderUser elderUser);
    
    void updatePassword(ElderUser elderUser);
    
    void updateElderInfo(ElderUser elderUser);
    
    void updateElderAvatar(ElderUser elderUser);
}