package com.duyi.readingweb.entity.framework;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Newbanner {
    @TableId
    private Integer newsbannerid;

    private String url;

    private String imgsrc;

    private Integer showednum;

    public Integer getNewsbannerid() {
        return newsbannerid;
    }

    public void setNewsbannerid(Integer newsbannerid) {
        this.newsbannerid = newsbannerid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public Integer getShowednum() {
        return showednum;
    }

    public void setShowednum(Integer showednum) {
        this.showednum = showednum;
    }
}