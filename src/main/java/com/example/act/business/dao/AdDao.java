package com.example.act.business.dao;

import com.example.act.business.domain.Advertisement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AdDao {

    List<Advertisement> selectAds(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    @Insert("insert ad (userid, img, name, createtime) values(#{userid}, #{img}, #{name}, #{createtime})")
    void addAd(Advertisement ad);

    void deleteAds(Map<String, Object> map);

    @Delete("delete from ad where id = #{id}")
    void deleteAdById(Integer id);

    @Update("update ad set name = #{name} where id = #{id}")
    void updateAd(Advertisement ad);
}
