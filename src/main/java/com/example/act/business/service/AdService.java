package com.example.act.business.service;

import com.example.act.business.dao.AdDao;
import com.example.act.business.domain.Advertisement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdService {
    @Autowired
    AdDao adDao;

    public List<Advertisement> selectAds(Map<String, Object> map) {
        return adDao.selectAds(map);
    }

    public int selectCount(Map<String, Object> map) {
        return adDao.selectCount(map);
    }

    public void addAd(Advertisement ad) {
        adDao.addAd(ad);
    }

    public void deleteAds(Map<String, Object> map) {
        adDao.deleteAds(map);
    }

    public void deleteRoleById(Integer id) {
        adDao.deleteAdById(id);
    }

    public void updateAd(Advertisement ad) {
        adDao.updateAd(ad);
    }
}
